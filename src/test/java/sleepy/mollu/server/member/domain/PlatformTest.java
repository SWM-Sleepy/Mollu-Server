package sleepy.mollu.server.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlatformTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"android", "ios"})
    @DisplayName("유효한 플랫폼을 입력하면 해당 플랫폼을 반환한다.")
    void PlatformTest0(String type) {
        assertThatCode(() -> Platform.from(type))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"androids", "iosa"})
    @DisplayName("유효하지 않은 플랫폼을 입력하면 오류가 발생한다.")
    void PlatformTest1(String type) {
        assertThatThrownBy(() -> Platform.from(type))
                .isInstanceOf(IllegalArgumentException.class);
    }
}