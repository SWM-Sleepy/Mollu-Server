package sleepy.mollu.server.alarm.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class RandomPicker implements Picker {

    private static final Random RANDOM = new Random();

    @Override
    public String pick(List<String> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items는 null이거나 비어있을 수 없습니다.");
        }

        final int randomIndex = RANDOM.nextInt(items.size());
        return items.get(randomIndex);
    }
}
