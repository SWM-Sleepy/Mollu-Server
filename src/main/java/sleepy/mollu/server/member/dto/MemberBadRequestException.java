package sleepy.mollu.server.member.dto;

import sleepy.mollu.server.common.exception.BadRequestException;

public class MemberBadRequestException extends BadRequestException {

    public MemberBadRequestException(String message) {
        super(message);
    }
}
