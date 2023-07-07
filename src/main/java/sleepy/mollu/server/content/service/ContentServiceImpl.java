package sleepy.mollu.server.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.content.domain.Content;
import sleepy.mollu.server.content.dto.GroupSearchContentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.repository.ContentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    @Override
    public GroupSearchFeedResponse searchGroupFeed() {

        final List<Content> contents = contentRepository.findAll();

        return getGroupSearchFeedResponse(contents);
    }

    private GroupSearchFeedResponse getGroupSearchFeedResponse(List<Content> contents) {

        return new GroupSearchFeedResponse(contents.stream()
                .map(content -> {
                    final String memberName = "memberName";
                    final String groupName = "groupName";
                    final LocalDateTime limitDateTime = LocalDateTime.now();

                    return new GroupSearchContentResponse(
                            content.getId(),
                            memberName,
                            content.getLocation(),
                            groupName,
                            limitDateTime,
                            content.getUpdatedAt(),
                            content.getContentTag(),
                            content.getFrontContentSource(),
                            content.getBackContentSource());
                }).toList());
    }
}
