package sleepy.mollu.server.group.groupmember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

    @Query("select gm from GroupMember gm join fetch gm.group g where gm.member = :member")
    List<GroupMember> findAllWithGroupByMember(Member member);
}
