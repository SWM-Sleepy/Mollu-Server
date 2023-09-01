package sleepy.mollu.server.common.domain;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

public class UuidOrTheadConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        final String uuid = MDC.get("uuid");
        if (uuid != null) {
            return uuid;
        }

        return event.getThreadName();
    }
}
