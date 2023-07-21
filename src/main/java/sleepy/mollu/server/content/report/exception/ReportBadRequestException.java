package sleepy.mollu.server.content.report.exception;

import sleepy.mollu.server.common.exception.BadRequestException;

public class ReportBadRequestException extends BadRequestException {

    public ReportBadRequestException(String message) {
        super(message);
    }
}
