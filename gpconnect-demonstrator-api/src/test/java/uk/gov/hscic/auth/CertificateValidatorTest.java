package uk.gov.hscic.auth;

import java.security.cert.X509Certificate;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.x509;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import uk.gov.hscic.common.ldap.EndpointResolver;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
    "gp.connect.provider.routing.filename = providerRouting.json",
    "ldap.context.keystore = ldapKeystore.jks",
    "ldap.context.keystore.pwd = password",
    "ldap.context.keystore.type = JKS",
    "ldap.context.port = 10636",
    "ldap.context.url = localhost",
    "ldap.context.useSSL = false"
})
@WebMvcTest(EndpointResolver.class)
public class CertificateValidatorTest {
    protected static final String KEYSTORE_PATH = "src/test/resources/Authentication/";
    protected static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validCertificateEndpointLookupTest() throws Exception {
        X509Certificate x = (X509Certificate) KeyStoreFactory
                .getKeyStore(KEYSTORE_PATH + "client.jks", PASSWORD)
                .getCertificate("nhsdigitalclient");

        assertNotNull(x);

        mockMvc.perform(get("/api/ldap/endpointLookup")
                .secure(true)
                .param("odsCode", "GPC001")
                .param("interactionId", "interactionId_A")
                .with(x509(x)))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void invalidCertificateEndpointLookupTest() throws Exception {
//        X509Certificate x = (X509Certificate) KeyStoreFactory
//                .getKeyStore(KEYSTORE_PATH + "invalidClient.jks", PASSWORD)
//                .getCertificate("nhsdigitalclientinvalid");
//
//        assertNotNull(x);
//
//        mockMvc.perform(get("/ldap/endpointLookup")
//                .secure(true)
//                .param("odsCode", "GPC001")
//                .param("interactionId", "interactionId_A")
//                .with(x509(x)))
//                .andDo(print())
//                .andExpect(status().is(495));
//    }
//
//    @Test
//    public void expiredCertificateEndpointLookupTest() throws Exception {
//        X509Certificate x = (X509Certificate) KeyStoreFactory
//                .getKeyStore(KEYSTORE_PATH + "expiredClient.jks", PASSWORD)
//                .getCertificate("nhsdigitalclientexpired");
//
//        assertNotNull(x);
//
//        mockMvc.perform(get("/ldap/endpointLookup")
//                .secure(true)
//                .param("odsCode", "GPC001")
//                .param("interactionId", "interactionId_A")
//                .with(x509(x)))
//                .andDo(print())
//                .andExpect(status().is(495));
//    }
//
//    @Test
//    public void noCertificateEndpointLookupTest() throws Exception {
//        mockMvc.perform(get("/ldap/endpointLookup")
//                .secure(true)
//                .param("odsCode", "GPC001")
//                .param("interactionId", "interactionId_A"))
//                .andDo(print())
//                .andExpect(status().is(496));
//    }
}
