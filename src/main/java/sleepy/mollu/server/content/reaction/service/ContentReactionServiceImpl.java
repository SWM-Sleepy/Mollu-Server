package sleepy.mollu.server.content.reaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.exception.ContentNotFoundException;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionExistsResponse;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionResponse;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionResponse.ReactionResponse;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.content.reaction.exception.ReactionConflictException;
import sleepy.mollu.server.content.reaction.repository.ReactionRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.emoji.domain.EmojiType;
import sleepy.mollu.server.member.emoji.exception.EmojiNotFoundException;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ContentReactionServiceImpl implements ContentReactionService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ReactionRepository reactionRepository;
    private final ContentGroupRepository contentGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final IdConstructor idConstructor;

    @Transactional
    @Override
    public String createReaction(String memberId, String contentId, String type) {
        final Member member = getMember(memberId);
        final Content content = getContent(contentId);
        final EmojiType emojiType = EmojiType.from(type);

        authorizeMemberForContent(member, content);
        checkReactionExists(member, content);
        checkEmojiExists(member, emojiType);

        return saveReaction(member, content, emojiType).getId();
    }

    private List<Member> getGroupMembersByContent(Content content) {
        final List<Group> groupsByContent = contentGroupRepository.findAllByContent(content)
                .stream()
                .map(ContentGroup::getGroup)
                .toList();
        return groupMemberRepository.findAllByGroupIn(groupsByContent)
                .stream()
                .map(GroupMember::getMember)
                .toList();
    }

    private void authorizeMemberForContent(Member member, Content content) {
        final List<Member> membersByGroups = getGroupMembersByContent(content);
        if (!membersByGroups.contains(member)) {
            throw new MemberUnAuthorizedException("해당 컨텐츠에 대한 접근 권한이 없습니다.");
        }
    }

    private void checkReactionExists(Member member, Content content) {
        if (reactionRepository.existsByMemberAndContent(member, content)) {
            throw new ReactionConflictException("이미 반응한 컨텐츠입니다.");
        }
    }

    private void checkEmojiExists(Member member, EmojiType emojiType) {
        if (!member.hasEmojiFrom(emojiType)) {
            throw new EmojiNotFoundException("[" + emojiType + "] 타입의 이모지를 찾을 수 없습니다.");
        }
    }

    private Reaction saveReaction(Member member, Content content, EmojiType emojiType) {
        return reactionRepository.save(Reaction.builder()
                .id(idConstructor.create())
                .type(emojiType)
                .reactionSource(member.getEmojiSourceFrom(emojiType))
                .member(member)
                .content(content)
                .build());
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("ID가 [" + memberId + "]인 멤버를 찾을 수 없습니다."));
    }

    private Content getContent(String contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("ID가 [" + contentId + "]인 컨텐츠를 찾을 수 없습니다."));
    }

    @Override
    public SearchReactionResponse searchReaction(String memberId, String contentId) {
        final Member member = getMember(memberId);
        final Content content = getContent(contentId);
        authorizeMemberForContent(member, content);

        final Optional<Reaction> myReaction = getMyReaction(member, content);
        final List<Reaction> otherReactions = getOtherReactions(member, content);

        return getSearchReactionResponse(Stream.concat(myReaction.stream(), otherReactions.stream()).toList());
    }

    private SearchReactionResponse getSearchReactionResponse(List<Reaction> reactions) {
        return new SearchReactionResponse(reactions.stream()
                .map(reaction -> new ReactionResponse(
                        reaction.getId(),
                        reaction.getReactionSource(),
                        reaction.getMemberName()))
                .toList());
    }

    private Optional<Reaction> getMyReaction(Member member, Content content) {
        return reactionRepository.findByMemberAndContent(member, content);
    }

    private List<Reaction> getOtherReactions(Member member, Content content) {
        return reactionRepository.findAllByContentExcludesMember(content, member);
    }

    @Transactional
    @Override
    public void deleteReaction(String memberId, String contentId, String reactionId) {
        final Member member = getMember(memberId);
        final Content content = getContent(contentId);
        final Reaction reaction = getReaction(reactionId);

        authorizeMemberForContent(member, content);
        authorizeMemberForReaction(member, reaction);

        reactionRepository.delete(reaction);
    }

    private Reaction getReaction(String reactionId) {
        return reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ReactionConflictException("ID가 [" + reactionId + "]인 반응을 찾을 수 없습니다."));
    }

    private void authorizeMemberForReaction(Member member, Reaction reaction) {
        if (!reaction.isOwner(member)) {
            throw new MemberUnAuthorizedException("해당 반응에 대한 삭제 권한이 없습니다.");
        }
    }

    @Override
    public SearchReactionExistsResponse searchReactionExists(String memberId, String contentId) {
        final Member member = getMember(memberId);
        final Content content = getContent(contentId);

        authorizeMemberForContent(member, content);

        return new SearchReactionExistsResponse(getContentReactionStatus(member, content));
    }

    private boolean getContentReactionStatus(Member member, Content content) {
        return reactionRepository.existsByMemberAndContent(member, content);
    }
}
