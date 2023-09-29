create table comment_report
(
    report_id  bigint       not null
        primary key,
    comment_id varchar(255) null,
    constraint FKlx3w0m2nxfv5s0dixvyftrsbb
        foreign key (report_id) references report (report_id),
    constraint FK8ugevhla12t9n0uw4o0rkvnth
        foreign key (comment_id) references comment (comment_id)
            on delete cascade
);