package sleepy.mollu.server.group.domain.group;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodeTest {

    @Test
    @DisplayName("코드를 생성한다")
    void CodeTest() {
        // given & when
        final Code code = Code.generate();

        // then
        assertThat(code.getValue()).hasSize(8);
    }
}