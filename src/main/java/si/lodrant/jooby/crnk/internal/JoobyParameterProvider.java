package si.lodrant.jooby.crnk.internal;

import io.crnk.legacy.internal.RepositoryMethodParameterProvider;
import org.jooby.Request;
import org.jooby.Response;

import java.lang.reflect.Method;

public class JoobyParameterProvider implements RepositoryMethodParameterProvider {
    private final Request request;
    private final Response response;

    public JoobyParameterProvider(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public <T> T provide(Method method, int parameterIndex) {
        Class<?> parameterType = method.getParameterTypes()[parameterIndex];
        Object returnValue = null;
        if (Request.class.isAssignableFrom(parameterType)) {
            returnValue = request;
        } else if (Response.class.isAssignableFrom(parameterType)) {
            returnValue = response;
        }
        return (T) returnValue;
    }
}
