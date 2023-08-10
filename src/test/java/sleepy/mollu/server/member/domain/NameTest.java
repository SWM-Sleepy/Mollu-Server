package sleepy.mollu.server.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {


    public static Stream<Arguments> ValidNameSource() {
        return Stream.of(
                Arguments.of("aa"),
                Arguments.of("a".repeat(10)),
                Arguments.of("김준"),
                Arguments.of("김".repeat(10))
        );
    }

    public static Stream<Arguments> InValidNameSource() {
        return Stream.of(
                Arguments.of("a"),
                Arguments.of("a".repeat(11)),
                Arguments.of("김"),
                Arguments.of("김".repeat(11))
        );
    }

    @ParameterizedTest
    @MethodSource("ValidNameSource")
    @DisplayName("유효한 Name 객체를 생성할 수 있다.")
    void NameTest(String name) {
        assertThatCode(() -> new Name(name)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("InValidNameSource")
    @NullAndEmptySource
    @DisplayName("유효하지 않은 Name 객체를 생성할 수 있다.")
    void NameTest1(String name) {
        assertThatThrownBy(() -> new Name(name)).isInstanceOf(IllegalArgumentException.class);
    }
}