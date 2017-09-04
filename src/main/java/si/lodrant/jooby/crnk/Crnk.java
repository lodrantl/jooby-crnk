package si.lodrant.jooby.crnk;


import com.google.inject.Binder;
import com.typesafe.config.Config;
import io.crnk.core.boot.CrnkBoot;
import io.crnk.core.engine.dispatcher.RequestDispatcher;
import io.crnk.core.engine.transaction.TransactionRunner;
import io.crnk.core.repository.Repository;
import io.crnk.jpa.DefaultJpaRepositoryFactory;
import io.crnk.jpa.JpaModule;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.hbm.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.lodrant.jooby.crnk.internal.JoobyCrnkModule;
import si.lodrant.jooby.crnk.internal.JoobyPropertiesProvider;
import si.lodrant.jooby.crnk.internal.JoobyRequestContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Crnk implements Jooby.Module {
    final Logger logger = LoggerFactory.getLogger(Crnk.class);

    private CrnkBoot boot = new CrnkBoot();

    private Optional<String> path;
    private boolean useJpa = false;

    private List<Object> repositories = new ArrayList<>();

    public Crnk path(String path) {
        this.path = Optional.of(path);
        return this;
    }

    public Crnk useJpa() {
        useJpa = true;
        return this;
    }

    public Crnk doWith(Consumer<CrnkBoot> configurer) {
        configurer.accept(boot);
        return this;
    }

    public Crnk repositories(Object... repos) {
        this.repositories.addAll(Arrays.asList(repos));
        return this;
    }

    @Override
    public void configure(Env env, Config conf, Binder binder) {
        boot.setPropertiesProvider(new JoobyPropertiesProvider(conf));

        JoobyCrnkModule module = new JoobyCrnkModule(boot.getModuleRegistry().getHttpRequestContextProvider(), repositories);
        boot.addModule(module);

        if (useJpa) {
            env.onStart(registry -> {
                TransactionRunner transactionRunner = new TransactionRunner() {
                    @Override
                    public <T> T doInTransaction(Callable<T> callable) {
                        try {
                            return registry.require(UnitOfWork.class).apply(em -> callable.call());
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            return null;
                        }
                    }
                };
                logger.info("Starting Crnk");
                EntityManagerFactory emf = registry.require(EntityManagerFactory.class);
                EntityManager em = registry.require(EntityManager.class);
                JpaModule jpaModule = JpaModule.newServerModule(emf, em, transactionRunner);
                logger.info("{}", em.toString());

                jpaModule.setRepositoryFactory(new DefaultJpaRepositoryFactory());
                boot.addModule(jpaModule);

                boot.boot();
            });
        }

        String route = path.orElse("/api");
        env.router().use(route + "/**", (req, rsp) -> {
            logger.info("Got into: " + req.path());
            CrnkBoot b = req.require(CrnkBoot.class);
            JoobyRequestContext context = new JoobyRequestContext(req, rsp, route);
            RequestDispatcher requestDispatcher = boot.getRequestDispatcher();
            requestDispatcher.process(context);
        });

        binder.bind(CrnkBoot.class).toInstance(boot);
    }
}
