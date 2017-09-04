package si.lodrant.jooby.crnk.internal;

import io.crnk.core.engine.http.HttpRequestContextProvider;
import io.crnk.core.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JoobyCrnkModule implements Module {
    final Logger logger = LoggerFactory.getLogger(JoobyCrnkModule.class);

    private HttpRequestContextProvider contextProvider;
    private List<Object> repositories;

    public JoobyCrnkModule(HttpRequestContextProvider contextProvider, List<Object> repositories) {
        this.contextProvider = contextProvider;
        this.repositories = repositories;
    }

    @Override
    public void setupModule(ModuleContext context) {
        context.addSecurityProvider(new JoobySecurityProvider(contextProvider));
        repositories.forEach(r -> {
            logger.info("Registering repository: {}", r.getClass().getName());
            context.addRepository(r);
        });
    }

    @Override
    public String getModuleName() {
        return "jooby";
    }
}
