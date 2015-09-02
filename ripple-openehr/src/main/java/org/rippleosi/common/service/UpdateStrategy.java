package org.rippleosi.common.service;

/**
 */
public interface UpdateStrategy<T> extends CreateStrategy<T> {

    String getCompositionId();
}
