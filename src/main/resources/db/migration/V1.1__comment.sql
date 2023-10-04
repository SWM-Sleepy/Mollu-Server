create table comment
(
    comment_id        varchar(255) not null
        primary key,
    message           varchar(255) null,
    member_id         varchar(255) null,
    content_id        varchar(255) null,
    parent_comment_id varchar(255) null,
    created_at        datetime(6)  null,
    updated_at        datetime(6)  null,

    constraint FKr9jchxqv43o7l4tv7qkoqr4di
        foreign key (content_id) references content (content_id),
    constraint FKmrrrpi513ssu63i2783jyiv9m
        foreign key (member_id) references member (member_id),
    constraint FKhvh0e2ybgg16bpu229a5teje7
        foreign key (parent_comment_id) references comment (comment_id)
);