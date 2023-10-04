alter table reaction add column created_at datetime(6) null after `type`;
alter table reaction add column updated_at datetime(6) null after created_at;