package sleepy.mollu.server.content.comment.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.domain.content.Content;

import java.util.List;
import java.util.Optional;

import static sleepy.mollu.server.content.comment.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Long countByContent(Content content, List<Comment> comments) {
        return queryFactory.select(comment.count())
                .from(comment)
                .where(filterByCommentsAndContent(content, comments))
                .fetchOne();
    }

    @Override
    public Optional<Comment> findTop(Content content, List<Comment> comments) {
        return Optional.ofNullable(queryFactory.selectFrom(comment)
                .join(comment.member).fetchJoin()
                .where(filterByCommentsAndContent(content, comments))
                .orderBy(comment.createdAt.desc())
                .limit(1)
                .fetchFirst());
    }

    private BooleanExpression filterByCommentsAndContent(Content content, List<Comment> comments) {
        return comment.notIn(comments).and(comment.content.eq(content));
    }


}
