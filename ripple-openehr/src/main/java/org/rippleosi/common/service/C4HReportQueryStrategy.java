package org.rippleosi.common.service;

import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.springframework.web.util.UriComponents;

public interface C4HReportQueryStrategy<T> {

    UriComponents getQueryUriComponents();

    T transform(ReportGraphResults resultSet);
}
