/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.common.repo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import uk.gov.hscic.common.types.RepoSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 */
public abstract class AbstractRepositoryFactory<R extends Repository> implements RepositoryFactory<R> {

    private final Map<RepoSource, R> repositories = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    protected abstract R defaultRepository();
    protected abstract Class<R> repositoryClass();

    @Override
    public R select(RepoSource source) {

        R repository = selectSpecifiedRepository(source);

        if (repository == null) {
            repository = selectRepositoryByPriority();
        }

        if (repository == null) {
            return defaultRepository();
        }

        return repository;
    }

    private R selectSpecifiedRepository(RepoSource source) {
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
