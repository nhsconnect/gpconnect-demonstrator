package org.rippleosi.common.service;

import org.springframework.http.ResponseEntity;

/**
 */
public interface RequestProxy {

    <T> ResponseEntity<T> getWithSession(String uri, Class<T> cls);

    <T> ResponseEntity<T> postWithSession(String uri, Class<T> cls, Object body);

    <T> ResponseEntity<T> putWithSession(String uri, Class<T> cls, Object body);
}
