package uk.gov.hscic.tests;

import java.security.cert.X509Certificate;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.x509;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hscic.IntegrationTest;
import uk.gov.hscic.auth.KeyStoreFactory;

public class SignedHandlerIT extends IntegrationTest {
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void validCertificateEndpointLookupTest() throws Exception {
        X509Certificate x = (X509Certificate) KeyStoreFactory
                .getKeyStore(KEYSTORE_PATH + "client", PASSWORD)
                .getCertificate("nhsdigitalclient");

        assertNotNull(x);

        mockMvc.perform(get("/ldap/endpointLookup")
                .secure(true)
                .param("odsCode", "GPC001")
                .param("interactionId", "interactionId_A")
                .with(x509(x)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void invalidCertificateEndpointLookupTest() throws Exception {
        X509Certificate x = (X509Certificate) KeyStoreFactory
                .getKeyStore(KEYSTORE_PATH + "invalidClient", PASSWORD)
                .getCertificate("nhsdigitalclientinvalid");

        assertNotNull(x);

        mockMvc.perform(get("/ldap/endpointLookup")
                .secure(true)
                .param("odsCode", "GPC001")
                .param("interactionId", "interactionId_A")
                .with(x509(x)))
                .andDo(print())
                .andExpect(status().is(495));
    }

    @Test
    public void expiredCertificateEndpointLookupTest() throws Exception {
        X509Certificate x = (X509Certificate) KeyStoreFactory
                .getKeyStore(KEYSTORE_PATH + "expiredClient", PASSWORD)
                .getCertificate("nhsdigitalclientexpired");

        assertNotNull(x);

        mockMvc.perform(get("/ldap/endpointLookup")
                .secure(true)
                .param("odsCode", "GPC001")
                .param("interactionId", "interactionId_A")
                .with(x509(x)))
                .andDo(print())
                .andExpect(status().is(495));
    }

    @Test
    public void noCertificateEndpointLookupTest() throws Exception {
        mockMvc.perform(get("/ldap/endpointLookup")
                .secure(true)
                .param("odsCode", "GPC001")
                .param("interactionId", "interactionId_A"))
                .andDo(print())
                .andExpect(status().is(496));
    }
}
