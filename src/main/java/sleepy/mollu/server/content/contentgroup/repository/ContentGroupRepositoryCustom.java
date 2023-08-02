package sleepy.mollu.server.content.contentgroup.repository;

import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.group.domain.group.Group;

import java.time.LocalDateTime;
import java.util.List;

public interface ContentGroupRepositoryCustom {

    List<ContentGroup> findGroupFeed(List<Group> groups, int pageSize, String cursorId, LocalDateTime cursorEndDate);
}
