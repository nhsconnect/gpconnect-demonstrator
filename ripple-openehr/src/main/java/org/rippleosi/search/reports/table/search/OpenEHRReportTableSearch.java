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

package org.rippleosi.search.reports.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractC4HReportingService;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRReportTableSearch extends AbstractC4HReportingService implements ReportTableSearch {

    @Override
    public List<String> findAllPatientsByQuery(ReportTableQuery tableQuery) {
        ReportTableQueryStrategy queryStrategy = new ReportTableQueryStrategy(tableQuery);
        return findTableData(queryStrategy, queryStrategy.getUriVariables());
    }
}
