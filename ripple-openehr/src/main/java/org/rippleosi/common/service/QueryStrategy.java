package org.rippleosi.common.service;

import java.util.List;
import java.util.Map;

/**
 */
public interface QueryStrategy<T> {

    String getPatientId();

    String getQuery(String ehrId);

    T transform(List<Map<String, Object>> resultSet);
}
