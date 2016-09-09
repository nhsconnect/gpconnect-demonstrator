package uk.gov.hscic.common.ldap;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class EndpointResolverTest {

	EndpointResolver endpointResolver;
	
	@Before
	public void setUp() throws Exception {
		endpointResolver = new EndpointResolver();
		
		URI externalSystemsUri = EndpointResolver.class.getResource("provider.routing.json").toURI();
		endpointResolver.providerRoutingFile = Paths.get(externalSystemsUri).toString();
		endpointResolver.postConstruct();
	}

	@Test
	public void testFindEndpointFromODSCode() throws Exception {
		// known + wildcard interaction
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/A\", \"recievingSysASID\" : \"200000000360\"}", endpointResolver.findEndpointFromODSCode("GPC001", "interactionId_A"));	
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/B\", \"recievingSysASID\" : \"200000000361\"}", endpointResolver.findEndpointFromODSCode("R1A14", "interactionId_B"));
	
		// known ODS + known interactionId
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/C\", \"recievingSysASID\" : \"200000000362\"}", endpointResolver.findEndpointFromODSCode("R3B46", "urn:nhs:names:services:gpconnect:fhir:rest:search:practitioner"));
		
		// known ODS + unknown interaction
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", "urn:nhs:names:services:gpconnect:fhir:rest:search:organization"));
		
		// unknown ODS + wildcard interaction
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", "*"));
	}

}
