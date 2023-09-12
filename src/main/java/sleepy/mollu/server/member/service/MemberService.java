package sleepy.mollu.server.member.service;

import sleepy.mollu.server.member.controller.dto.MyCalendarResponse;
import sleepy.mollu.server.member.controller.dto.MyContentsResponse;

import java.time.LocalDate;

public interface MemberService {

    MyContentsResponse searchMyContents(String memberId, LocalDate date);

    MyCalendarResponse searchCalendar(String memberId);

    void deleteMember(String memberId);
}
