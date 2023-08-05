package sleepy.mollu.server.acceptance;

import com.google.common.base.CaseFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DBCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager em;

    private List<String> tables;

    @Override
    public void afterPropertiesSet() {
        tables = em.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .toList();
    }

    @Transactional
    public void clean() {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        tables.forEach(table -> {
            if (table.equals("group")) {
                table = "\"" + table + "\"";
            }
            em.createNativeQuery("TRUNCATE TABLE " +table).executeUpdate();
        });

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
