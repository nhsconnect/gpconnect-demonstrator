/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package org.rippleosi.search.reports.graph.search;

import org.apache.commons.lang3.StringUtils;
import org.rippleosi.common.service.C4HUriQueryStrategy;
import org.rippleosi.search.reports.graph.model.ReportGraphQuery;
import org.rippleosi.search.reports.graph.model.ReportGraphResults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ReportGraphQueryStrategy implements C4HUriQueryStrategy<ReportGraphResults, ReportGraphResults> {

    @Value("${c4hOpenEHR.address}")
    private String c4hOpenEHRAddress;

    private ReportGraphQuery graphQuery;

    public void setGraphQuery(ReportGraphQuery graphQuery) {
        this.graphQuery = graphQuery;
    }

    @Override
    public UriComponents getQueryUriComponents() {
        // TODO - use graphQuery and remove hard coded ehrID, and introduce template lookup
        return UriComponentsBuilder
            .fromHttpUrl(c4hOpenEHRAddress + "/view")
            .path("/4ee4bad9-2f9e-4e33-b1d6-6572709cabee" + "/ProblemAgeBands")
            .queryParam("targetCompositions", "Problem list")
            .queryParam("targetTextValues", StringUtils.strip(graphQuery.getSearchString()))
            .build();
    }

    @Override
    public String getRequestBody() {
        return null;
    }

    @Override
    public ReportGraphResults transform(ReportGraphResults resultSet) {
        resultSet.setSource("c4hOpenEHR");
        return resultSet;
    }
}
