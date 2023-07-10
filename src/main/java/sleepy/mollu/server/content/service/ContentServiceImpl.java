package sleepy.mollu.server.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

        final Content content = getContent(request);

        final String frontContentFileUrl = uploadContent(request.frontContentFile());
        final String backContentFileUrl = uploadContent(request.backContentFile());

        content.updateUrl(frontContentFileUrl, backContentFileUrl);

        contentRepository.save(content);
    }

    private Content getContent(CreateContentRequest request) {

        return Content.builder()
                .id(idConstructor.create())
                .location(request.location())
                .contentTag(request.tag())
                .build();
    }

    private String uploadContent(MultipartFile file) {

        final ContentFile frontContentFile = new ImageContentFile(file);

        return fileHandler.upload(frontContentFile);
    }
}
