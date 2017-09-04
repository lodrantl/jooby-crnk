package si.lodrant.jooby.crnk.internal;

import io.crnk.core.engine.http.HttpRequestContextProvider;
import io.crnk.core.module.Module;
import org.jooby.pac4j.AuthStore;
import org.pac4j.core.profile.CommonProfile;

public class JoobyCrnkModule implements Module {
    private HttpRequestContextProvider contextProvider;

    public JoobyCrnkModule(HttpRequestContextProvider httpRequestContextProvider) {
        contextProvider = httpRequestContextProvider;
    }

    @Override
    public void setupModule(ModuleContext context) {
        context.addSecurityProvider(new JoobySecurityProvider(contextProvider));

    }

    @Override
    public String getModuleName() {
        return "jooby";
    }
}
