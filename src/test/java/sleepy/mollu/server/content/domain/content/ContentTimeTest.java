package sleepy.mollu.server.content.domain.content;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ContentTimeTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    public static Stream<Arguments> falseSource() {
        return Stream.of(
                Arguments.of(NOW),
                Arguments.of(NOW.minusSeconds(1))
        );
    }


    @Test
    @DisplayName("[isUploadedBefore 메서드 호출시] 파라미터보다 업로드 시간이 더 이전인 경우 true를 반환한다.")
    void isUploadedBefore1() {
        // given
        final ContentTime contentTime = ContentTime.of(NOW, NOW.minusSeconds(1));

        // when
        assertThat(contentTime.isUploadedBefore(NOW)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("[isUploadedBefore 메서드 호출시] 업로드 시간이 파라미터보다 크거나 같은 경우 false를 반환한다.")
    @MethodSource("falseSource")
    void isUploadedBefore2(LocalDateTime localDateTime) {
        // given
        final ContentTime contentTime = ContentTime.of(NOW, NOW);

        // when
        assertThat(contentTime.isUploadedBefore(localDateTime)).isFalse();
    }
}