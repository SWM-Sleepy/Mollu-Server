package sleepy.mollu.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.member.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, String> {
}
