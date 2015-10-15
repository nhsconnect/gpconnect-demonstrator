package org.rippleosi.common.service;

import org.springframework.web.util.UriComponents;

public interface C4HUriQueryStrategy<I, O> {

    UriComponents getQueryUriComponents();

    Object getRequestBody();

    O transform(I resultSet);
}
