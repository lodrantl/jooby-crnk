package si.lodrant.jooby.crnk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.jooby.Jooby;
import org.jooby.hbm.Hbm;
import org.jooby.hbm.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class App extends Jooby {
    final Logger logger = LoggerFactory.getLogger(App.class);

    {
        use(new Hbm("db")
                .classes(Project.class)
        );

        use(new Crnk().path("/api").useJpa().doWith(boot -> {
            boot.getObjectMapper().setDateFormat(new ISO8601DateFormat());
        }));


        get("/projects", req -> {
            return require(UnitOfWork.class).apply(em -> {
                return em.createQuery("from Project").getResultList();
            });
        });

        post("/projects", req -> {
            return require(UnitOfWork.class).apply(em -> {
                em.persist(new Project("Banana" + new Date().toString()));
                return "Created";
            });
        });
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}
