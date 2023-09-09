package sleepy.mollu.server.content.comment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageTest {

    public static Stream<Arguments> invalidMethodSource() {
        return Stream.of(
                Arguments.of("a".repeat(201)),
                Arguments.of("김".repeat(201))
        );
    }

    public static Stream<Arguments> validMethodSource() {
        return Stream.of(
                Arguments.of("a".repeat(1)),
                Arguments.of("a".repeat(200)),
                Arguments.of("김".repeat(1)),
                Arguments.of("김".repeat(200))
        );
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 값으로 Message 객체를 생성할 수 없다.")
    @NullSource
    @MethodSource("invalidMethodSource")
    void MessageTest0(String value) {
        assertThatThrownBy(() -> new Message(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 값으로 Message 객체를 생성할 수 없다.")
    @EmptySource
    @MethodSource("validMethodSource")
    void MessageTest1(String value) {
        assertThatCode(() -> new Message(value))
                .doesNotThrowAnyException();
    }
}