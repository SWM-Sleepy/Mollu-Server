package sleepy.mollu.server.common.domain;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDConstructor implements IdConstructor {

    @Override
    public String create() {
        return UUID.randomUUID().toString(); // UUIDv4
    }
}
