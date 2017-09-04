package si.lodrant.jooby.crnk.internal;

import io.crnk.core.engine.http.HttpRequestContextBase;
import io.crnk.legacy.internal.RepositoryMethodParameterProvider;
import org.jooby.Mutant;
import org.jooby.Request;
import org.jooby.Response;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.lodrant.jooby.crnk.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JoobyRequestContext implements HttpRequestContextBase {
    final Logger logger = LoggerFactory.getLogger(App.class);

    private final Request request;
    private final Response response;
    private final JoobyParameterProvider parameterProvider;
    private final String pathPrefix;

    public JoobyRequestContext(Request request, Response response, String pathPrefix) {
        this.request = request;
        this.response = response;
        this.pathPrefix = pathPrefix;
        this.parameterProvider = new JoobyParameterProvider(request, response);
    }

    @Override
    public RepositoryMethodParameterProvider getRequestParameterProvider() {
        return parameterProvider;
    }

    @Override
    public String getRequestHeader(String name) {
        return request.header(name).value();
    }

    @Override
    public Map<String, Set<String>> getRequestParameters() {
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Mutant> param : request.params().toMap().entrySet()) {
            result.getOrDefault(param.getKey(), new HashSet<>()).add(param.getValue().value());
        }
        return result;
    }

    @Override
    public String getPath() {
        String path = request.path();
        logger.info("Path prefix: {}", pathPrefix);

        if (pathPrefix != null && path.startsWith(pathPrefix)) {
            path = path.substring(pathPrefix.length());
        }
        logger.info("Request path: {}", path);

        return path;
    }

    @Override
    public String getBaseUrl() {
        String proto = request.secure() ? "https://" : "http://";
        String host;
        Mutant proxy = request.header("X-Forwarded-Host");
        if (proxy.isSet()) {
            host = proxy.value();
        } else {
            host = request.header("Host").value("");
        }

        String base = proto + host + pathPrefix;
        logger.info("Base url: {}", base);
        return base;
    }

    @Override
    public byte[] getRequestBody() throws IOException {
        try {
            Mutant body = request.body();
            if (body.isSet()) {
                return body.value().getBytes();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void setResponseHeader(String name, String value) {
        response.header(name, value);
    }

    @Override
    public void setResponse(int code, byte[] body) throws IOException {
        logger.info("Setting response");
        response.status(code);
        response.type("application/vnd.api+json");
        try {
            response.send(body);
        } catch (Throwable throwable) {
            throw new IOException(throwable);
        }
    }

    @Override
    public String getMethod() {
        String method = request.method().toUpperCase();
        logger.info("Getting method: {}", method);
        return method;
    }

    @Override
    public String getResponseHeader(String name) {
        return response.header(name).value();
    }

    public boolean isUserInRole(String role) {
        CommonProfile profile = request.require(CommonProfile.class);
        return profile.getRoles().contains(role);
    }
}