ALTER TABLE comment
    ADD CONSTRAINT FKmrrrpi513ssu63i2783jyiv9m FOREIGN KEY (content_id) REFERENCES content (content_id) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT FKr9jchxqv43o7l4tv7qkoqr4di FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE;