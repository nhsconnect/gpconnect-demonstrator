package net.nhs.esb.cancermdt.route.converter;

import java.util.List;
import java.util.Map;
import net.nhs.esb.cancermdt.model.*;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class CancerMDTCompositionConverter extends BaseCompositionConverter<CancerMDT> {

    private static final String CANCERMDT_UID = "cancer_mdt_output_report/_uid";
    private static final String CANCERMDT_DEFINITION = "cancer_mdt_output_report/referral_details:";

    @Converter
    public CancerMDTComposition convertResponseToMedicationComposition(CompositionResponseData response) {

        Map<String, Object> rawComposition = response.getComposition();

        String compositionId = MapUtils.getString(rawComposition, CANCERMDT_UID);

        List<CancerMDT> cancerMDT = extractCompositionData(rawComposition);

        CancerMDTComposition cancerMDTComposition = new CancerMDTComposition();
        cancerMDTComposition.setCompositionId(compositionId);
        cancerMDTComposition.setCancerMDT(cancerMDT);

        return cancerMDTComposition;
    }

    @Override
    protected CancerMDT create(Map<String, Object> rawComposition, String prefix) {

        CancerMDT cancerMDT = new CancerMDT();

        String indexStr = prefix.substring(prefix.lastIndexOf(":") + 1);
        int index = Integer.valueOf(indexStr);

        // NOTE - As per meeting we are assuming that the will not be repeating groups at this time so only going to take first instance of the fields
        cancerMDT.setId((long)index);
        cancerMDT.setService(MapUtils.getString(rawComposition, "cancer_mdt_output_report/referral_details:" + index + "/original_referral/request:0/service_requested"));
        cancerMDT.setDate(MapUtils.getString(rawComposition,    "cancer_mdt_output_report/referral_details:" + index + "/mdt_referral/request:0/date_or_time_service_required"));
        cancerMDT.setNotes(MapUtils.getString(rawComposition,   "cancer_mdt_output_report/plan_and_requested_actions:" + index + "/recommendation:0/recommendation"));

        return cancerMDT;
    }

    @Override
    protected String dataDefinitionPrefix() {
        return CANCERMDT_DEFINITION;
    }
}
