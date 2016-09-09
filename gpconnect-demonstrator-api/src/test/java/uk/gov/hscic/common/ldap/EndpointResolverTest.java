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
		
		URI externalSystemsUri = EndpointResolver.class.getResource("external.systems.json").toURI();
		endpointResolver.externalSystemsFile = Paths.get(externalSystemsUri).toString();
		endpointResolver.postConstruct();
	}

	@Test
	public void testFindEndpointFromODSCode() throws Exception {
		// known
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/A\", \"recievingSysASID\" : \"asid_A\"}", endpointResolver.findEndpointFromODSCode("odsCode_A", "interactionId_A"));
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/B\", \"recievingSysASID\" : \"asid_B\"}", endpointResolver.findEndpointFromODSCode("odsCode_B", "interactionId_B"));
	
		// unknown
		assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", "interactionId_C"));
	}

}
