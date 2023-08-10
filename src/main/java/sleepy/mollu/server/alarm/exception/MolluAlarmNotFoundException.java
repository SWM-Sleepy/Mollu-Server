package sleepy.mollu.server.alarm.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class MolluAlarmNotFoundException extends NotFoundException {

    public MolluAlarmNotFoundException(String message) {
        super(message);
    }
}
