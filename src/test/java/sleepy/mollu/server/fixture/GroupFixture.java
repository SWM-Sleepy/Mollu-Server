package sleepy.mollu.server.fixture;

import sleepy.mollu.server.group.domain.group.Group;

public class GroupFixture {

    public static final String DEFAULT_ID = "group_id";
    public static final String DEFAULT_NAME = "group_name";
    public static final String DEFAULT_INTRODUCTION = "introduction";
    public static final String DEFAULT_GROUP_PROFILE_SOURCE = "group_profile_source";

    public static Group create() {
        return Group.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .introduction(DEFAULT_INTRODUCTION)
                .groupProfileSource(DEFAULT_GROUP_PROFILE_SOURCE)
                .build();
    }

    public static Group create(String id) {
        return Group.builder()
                .id(id)
                .name(DEFAULT_NAME)
                .introduction(DEFAULT_INTRODUCTION)
                .groupProfileSource(DEFAULT_GROUP_PROFILE_SOURCE)
                .build();
    }
}
