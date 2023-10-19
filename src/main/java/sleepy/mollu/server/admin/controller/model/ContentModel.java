package sleepy.mollu.server.admin.controller.model;

public record ContentModel(String contentId, String molluDateTime, String uploadDateTime,
                           String frontContentSource, String backContentSource) {
}
