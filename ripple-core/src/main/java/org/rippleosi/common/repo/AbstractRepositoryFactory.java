package org.rippleosi.common.repo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 */
public abstract class AbstractRepositoryFactory<R extends Repository> implements RepositoryFactory<R> {

    private final Map<String, R> repositories = new HashMap<>();

    protected abstract R defaultRepository();
    protected abstract Class<R> repositoryClass();

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public R select(String source) {

        R repository = selectSpecifiedRepository(source);

        if (repository == null) {
            repository = selectRepositoryByPriority();
        }

        if (repository == null) {
            return defaultRepository();
        }

        return repository;
    }

    private R selectSpecifiedRepository(String source) {
        return repositories.get(source);
    }

    private R selectRepositoryByPriority() {

        int currentPriority = Integer.MAX_VALUE;
        R selectedRepository = null;
        for (R repository : repositories.values()) {

            int priority = repository.getPriority();
            if (priority < currentPriority) {
                currentPriority = priority;
                selectedRepository = repository;
            }
        }

        return selectedRepository;
    }

    @PostConstruct
    public void postConstruct() {
        Map<String, R> beans = applicationContext.getBeansOfType(repositoryClass());

        for (R repository : beans.values()) {
            repositories.put(repository.getSource(), repository);
        }
    }
}
