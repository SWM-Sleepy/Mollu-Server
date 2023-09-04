package sleepy.mollu.server.member.emoji.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmojiTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"emotion1", "emotion2", "emotion3", "emotion4", "emotion5"})
    @DisplayName("유효한 이모티콘 타입을 입력하면 해당 이모티콘 타입을 반환한다.")
    void EmojiTypeTest0(String type) {
        assertThatCode(() -> EmojiType.from(type))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"emotion0", "emotion6"})
    @DisplayName("유효하지 않은 이모티콘 타입을 입력하면 오류가 발생한다.")
    void EmojiTypeTest1(String type) {
        assertThatThrownBy(() -> EmojiType.from(type))
                .isInstanceOf(IllegalArgumentException.class);
    }
}