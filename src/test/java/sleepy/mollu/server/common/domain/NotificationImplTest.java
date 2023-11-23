package sleepy.mollu.server.common.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Platform;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class NotificationImplTest {

    @Autowired
    private NotificationHandler notificationHandler;

    @Test
    @DisplayName("[알림 전송 서비스 호출시] AWS Lambda 함수를 호출한다.")
    void NotificationImplTest() {
        // given
        final String title = "title";
        final String body = "body";
        final Member member1 = Mockito.mock(Member.class);
        final Member member2 = Mockito.mock(Member.class);

        given(member1.hasSamePlatform(Platform.ANDROID)).willReturn(true);
        given(member1.getPhoneToken()).willReturn("token1");
        given(member2.hasSamePlatform(Platform.IOS)).willReturn(true);
        given(member2.getPhoneToken()).willReturn("token2");


        // when
        assertThatCode(() -> notificationHandler.send(List.of(member1, member2), title, body))
                .doesNotThrowAnyException();

        // then
    }
}