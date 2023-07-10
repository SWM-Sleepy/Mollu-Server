package sleepy.mollu.server.common.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import sleepy.mollu.server.common.exception.PageRequestBadRequestException;

public class CustomPageRequest extends PageRequest {

    private static final int PAGE_SIZE = 15;

    private CustomPageRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public static CustomPageRequest of(int page) {

        validate(page);
        return new CustomPageRequest(page, PAGE_SIZE, Sort.unsorted());
    }

    private static void validate(int page) {
        if (page < 0) {
            throw new PageRequestBadRequestException("페이지는 0보다 작을 수 없습니다.");
        }
    }
}
