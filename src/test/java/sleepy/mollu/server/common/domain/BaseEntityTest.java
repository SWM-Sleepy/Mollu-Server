package sleepy.mollu.server.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.repository.ContentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class BaseEntityTest {

    @Autowired
    ContentRepository contentRepository;

    @Test
    @DisplayName("BaseEntity를 상속받은 클래스는 create_at, updated_at 필드를 가진다.")
    void BaseEntityTest() {

        // given
        final String id = "ID";
        final String contentTag = "TAG";

        final Content content = contentRepository.save(Content.builder()
                .id(id)
                .contentTag(contentTag)
                .build());

        // when & then
        assertAll(
                () -> assertThat(content.getCreatedAt()).isNotNull(),
                () -> assertThat(content.getUpdatedAt()).isNotNull()
        );
    }

}