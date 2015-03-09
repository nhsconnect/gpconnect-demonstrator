package net.nhs.esb.enrichers;

import net.nhs.esb.openehr.model.ActionRestResponseData;
import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
public class FindDiagnosisIdEnricher {
    public String enrich(@Body ActionRestResponseData ehrDiagnosisResponse) {
        String result = null;
        if (ehrDiagnosisResponse != null && ehrDiagnosisResponse.getMeta() != null && ehrDiagnosisResponse.getMeta().getHref() != null) {
            String value = ehrDiagnosisResponse.getMeta().getHref();
            result = value.substring(value.lastIndexOf("/") + 1);
        }
        return result;
    }
}
