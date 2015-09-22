package org.rippleosi.patient.procedures.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.procedures.model.ProcedureSummary;

/**
 */
public class ProcedureSummaryQueryStrategy extends AbstractListQueryStrategy<ProcedureSummary> {

    ProcedureSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a_a/description[at0001]/items[at0002]/value/value as procedure_name, " +
                "a_a/time/value as procedure_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains ACTION a_a[openEHR-EHR-ACTION.procedure.v1] " +
                "where a/name/value='Procedures list'";
    }

    @Override
    public List<ProcedureSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new ProcedureSummaryTransformer(), new ArrayList<>());
    }
}
