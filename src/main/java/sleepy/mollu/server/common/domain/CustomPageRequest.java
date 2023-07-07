package sleepy.mollu.server.common.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageRequest extends PageRequest {

    private static final int PAGE_SIZE = 15;

    private CustomPageRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public static CustomPageRequest of(int page) {
        return new CustomPageRequest(page, PAGE_SIZE, Sort.unsorted());
    }
}
