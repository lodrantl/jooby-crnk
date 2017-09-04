package si.lodrant.jooby.crnk.internal;

import io.crnk.core.engine.http.HttpRequestContextProvider;
import io.crnk.core.engine.security.SecurityProvider;
import org.jooby.pac4j.AuthSessionStore;
import org.jooby.pac4j.AuthStore;
import org.pac4j.core.profile.CommonProfile;

public class JoobySecurityProvider implements SecurityProvider {
    private HttpRequestContextProvider contextProvider;

    public JoobySecurityProvider(HttpRequestContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public boolean isUserInRole(String role) {
        JoobyRequestContext context = contextProvider.getRequestContext().unwrap(JoobyRequestContext.class);
        return context.isUserInRole(role);
    }

}