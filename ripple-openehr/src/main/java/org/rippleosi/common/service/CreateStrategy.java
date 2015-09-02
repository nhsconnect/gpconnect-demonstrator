package org.rippleosi.common.service;

import java.util.Map;

/**
 */
public interface CreateStrategy<T> {

    String getPatientId();

    String getTemplate();

    Map<String,String> getContent();
}
