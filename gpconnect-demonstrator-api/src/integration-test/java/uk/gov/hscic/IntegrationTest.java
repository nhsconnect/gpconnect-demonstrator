package uk.gov.hscic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Kris Bloe
 */
@WebAppConfiguration
@TestPropertySource(properties = {
    "ldap.context.url = localhost",
    "ldap.context.port = 10636",
    "ldap.context.useSSL = false",
    "ldap.context.keystore = ldapKeystore.jks",
    "ldap.context.keystore.pwd = password",
    "ldap.context.keystore.type = JKS",
    "gp.connect.provider.routing.file = gpconnect-demonstrator-api/providerRouting.json"
})
@ContextConfiguration(classes = IntegrationTestConfig.class)
public abstract class IntegrationTest extends AbstractJUnit4SpringContextTests {
    protected static final String KEYSTORE_PATH = "src/integration-test/resources/authentication/";
    protected static final String PASSWORD = "password";

    @Autowired
    protected WebApplicationContext webApplicationContext;
}
