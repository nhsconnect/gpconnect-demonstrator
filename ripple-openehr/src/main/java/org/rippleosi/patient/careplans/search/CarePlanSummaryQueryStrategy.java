package org.rippleosi.patient.careplans.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.careplans.model.CarePlanSummary;

/**
 */
public class CarePlanSummaryQueryStrategy extends AbstractListQueryStrategy<CarePlanSummary> {

    CarePlanSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/context/start_time/value as date_created " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_plan.v1] " +
                "contains SECTION b_a[openEHR-EHR-SECTION.legal_information_rcp.v1] " +
                "where a/name/value='End of Life Patient Preferences'";
    }

    @Override
    public List<CarePlanSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new CarePlanSummaryTransformer(), new ArrayList<>());
    }
}
