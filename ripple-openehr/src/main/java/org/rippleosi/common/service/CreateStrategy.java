package org.rippleosi.common.service;

import java.util.Map;

/**
 */
public interface CreateStrategy {

    String getPatientId();

    String getTemplate();

    Map<String,Object> getContent();
}
