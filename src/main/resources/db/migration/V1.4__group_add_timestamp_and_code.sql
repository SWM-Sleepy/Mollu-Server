alter table `group` add column group_code varchar(255) null after introduction;
alter table `group` add column created_at datetime(6) null after group_profile_source;
alter table `group` add column updated_at datetime(6) null after created_at;