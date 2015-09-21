package org.rippleosi.patient.referral.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.exception.InvalidDataException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.referral.model.ReferralDetails;

/**
 */
public class ReferralDetailsQueryStrategy extends AbstractQueryStrategy<ReferralDetails> {

    private final String referralId;

    ReferralDetailsQueryStrategy(String patientId, String referralId) {
        super(patientId);
        this.referralId = referralId;
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_submitted, " +
                "a_a/items/activities/timing/value as referral_date, " +
                "a_a/items/protocol/items/items/value/value as referral_from, " +
                "a_a/items/activities/description/items[at0121]/value/value as referral_to, " +
                "a_a/items/activities/description/items[at0062]/value/value as referral_reason, " +
                "a_a/items/activities/description/items[at0064]/value/value as clinical_summary " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
                "contains SECTION a_a[openEHR-EHR-SECTION.referral_details_rcp.v1] " +
                "where a/name/value='Referral' " +
                "and a/uid/value='" + referralId + "' ";
    }

    @Override
    public ReferralDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        if (resultSet.size() > 1) {
            throw new InvalidDataException("Too many results found");
        }

        Map<String,Object> data = resultSet.get(0);

        return new ReferralDetailsTransformer().transform(data);
    }
}
