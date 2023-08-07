package sleepy.mollu.server.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MolluIdTest {

    public static Stream<Arguments> ValidMolluIdSource() {
        return Stream.of(
                Arguments.of("mollu"),
                Arguments.of("a"),
                Arguments.of("a".repeat(20)));
    }

    public static Stream<Arguments> InValidMolluIdSource() {
        return Stream.of(
                Arguments.of("a".repeat(21)),
                Arguments.of("한국어"));
    }

    @ParameterizedTest
    @MethodSource("ValidMolluIdSource")
    @DisplayName("유효한 MolluId 객체를 생성할 수 있다.")
    void MolluIdTest0(String molluId) {
        assertThatCode(() -> new MolluId(molluId)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("InValidMolluIdSource")
    @NullAndEmptySource
    @DisplayName("유효하지 않은 MolluId 객체를 생성할 수 없다.")
    void MolluIdTest1(String molluId) {
        assertThatThrownBy(() -> new MolluId(molluId)).isInstanceOf(IllegalArgumentException.class);
    }

}