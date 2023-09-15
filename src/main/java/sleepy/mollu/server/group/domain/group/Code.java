package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    private static final int CODE_LENGTH = 8;
    private static final List<Character> ALLOWED_CHARS = getAllowedChars();
    private static final Random RANDOM = new Random();

    @Column(name = "group_code")
    private String value;

    private Code(String value) {
        this.value = value;
    }

    private static List<Character> getAllowedChars() {
        List<Character> characters = new ArrayList<>();
        IntStream.rangeClosed('A', 'Z').forEach(i -> characters.add((char) i));
        IntStream.rangeClosed('0', '9').forEach(i -> characters.add((char) i));

        return characters;
    }

    public static Code generate() {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, CODE_LENGTH).forEach(i -> stringBuilder.append(getRandomChar()));

        return new Code(stringBuilder.toString());
    }

    private static Character getRandomChar() {
        return ALLOWED_CHARS.get(RANDOM.nextInt(ALLOWED_CHARS.size()));
    }
}
