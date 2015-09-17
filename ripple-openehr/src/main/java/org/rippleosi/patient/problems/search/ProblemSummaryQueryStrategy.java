package org.rippleosi.patient.problems.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemSummary;

/**
 */
public class ProblemSummaryQueryStrategy extends AbstractListQueryStrategy<ProblemSummary> {

    ProblemSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a_a/items/data[at0001]/items[at0002]/value/value as problem, " +
                "a_a/items/data[at0001]/items[at0003]/value/value as onset_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.problems_issues_rcp.v1] " +
                "where a/name/value='Problem list'";
    }

    @Override
    public List<ProblemSummary> transform(List<Map<String, Object>> resultSet) {

        List<ProblemSummary> problemList = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {

            Date dateOfOnset = DateFormatter.toDate(MapUtils.getString(data, "onset_date"));

            ProblemSummary problem = new ProblemSummary();
            problem.setSource("openehr");
            problem.setSourceId(MapUtils.getString(data, "uid"));
            problem.setProblem(MapUtils.getString(data, "problem"));
            problem.setDateOfOnset(dateOfOnset);

            problemList.add(problem);
        }

        return problemList;
    }
}
