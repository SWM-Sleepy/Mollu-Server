package sleepy.mollu.server.common.domain;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidConstructor implements IdConstructor {

    @Override
    public String create() {
        return UUID.randomUUID().toString(); // UUIDv4
    }
}
