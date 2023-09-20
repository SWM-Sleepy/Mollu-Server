package sleepy.mollu.server.content.reaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
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
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.member.service.AuthorizationService;

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
    private final IdConstructor idConstructor;

    private final AuthorizationService authorizationService;

    @Transactional
    @Override
    public String createReaction(String memberId, String contentId, String type) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        final EmojiType emojiType = EmojiType.from(type);

        authorizationService.authorizeMemberForContent(member, content);
        checkReactionExists(member, content);
        checkEmojiExists(member, emojiType);

        return saveReaction(member, content, emojiType).getId();
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

    @Override
    public SearchReactionResponse searchReaction(String memberId, String contentId) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        authorizationService.authorizeMemberForContent(member, content);

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
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        final Reaction reaction = getReaction(reactionId);

        authorizationService.authorizeMemberForContent(member, content);
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
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);

        authorizationService.authorizeMemberForContent(member, content);

        return new SearchReactionExistsResponse(getContentReactionStatus(member, content));
    }

    private boolean getContentReactionStatus(Member member, Content content) {
        return reactionRepository.existsByMemberAndContent(member, content);
    }
}
