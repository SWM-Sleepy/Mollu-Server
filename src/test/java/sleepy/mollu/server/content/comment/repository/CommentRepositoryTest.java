package sleepy.mollu.server.content.comment.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("[findAllWithMemberByContent 메서드 호출시] 해당 컨텐츠에 대한 댓글들을 멤버와 함께 조회한다.")
    void CommentRepositoryTest() {
        // given
        final Member member1 = saveMember("memberId1", "mollu1");
        final Member member2 = saveMember("memberId2", "mollu2");
        final Member member3 = saveMember("memberId3", "mollu3");
        final Content content = saveContent("contentId", "tag", NOW, member1);

        saveComment("commentId1", member1, content);
        saveComment("commentId2", member2, content);
        saveComment("commentId3", member3, content);

        em.flush();
        em.clear();

        // when
        final List<Comment> comments = commentRepository.findAllWithMemberByContent(content);

        System.out.println("-----------------------");
        comments.forEach(comment ->
                System.out.println("comment.getMemberName() = " + comment.getMemberName())
        );
        System.out.println("-----------------------");

        // then
        assertThat(comments).extracting("id")
                .containsExactly("commentId1", "commentId2", "commentId3");
    }

    @Test
    @DisplayName("[countByContent 메서드 호출시] 신고한 댓글을 제외한 해당 컨텐츠의 댓글 수를 조회한다.")
    void CommentRepositoryTest0() {
        // given
        final Member member1 = saveMember("memberId1", "mollu1");
        final Member member2 = saveMember("memberId2", "mollu2");
        final Member member3 = saveMember("memberId3", "mollu3");
        final Content content = saveContent("contentId", "tag", NOW, member1);

        final Comment comment1 = saveComment("commentId1", member1, content);
        saveComment("commentId2", member2, content);
        saveComment("commentId3", member3, content);

        em.flush();
        em.clear();

        // when
        final Long count = commentRepository.countByContent(content, List.of(comment1));

        // then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("[findTop 메서드 호출시] 신고한 댓글을 제외한 해당 컨텐츠의 가장 최신 댓글을 조회한다.")
    void CommentRepositoryTest1() {
        // given
        final Member member1 = saveMember("memberId1", "mollu1");
        final Member member2 = saveMember("memberId2", "mollu2");
        final Member member3 = saveMember("memberId3", "mollu3");
        final Content content = saveContent("contentId", "tag", NOW, member1);

        saveComment("commentId1", member1, content);
        final Comment comment2 = saveComment("commentId2", member2, content);
        final Comment comment3 = saveComment("commentId3", member3, content);

        em.flush();
        em.clear();

        // when
        final Comment topComment = commentRepository.findTop(content, List.of(comment3))
                .orElseThrow();

        // then
        assertThat(topComment).isEqualTo(comment2);
    }
}