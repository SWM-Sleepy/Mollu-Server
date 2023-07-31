package sleepy.mollu.server.group.groupmember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

    @Query("select gm from GroupMember gm join fetch gm.group g where gm.member = :member")
    List<GroupMember> findAllWithGroupByMember(@Param("member") Member member);

    @Query("select gm from GroupMember gm join fetch gm.member m where gm.group = :group")
    List<GroupMember> findAllWithMemberByGroup(@Param("group") Group group);
}
