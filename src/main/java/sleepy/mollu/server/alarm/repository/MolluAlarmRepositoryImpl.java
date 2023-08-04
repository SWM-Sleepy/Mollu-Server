package sleepy.mollu.server.alarm.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sleepy.mollu.server.alarm.domain.MolluAlarm;

import java.util.Optional;

import static sleepy.mollu.server.alarm.domain.QMolluAlarm.molluAlarm;

@Repository
@RequiredArgsConstructor
public class MolluAlarmRepositoryImpl implements CustomMolluAlarmRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MolluAlarm> findSecondTop() {
        return Optional.ofNullable(queryFactory.selectFrom(molluAlarm)
                .orderBy(molluAlarm.id.desc())
                .offset(1)
                .limit(1)
                .fetchOne());
    }
}
