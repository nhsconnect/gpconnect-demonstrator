package org.rippleosi.patient.allergies.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.allergies.model.AllergyHeadline;

/**
 */
public class AllergyHeadlineQueryStrategy extends AbstractListQueryStrategy<AllergyHeadline> {

    AllergyHeadlineQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a_a/items/data[at0001]/items[at0002]/value/value as cause, " +
                "a_a/items/data[at0001]/items[at0002]/value/defining_code/terminology_id/value as cause_terminology, " +
                "a_a/items/data[at0001]/items[at0002]/value/defining_code/code_string as cause_code, " +
                "a_a/items/data[at0001]/items[at0025]/items[at0022]/value/value as reaction " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1] " +
                "where a/name/value='Allergies list' ";
    }

    @Override
    public List<AllergyHeadline> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new AllergyHeadlineTransformer(), new ArrayList<>());
    }
}
