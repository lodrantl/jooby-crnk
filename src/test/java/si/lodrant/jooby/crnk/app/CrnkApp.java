package si.lodrant.jooby.crnk.app;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.jooby.Jooby;
import org.jooby.hbm.Hbm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.lodrant.jooby.crnk.Crnk;

public class CrnkApp extends Jooby {
    final Logger logger = LoggerFactory.getLogger(CrnkApp.class);

    {
        use(new Hbm("db")
                .classes(Beer.class)
        );

        use(new Crnk()
                .path("/api")
                .repositories(new BoxRepository())
                .useJpa()
                .doWith(boot -> {
                    boot.getObjectMapper().setDateFormat(new ISO8601DateFormat());
                }));
    }

    public static void main(final String[] args) {
        run(CrnkApp::new, args);
    }

}
