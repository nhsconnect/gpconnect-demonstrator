package uk.gov.hscic.common.ldap;

import java.net.URI;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class EndpointResolverTest {
    private EndpointResolver endpointResolver;

    @Before
    public void setUp() throws Exception {
        endpointResolver = new EndpointResolver();

        URI externalSystemsUri = EndpointResolver.class.getResource("provider.routing.json").toURI();
        endpointResolver.providerRoutingFile = Paths.get(externalSystemsUri).toString();
        endpointResolver.postConstruct();
    }

    @Test
    public void testFindEndpointFromODSCode() throws Exception {
        // 1) known + wildcard interaction
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/A\", \"recievingSysASID\" : \"200000000359\"}", endpointResolver.findEndpointFromODSCode("GPC001", "interactionId_A"));

        // 2) multiple interaction IDs registered for same provider

        // 2a) known ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/B\", \"recievingSysASID\" : \"200000000361\"}", endpointResolver.findEndpointFromODSCode("R1A14", "urn:nhs:names:services:gpconnect:fhir:rest:update:appointment"));

        // 2b) known ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/B\", \"recievingSysASID\" : \"200000000361\"}", endpointResolver.findEndpointFromODSCode("R1A14", "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord"));

        // 2c) known ODS + unknown interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("R1A14", "urn:nhs:names:services:gpconnect:fhir:rest:create:order"));

        // 3) known ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/C\", \"recievingSysASID\" : \"200000000362\"}", endpointResolver.findEndpointFromODSCode("R3B46", "urn:nhs:names:services:gpconnect:fhir:rest:search:practitioner"));

        // 4) blank ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("", "urn:nhs:names:services:gpconnect:fhir:rest:search:practitioner"));

        // 5) known ODS + blank interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("R3B46", ""));

        // 6) known ODS + unknown interaction
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", "urn:nhs:names:services:gpconnect:fhir:rest:search:organization"));

        // 7) unknown ODS + wildcard interaction
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", "*"));
    }
}