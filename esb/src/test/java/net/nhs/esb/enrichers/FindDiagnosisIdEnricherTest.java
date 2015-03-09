package net.nhs.esb.enrichers;

import static org.junit.Assert.assertEquals;

import net.nhs.esb.openehr.model.ActionRestResponseData;
import net.nhs.esb.openehr.model.Meta;
import org.junit.Test;

public class FindDiagnosisIdEnricherTest {

    @Test
    public void testEnrich() {
        Meta meta = new Meta();
        meta.setHref("https://rest.ehrscape.com/rest/v1/composition/db7339d6-0b02-4810-a591-f775bb7e9c5d::handi.ehrscape.com::1");

        ActionRestResponseData ehrDiagnosisResponse = new ActionRestResponseData();
        ehrDiagnosisResponse.setMeta(meta);

        FindDiagnosisIdEnricher findDiagnosisIdEnricher = new FindDiagnosisIdEnricher();
        assertEquals("db7339d6-0b02-4810-a591-f775bb7e9c5d::handi.ehrscape.com::1", findDiagnosisIdEnricher.enrich(ehrDiagnosisResponse));

    }
}
