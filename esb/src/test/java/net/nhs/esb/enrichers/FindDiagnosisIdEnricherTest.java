package net.nhs.esb.enrichers;

import static org.junit.Assert.*;
import net.nhs.esb.rest.domain.EhrDiagnosisResponse;
import net.nhs.esb.rest.domain.Meta;

import org.junit.Test;

public class FindDiagnosisIdEnricherTest {
	
	@Test
	public void testEnrich() {
		Meta meta = new Meta();
		meta.setHref("https://rest.ehrscape.com/rest/v1/composition/db7339d6-0b02-4810-a591-f775bb7e9c5d::handi.ehrscape.com::1");
		
		EhrDiagnosisResponse ehrDiagnosisResponse = new EhrDiagnosisResponse();
		ehrDiagnosisResponse.setMeta(meta);
		
		FindDiagnosisIdEnricher findDiagnosisIdEnricher = new FindDiagnosisIdEnricher();
		assertEquals("db7339d6-0b02-4810-a591-f775bb7e9c5d::handi.ehrscape.com::1", findDiagnosisIdEnricher.enrich(ehrDiagnosisResponse));
		
	}
}
