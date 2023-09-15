package sleepy.mollu.server.group.domain.group;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupNameTest {

    public static Stream<Arguments> validSource() {
        return Stream.of(
                Arguments.of("그".repeat(15)),
                Arguments.of("a".repeat(15)),
                Arguments.of("a")
        );
    }

    public static Stream<Arguments> inValidSource() {
        return Stream.of(
                Arguments.of("그".repeat(16)),
                Arguments.of("a".repeat(16))
        );
    }

    @ParameterizedTest
    @MethodSource("validSource")
    @DisplayName("[GroupName 객체 생성시] 유효한 그룹 이름이 주어지면, 객체가 생성된다.")
    void GroupNameTest0(String value) {
        assertThatCode(() -> new GroupName(value)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("inValidSource")
    @NullAndEmptySource
    @DisplayName("[GroupName 객체 생성시] 유효하지 않은 그룹 이름이 주어지면, 객체가 생성이 실패한다.")
    void GroupNameTest1(String value) {
        assertThatThrownBy(() -> new GroupName(value)).isInstanceOf(IllegalArgumentException.class);
    }
}