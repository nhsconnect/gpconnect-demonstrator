package org.rippleosi.patient.problems.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.problems.model.ProblemHeadline;

/**
 */
public class ProblemHeadlineQueryStrategy extends AbstractListQueryStrategy<ProblemHeadline> {

    ProblemHeadlineQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a_a/items/data[at0001]/items[at0002]/value/value as problem " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.problems_issues_rcp.v1] " +
                "where a/name/value='Problem list'";
    }

    @Override
    public List<ProblemHeadline> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new ProblemHeadlineTransformer(), new ArrayList<>());
    }
}
