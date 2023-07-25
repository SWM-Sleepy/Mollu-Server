package sleepy.mollu.server.group.groupmember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {
}
