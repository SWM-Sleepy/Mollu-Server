package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.group.domain.group.Group;

import java.util.List;
import java.util.UUID;

public class ContentGroupFixture {

    public static ContentGroup create(Content content, Group group) {
        return ContentGroup.builder()
                .id(UUID.randomUUID().toString())
                .content(content)
                .group(group)
                .build();
    }

    public static List<ContentGroup> createAll(List<Content> contents, List<Group> groups) {
        return contents.stream()
                .flatMap(content -> groups.stream()
                        .map(group -> create(content, group)))
                .toList();
    }
}
