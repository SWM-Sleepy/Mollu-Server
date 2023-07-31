package sleepy.mollu.server.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import sleepy.mollu.server.content.domain.content.ContentTag;
import sleepy.mollu.server.content.exception.ContentTagBadRequestException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ContentTagTest {

    public static Stream<Arguments> invalidContentTag() {
        return Stream.of(
                Arguments.of("a".repeat(11)),
                Arguments.of("박".repeat(11))
        );
    }

    public static Stream<Arguments> validContentTag() {
        return Stream.of(
                Arguments.of("a".repeat(5)),
                Arguments.of("김".repeat(5))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidContentTag")
    @DisplayName("태그 객체가 유효하지 않은 값이 생성될 때 예외를 던진다")
    void test1(String value) {
        assertThatThrownBy(() -> new ContentTag(value)).isInstanceOf(ContentTagBadRequestException.class);
    }

    @ParameterizedTest
    @MethodSource("validContentTag")
    @NullAndEmptySource
    @DisplayName("태그 객체가 성공적으로 생성된다")
    void test2(String value) {
        assertThatCode(() -> new ContentTag(value)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("null 값이 들어올 경우 빈 문자열로 초기화된다")
    void ContentTagTest() {
        // given & when
        final ContentTag contentTag = new ContentTag(null);

        // then
        assertThat(contentTag.getValue()).isEmpty();
    }

}