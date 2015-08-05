package net.nhs.esb.referrals.route.converter;

import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.referrals.model.Referral;
import net.nhs.esb.referrals.model.ReferralUpdate;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.nhs.esb.util.DateFormatter.stripOddDate;
import static net.nhs.esb.util.DateFormatter.toDate;
import static org.apache.commons.collections.MapUtils.getString;


@Converter
@Component
public class PatientReferralsCompositionConverter {

    private static final String REFERRALS_PREFIX = "referral/referral_details:0/";
    private static final String REFERRALS_REQUEST = REFERRALS_PREFIX + "referral_request/";
    private static final String ORDER_REFERRAL = REFERRALS_PREFIX + "order_referral/";

    @Converter
    public Referral convertResponseToReferralsComposition(CompositionResponseData responseData) {
        // retrieve composition
        Map<String, Object> rawComposition = responseData.getComposition();

        // retrieve data
        String compositionId = getString(rawComposition, "referral/_uid");
        String referralFrom = getString(rawComposition, REFERRALS_REQUEST + "referral_from/unstructured_name");
        String referralTo = getString(rawComposition, REFERRALS_REQUEST + "request/referral_to");

        String rawDateOfReferral = getString(rawComposition, REFERRALS_REQUEST + "request/timing");
        rawDateOfReferral = stripOddDate(rawDateOfReferral);
        Date dateOfReferral = toDate(rawDateOfReferral);

        String reasonForReferral = getString(rawComposition, REFERRALS_REQUEST + "request/reason_for_referral");
        String clinicalSummary = getString(rawComposition, REFERRALS_REQUEST + "request/clinical_summary");
        String author = getString(rawComposition, "ctx/composer_name");
        String source = "openehr";

        String rawDateCreated = getString(rawComposition, REFERRALS_REQUEST + "request/timing");
        rawDateCreated = stripOddDate(rawDateCreated);
        Date dateCreated = toDate(rawDateCreated);

        // populate composition
        Referral referral = new Referral();
        referral.setCompositionId(compositionId);
        referral.setReferralFrom(referralFrom);
        referral.setReferralTo(referralTo);
        referral.setDateOfReferral(dateOfReferral);
        referral.setReasonForReferral(reasonForReferral);
        referral.setClinicalSummary(clinicalSummary);
        referral.setAuthor(author);
        referral.setDateCreated(dateCreated);
        referral.setSource(source);

        return referral;
    }

    @Converter
    public ReferralUpdate convertReferralsCompositionToUpdate(Referral referral) {
        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");
        content.put("ctx/composer_name", referral.getAuthor());

        String dateOfReferral = DateFormatter.toString(referral.getDateOfReferral());

        content.put(REFERRALS_REQUEST + "request/timing", dateOfReferral);
        content.put(REFERRALS_REQUEST + "request/referral_to", referral.getReferralTo());
        content.put(REFERRALS_REQUEST + "request/reason_for_referral", referral.getReasonForReferral());
        content.put(REFERRALS_REQUEST + "request/clinical_summary", referral.getClinicalSummary());
        content.put(REFERRALS_REQUEST + "request/timing", DateFormatter.toString(referral.getDateCreated()));
        content.put(REFERRALS_REQUEST + "referral_from/unstructured_name", referral.getReferralFrom());
        content.put(REFERRALS_REQUEST + "narrative", referral.getReasonForReferral());

        content.put(ORDER_REFERRAL + "ism_transition/current_state|code", "526");
        content.put(ORDER_REFERRAL + "ism_transition/current_state|value", "planned");
        content.put(ORDER_REFERRAL + "ism_transition/careflow_step|code", "at0002");
        content.put(ORDER_REFERRAL + "ism_transition/careflow_step|value", "Referral planned");
        content.put(ORDER_REFERRAL + "referral_to", referral.getReasonForReferral());
        content.put(ORDER_REFERRAL + "receiver/address:0/address_type|code", "at0013");
        content.put(ORDER_REFERRAL + "receiver/address:0/location", "Leeds Royal Infirmary");
        content.put(ORDER_REFERRAL + "time", dateOfReferral);

        return new ReferralUpdate(content);
    }
}
