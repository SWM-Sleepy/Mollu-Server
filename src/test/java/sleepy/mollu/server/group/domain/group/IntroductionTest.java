package sleepy.mollu.server.group.domain.group;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class IntroductionTest {

    public static Stream<Arguments> validSource() {
        return Stream.of(
                Arguments.of("소"),
                Arguments.of("소".repeat(50))
        );
    }

    public static Stream<Arguments> inValidSource() {
        return Stream.of(
                Arguments.of("소".repeat(51))
        );
    }

    @ParameterizedTest
    @MethodSource("validSource")
    @DisplayName("[Introduction 객체 생성시] 유효한 소개글로 객체를 생성할 수 있다.")
    void IntroductionTest0(String value) {
        assertThatCode(() -> new Introduction(value)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("inValidSource")
    @DisplayName("[Introduction 객체 생성시] 유효하지 않은 소개글로 객체를 생성할 수 없다.")
    void IntroductionTest1(String value) {
        assertThatThrownBy(() -> new Introduction(value)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[Introduction 객체 생성시] 소개글이 없으면 빈 문자열로 객체가 생성된다.")
    void IntroductionTest2() {
        // given & when
        final Introduction introduction = new Introduction(null);

        // then
        assertThat(introduction.getValue()).isEmpty();
    }
}