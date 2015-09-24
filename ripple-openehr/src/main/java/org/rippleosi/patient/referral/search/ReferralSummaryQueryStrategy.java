package org.rippleosi.patient.referral.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public class ReferralSummaryQueryStrategy extends AbstractListQueryStrategy<ReferralSummary> {

    ReferralSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a_a/items/activities/timing/value as referral_date, " +
                "a_a/items/protocol/items/items/value/value as referral_from, " +
                "a_a/items/activities/description/items[at0121]/value/value as referral_to " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.referral_details_rcp.v1] " +
                "where a/name/value='Referral' ";
    }

    @Override
    public List<ReferralSummary> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new ReferralOnlyPredicate());

        return CollectionUtils.collect(filtered, new ReferralSummaryTransformer(), new ArrayList<>());
    }
}
