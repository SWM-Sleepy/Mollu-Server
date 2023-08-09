package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.content.ContentSource;
import sleepy.mollu.server.content.domain.content.ContentTime;
import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDateTime;

public class ContentFixture {

    public static final String DEFAULT_LOCATION = "DEFAULT_LOCATION";
    public static final String DEFAULT_QUESTION = "DEFAULT_QUESTION";
    public static final String DEFAULT_FRONT_CONTENT_FILE_URL = "DEFAULT_FRONT_CONTENT_FILE_URL";
    public static final String DEFAULT_BACK_CONTENT_FILE_URL = "DEFAULT_BACK_CONTENT_FILE_URL";
    public static final LocalDateTime DEFAULT_MOLLU_TIME = LocalDateTime.now();

    public static Content create(String contentId, String tag, LocalDateTime uploadDateTime, Member member) {
        return Content.builder()
                .id(contentId)
                .location(DEFAULT_LOCATION)
                .contentTag(tag)
                .question(DEFAULT_QUESTION)
                .contentTime(ContentTime.of(DEFAULT_MOLLU_TIME, uploadDateTime))
                .contentSource(ContentSource.of(DEFAULT_FRONT_CONTENT_FILE_URL, DEFAULT_BACK_CONTENT_FILE_URL))
                .member(member)
                .build();
    }
}
