package sleepy.mollu.server.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MolluIdTest {

    public static Stream<Arguments> ValidMolluIdSource() {
        return Stream.of(
                Arguments.of("a".repeat(3)),
                Arguments.of("a".repeat(20)),
                Arguments.of("_m_"),
                Arguments.of("m_o"),
                Arguments.of("m_o_01"));
    }

    public static Stream<Arguments> InValidMolluIdSource() {
        return Stream.of(
                Arguments.of("a".repeat(2)),
                Arguments.of("a".repeat(21)),
                Arguments.of("한국어"),
                Arguments.of("AAA"),
                Arguments.of("___"),
                Arguments.of("111"));
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