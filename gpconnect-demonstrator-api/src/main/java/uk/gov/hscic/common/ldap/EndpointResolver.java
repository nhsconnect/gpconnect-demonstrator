package uk.gov.hscic.common.ldap;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Collection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ldap")
public class EndpointResolver {

    static Logger ldapLog = Logger.getLogger("LDAPLog");

    @Value("${ldap.context.url}")
    private String ldapUrl;

    @Value("${ldap.context.port}")
    private int ldapPort;
    
    @Value("${ldap.context.useSSL}")
    private boolean ldapUseSSL;
    
    @Value("${ldap.context.keystore}")
    private String keystore;
    
    @Value("${ldap.context.keystore.pwd}")
    private String keystorePassword;
    
    @Value("${ldap.context.keystore.type}")
    private String keystoreType;

    @RequestMapping(value = "/endpointLookup", method = RequestMethod.GET)
    public String findEndpointFromODSCode(@RequestParam(value = "odsCode", required = true) String odsCode,
            @RequestParam(value = "interactionId", required = true) String interactionId,
            @RequestParam(value = "pUrl", required = false) String pUrl,                                // Temp parameters to allow query configuration
            @RequestParam(value = "pPort", required = false) Integer pPort,                             // Temp parameters to allow query configuration
            @RequestParam(value = "pSSL", required = false) Boolean pSSL,                             // Temp parameters to allow query configuration
            @RequestParam(value = "pBase", required = false) String pBase,                              // Temp parameters to allow query configuration
            @RequestParam(value = "pFilter", required = false) String pFilter) throws IOException {     // Temp parameters to allow query configuration

        String uuid = java.util.UUID.randomUUID().toString();
        ldapLog.info(uuid + " Endpoint Lookup - ODSCode:" + odsCode + " InteractionId:" + interactionId);

        String partyKey = null;
        String endpointURL = "";

        LdapNetworkConnection connection = null;
        try {

            if (pUrl != null && pPort != null && pSSL != null) {
                connection = new LdapNetworkConnection(pUrl, pPort, pSSL);
            } else {
                connection = new LdapNetworkConnection(ldapUrl, ldapPort, ldapUseSSL);
            }

            // Create Key Manager
            KeyStore serverKeys = KeyStore.getInstance(keystoreType);
            serverKeys.load(new FileInputStream(keystore), keystorePassword.toCharArray());
            KeyManagerFactory serverKeyManager = KeyManagerFactory.getInstance("SunX509");
            serverKeyManager.init(serverKeys, keystorePassword.toCharArray());

            // Create New Trust Store
            KeyStore serverTrustStore = KeyStore.getInstance(keystoreType);
            serverTrustStore.load(new FileInputStream(keystore), keystorePassword.toCharArray());
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
            trustManager.init(serverTrustStore);
        
            // Set SSL Trust and Key stores in the config
            connection.getConfig().setKeyManagers(serverKeyManager.getKeyManagers());
            connection.getConfig().setTrustManagers(trustManager.getTrustManagers());

            connection.bind();

            if (pBase != null && pFilter != null) {
                EntryCursor asidCursor = connection.search(pBase, pFilter, SearchScope.SUBTREE);

                while (asidCursor.next()) {
                    Collection<Attribute> attributes = asidCursor.get().getAttributes();
                    for (Attribute attribute : attributes) {
                        ldapLog.info(uuid + " param Query Arribute - " + attribute.getId() + " : " + attribute.getString());
                    }
                }
            } else {

                // Lookup the PartyKey for the Organization ODS Code
                String asidFilter = "(&(nhsIDCode=" + odsCode + ")(objectClass=nhsAS)(nhsAsSvcIA=" + interactionId + "))";
                EntryCursor asidCursor = connection.search("ou=services, o=nhs", asidFilter, SearchScope.SUBTREE);

                while (asidCursor.next()) {
                    Collection<Attribute> attributes = asidCursor.get().getAttributes();
                    for (Attribute attribute : attributes) {
                        ldapLog.info(uuid + " ASID Arribute - " + attribute.getId() + " : " + attribute.getString());
                        // Extract PartyKey
                        if ("nhsMhsPartyKey".equalsIgnoreCase(attribute.getId())) {
                            partyKey = attribute.getString();
                        }
                    }
                }

                // Lookup the GP Connect endpoint URL
                if (partyKey != null) {
                    String mhsFilter = "(&(nhsMhsPartyKey=[partKey])(objectClass=nhsMhs)(nhsMhsSvcIA=" + interactionId + "))";
                    EntryCursor mhsCursor = connection.search("ou=services, o=nhs", mhsFilter, SearchScope.SUBTREE);

                    while (mhsCursor.next()) {
                        Collection<Attribute> attributes = mhsCursor.get().getAttributes();
                        for (Attribute attribute : attributes) {
                            ldapLog.info(uuid + " MHS Arribute - " + attribute.getId() + " : " + attribute.getString());
                            // Extract endpoint URL
                        }
                    }
                }
            }

            connection.unBind();

        } catch (Exception e) {
            ldapLog.info(uuid + " Error - " + e.getMessage());
            if (connection != null) {
                connection.close();
            }
        }

        return endpointURL;
    }

}
