alter table group_member add column created_at datetime(6) null after member_id;
alter table group_member add column updated_at datetime(6) null after created_at;