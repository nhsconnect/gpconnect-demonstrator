package org.rippleosi.common.repo;

/**
 */
@FunctionalInterface
public interface RepositoryFactory<R> {
    R select(String source);
}
