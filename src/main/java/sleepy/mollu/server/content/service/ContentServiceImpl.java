package sleepy.mollu.server.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.content.domain.Content;
import sleepy.mollu.server.content.dto.GroupSearchContentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.repository.ContentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    @Override
    public GroupSearchFeedResponse searchGroupFeed(Pageable pageable) {

        final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        final Page<Content> contents = contentRepository.findAll(pageRequest);

        return getGroupSearchFeedResponse(contents);
    }

    private GroupSearchFeedResponse getGroupSearchFeedResponse(Page<Content> contents) {

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
