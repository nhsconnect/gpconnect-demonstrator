package org.rippleosi.common.search;

/**
 */
public interface RepositoryFactory<R> {
    R select(String source);
}
