package org.rippleosi.common.search;

/**
 */
@FunctionalInterface
public interface RepositoryFactory<R> {
    R select(String source);
}
