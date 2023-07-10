package sleepy.mollu.server.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import sleepy.mollu.server.content.domain.content.ContentTag;
import sleepy.mollu.server.content.exception.ContentTagBadRequestException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentTagTest {

    public static Stream<Arguments> invalidContentTag() {
        return Stream.of(
                Arguments.of("a".repeat(11)),
                Arguments.of("박".repeat(11)),
                Arguments.of(" ")
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
    @NullAndEmptySource
    @DisplayName("태그 객체가 유효하지 않은 값이 생성될 때 예외를 던진다")
    void test1(String value) {
        assertThatThrownBy(() -> new ContentTag(value)).isInstanceOf(ContentTagBadRequestException.class);
    }

    @ParameterizedTest
    @MethodSource("validContentTag")
    @DisplayName("태그 객체가 성공적으로 생성된다")
    void test2(String value) {
        assertThatCode(() -> new ContentTag(value)).doesNotThrowAnyException();
    }

}