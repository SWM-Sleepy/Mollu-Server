package sleepy.mollu.server.content.contentgroup.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.group.domain.group.Group;

import java.time.LocalDateTime;
import java.util.List;

import static sleepy.mollu.server.content.contentgroup.domain.QContentGroup.contentGroup;
import static sleepy.mollu.server.content.domain.content.QContent.content;

@Repository
@RequiredArgsConstructor
public class ContentGroupRepositoryImpl implements ContentGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ContentGroup> findGroupFeed(List<Group> groups, int pageSize, String cursorId, LocalDateTime cursorEndDate) {
        return queryFactory.selectFrom(contentGroup)
                .join(contentGroup.content, content).fetchJoin()
                .join(content.member).fetchJoin()
                .where(contentGroup.group.in(groups), cursorIdAndCursorEndDate(cursorId, cursorEndDate))
                .orderBy(contentGroup.createdAt.desc())
                .limit(pageSize)
                .fetch();
    }

    private Predicate cursorIdAndCursorEndDate(String cursorId, LocalDateTime cursorEndDate) {
        if (cursorId == null && cursorEndDate == null) {
            return null;
        }

        return contentGroup.createdAt.lt(cursorEndDate)
                .or(contentGroup.createdAt.lt(cursorEndDate))
                .and(contentGroup.id.ne(cursorId));
    }
}
