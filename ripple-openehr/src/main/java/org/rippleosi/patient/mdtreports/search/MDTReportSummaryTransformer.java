/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.mdtreports.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;

/**
 */
public class MDTReportSummaryTransformer implements Transformer<Map<String, Object>, MDTReportSummary> {

    @Override
    public MDTReportSummary transform(Map<String, Object> input) {

        Date dateOfRequest = DateFormatter.toDate(MapUtils.getString(input, "date_created"));
        Date dateOfMeeting = DateFormatter.toDateOnly(MapUtils.getString(input, "meeting_date"));

        MDTReportSummary mdtReport = new MDTReportSummary();
        mdtReport.setSource("Marand");
        mdtReport.setSourceId(MapUtils.getString(input, "uid"));
        mdtReport.setServiceTeam(MapUtils.getString(input, "service_team"));
        mdtReport.setDateOfRequest(dateOfRequest);
        mdtReport.setDateOfMeeting(dateOfMeeting);

        return mdtReport;
    }
}
