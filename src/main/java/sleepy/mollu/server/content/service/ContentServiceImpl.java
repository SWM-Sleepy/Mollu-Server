package sleepy.mollu.server.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchContentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.repository.ContentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final IdConstructor idConstructor;
    private final FileHandler fileHandler;

    @Override
    public GroupSearchFeedResponse searchGroupFeed(Pageable pageable) {

        final Page<Content> contents = contentRepository.findAll(pageable);

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

    @Override
    public void createContent(CreateContentRequest request) {

        final Content content = Content.builder()
                .id(idConstructor.create())
                .location(request.location())
                .contentTag(request.tag())
                .build();

        final ContentFile frontContentFile = new ImageContentFile(request.frontContentFile());
        final ContentFile backContentFile = new ImageContentFile(request.backContentFile());

        final String frontContentFileUrl = fileHandler.upload(frontContentFile);
        final String backContentFileUrl = fileHandler.upload(backContentFile);

        content.updateUrl(frontContentFileUrl, backContentFileUrl);

        contentRepository.save(content);
    }
}
