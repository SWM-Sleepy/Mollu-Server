package sleepy.mollu.server.content.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
    void 태그_객체가_유효하지_않은_값이_생성될_때_예외를_던진다(String value) {
        assertThatThrownBy(() -> new ContentTag(value)).isInstanceOf(ContentTagBadRequestException.class);
    }

    @ParameterizedTest
    @MethodSource("validContentTag")
    void 태그_객체가_성공적으로_생성된다(String value) {
        assertThatCode(() -> new ContentTag(value)).doesNotThrowAnyException();
    }

}