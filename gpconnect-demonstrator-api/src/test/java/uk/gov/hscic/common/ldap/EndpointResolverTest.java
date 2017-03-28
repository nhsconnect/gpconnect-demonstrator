package uk.gov.hscic.common.ldap;

import java.net.URI;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hscic.InteractionId;

public class EndpointResolverTest {
    private EndpointResolver endpointResolver;

    @Before
    public void setUp() throws Exception {
        endpointResolver = new EndpointResolver();

        ReflectionTestUtils.setField(endpointResolver, "configPath", "");

        URI externalSystemsUri = EndpointResolver.class.getResource("provider.routing.json").toURI();
        endpointResolver.providerRoutingFilename = Paths.get(externalSystemsUri).toString();
        endpointResolver.postConstruct();
    }

    @Test
    public void testFindEndpointFromODSCode() throws Exception {
        // 1) known + wildcard interaction
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/A\", \"recievingSysASID\" : \"200000000359\"}", endpointResolver.findEndpointFromODSCode("GPC001", "interactionId_A"));

        // 2) multiple interaction IDs registered for same provider

        // 2a) known ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/B\", \"recievingSysASID\" : \"200000000361\"}", endpointResolver.findEndpointFromODSCode("R1A14", InteractionId.REST_UPDATE_APPOINTMENT));

        // 2b) known ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/B\", \"recievingSysASID\" : \"200000000361\"}", endpointResolver.findEndpointFromODSCode("R1A14", InteractionId.OPERATION_GPC_GET_CARE_RECORD));

        // 2c) known ODS + unknown interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("R1A14", InteractionId.REST_CREATE_ORDER));

        // 3) known ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"http://endpointUrl/C\", \"recievingSysASID\" : \"200000000362\"}", endpointResolver.findEndpointFromODSCode("R3B46", InteractionId.REST_SEARCH_PRACTITIONER));

        // 4) blank ODS + known interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("", InteractionId.REST_SEARCH_PRACTITIONER));

        // 5) known ODS + blank interactionId
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("R3B46", ""));

        // 6) known ODS + unknown interaction
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", InteractionId.REST_SEARCH_ORGANIZATION));

        // 7) unknown ODS + wildcard interaction
        assertEquals("Make sure that the result is as expected", "{ \"endpointURL\" : \"\", \"recievingSysASID\" : \"\"}", endpointResolver.findEndpointFromODSCode("odsCode_C", "*"));
    }
}