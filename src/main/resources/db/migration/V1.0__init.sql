create table emoji
(
    emoji_id   bigint auto_increment
        primary key,
    emoji1     varchar(255) null,
    emoji2     varchar(255) null,
    emoji3     varchar(255) null,
    emoji4     varchar(255) null,
    emoji5     varchar(255) null,
    created_at datetime(6)  null,
    updated_at datetime(6)  null가
);

create table `group`
(
    group_id             varchar(255) not null
        primary key,
    group_name           varchar(255) null,
    introduction         varchar(255) null,
    group_profile_source varchar(255) null
);

create table mollu_alarm
(
    id         bigint auto_increment
        primary key,
    mollu_time datetime(6)  null,
    question   varchar(255) null,
    send       tinyint(1)   not null,
    created_at datetime(6)  null,
    updated_at datetime(6)  null
);

create table preference
(
    preference_id bigint auto_increment
        primary key,
    mollu_alarm   tinyint(1)  not null,
    content_alarm tinyint(1)  not null,
    created_at    datetime(6) null,
    updated_at    datetime(6) null
);

create table member
(
    member_id      varchar(255)            not null
        primary key,
    name           varchar(255)            null,
    mollu_id       varchar(255)            null,
    birthday       date                    null,
    phone_token    varchar(255)            null,
    refresh_token  varchar(255)            null,
    profile_source varchar(255)            null,
    preference_id  bigint                  null,
    created_at     datetime(6)             null,
    updated_at     datetime(6)             null,
    platform       enum ('ANDROID', 'IOS') null,
    emoji_id       bigint                  null,
    constraint UK_6t5ktupw4nehpjx7wy2f0q9g6
        unique (emoji_id),
    constraint UK_lma2wojvituay5xedkehuq0fc
        unique (mollu_id),
    constraint UK_ph3rpbt046gvntsy62er8e59o
        unique (preference_id),
    constraint mollu_id
        unique (mollu_id),
    constraint preference_id
        unique (preference_id),
    constraint FKpfxcuexsgqx7iadx3dack9pog
        foreign key (preference_id) references preference (preference_id),
    constraint FKra8srmneu24v0hr4v4b9wd8xi
        foreign key (emoji_id) references emoji (emoji_id)
);

create table content
(
    content_id           varchar(255) not null
        primary key,
    location             varchar(255) null,
    question             varchar(255) null,
    tag                  varchar(255) null,
    mollu_date_time      datetime(6)  null,
    upload_date_time     datetime(6)  null,
    front_content_source varchar(255) null,
    back_content_source  varchar(255) null,
    member_id            varchar(255) null,
    created_at           datetime(6)  null,
    updated_at           datetime(6)  null,
    constraint FK14isx50hfa3s9057mvdchquel
        foreign key (member_id) references member (member_id)
            on delete cascade
);

create table content_group
(
    content_group_id varchar(255) not null
        primary key,
    content_id       varchar(255) null,
    group_id         varchar(255) null,
    created_at       datetime(6)  null,
    updated_at       datetime(6)  null,
    constraint FK1lcvtske6alkylt0m82bw7ryb
        foreign key (group_id) references `group` (group_id),
    constraint FKc796wre2pblj2jpn7jk3lj2th
        foreign key (content_id) references content (content_id)
            on delete cascade
);

create table group_member
(
    group_member_id varchar(255)             not null
        primary key,
    role            enum ('MEMBER', 'OWNER') null,
    group_id        varchar(255)             null,
    member_id       varchar(255)             null,
    constraint FK1jbvj2a8rl8bo9g2djtgpmgcn
        foreign key (group_id) references `group` (group_id),
    constraint FKeamf7nngsg582uxwqgde8o28x
        foreign key (member_id) references member (member_id)
            on delete cascade
);

create table reaction
(
    reaction_id     varchar(255)                                                           not null
        primary key,
    reaction_source varchar(255)                                                           null,
    content_id      varchar(255)                                                           null,
    member_id       varchar(255)                                                           null,
    type            enum ('EMOTICON1', 'EMOTICON2', 'EMOTICON3', 'EMOTICON4', 'EMOTICON5') null,
    constraint FK90je19fepcivb2qmxhd4slx85
        foreign key (content_id) references content (content_id)
            on delete cascade,
    constraint FKf0kgc48u5e6wakvieex07kk5w
        foreign key (member_id) references member (member_id)
            on delete cascade
);

create table report
(
    report_id             bigint auto_increment
        primary key,
    content_report_reason varchar(255) null,
    dtype                 varchar(31)  not null,
    member_id             varchar(255) null,
    created_at            datetime(6)  null,
    updated_at            datetime(6)  null,
    constraint FKel7y5wyx42a6njav1dbe2torl
        foreign key (member_id) references member (member_id)
            on delete cascade
);

create table content_report
(
    report_id  bigint       not null
        primary key,
    content_id varchar(255) null,
    constraint FKbiyf4ldp2t1ekwq13slk2jruo
        foreign key (report_id) references report (report_id),
    constraint FKscyxqecdvcktnu4nixlm6fccv
        foreign key (content_id) references content (content_id)
            on delete cascade
);

