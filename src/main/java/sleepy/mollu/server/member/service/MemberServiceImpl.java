package sleepy.mollu.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.member.controller.dto.MyContentsResponse;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Override
    public MyContentsResponse searchMyContents(String memberId, LocalDate date) {
        return null;
    }
}
