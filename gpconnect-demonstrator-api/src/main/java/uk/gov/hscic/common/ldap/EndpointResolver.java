package uk.gov.hscic.common.ldap;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
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

    private KeyManagerFactory serverKeyManager = null;
    private TrustManagerFactory trustManager = null;

    @RequestMapping(value = "/endpointLookup", method = RequestMethod.GET)
    public String findEndpointFromODSCode(@RequestParam(value = "odsCode", required = true) String odsCode,
            @RequestParam(value = "interactionId", required = true) String interactionId) throws IOException, LdapInvalidAttributeValueException {

        String uuid = java.util.UUID.randomUUID().toString();

        ldapLog.info(uuid + " Endpoint Lookup - ODSCode:" + odsCode + " InteractionId:" + interactionId);

        String partyKey = null;
        String asid = "";
        String endpointURL = "";

        // Lookup the PartyKey for the Organization ODS Code
        String asidFilter = "(&(nhsIDCode=" + odsCode + ")(objectClass=nhsAS)(nhsAsSvcIA=" + interactionId + "))";
        ArrayList<Collection<Attribute>> asidResult = ldapQueryRequest("ou=services, o=nhs", asidFilter);

        for (Collection<Attribute> attributes : asidResult) {
            for (Attribute attribute : attributes) {
                ldapLog.debug(uuid + " ASID Arribute - " + attribute.getId() + " : " + attribute.getString());
                // Extract PartyKey
                if ("nhsMhsPartyKey".equalsIgnoreCase(attribute.getId())) {
                    partyKey = attribute.getString();   
                }
                else if ("uniqueIdentifier".equalsIgnoreCase(attribute.getId())) {
                    asid = attribute.getString();   
                }
            }
        }

        // Lookup the GP Connect endpoint URL
        if (partyKey != null) {
            String mhsFilter = "(&(nhsMhsPartyKey=" + partyKey + ")(objectClass=nhsMhs)(nhsMhsSvcIA=" + interactionId + "))";
            ArrayList<Collection<Attribute>> mhsResult = ldapQueryRequest("ou=services, o=nhs", mhsFilter);

            for (Collection<Attribute> attributes : mhsResult) {
                for (Attribute attribute : attributes) {
                    ldapLog.debug(uuid + " MHS Arribute - " + attribute.getId() + " : " + attribute.getString());
                    if ("nhsMhsEndPoint".equalsIgnoreCase(attribute.getId())) {
                        endpointURL = attribute.getString();
                        break;
                    }
                }
                if (!endpointURL.isEmpty()) {
                    break;
                }
            }
        }

        return "{ \"endpointURL\" : \"" + endpointURL + "\", \"recievingSysASID\" : \""  + asid + "\"}";
        
    }
    
    public ArrayList<Collection<Attribute>> ldapQueryRequest(@RequestParam(value = "queryBase", required = true) String queryBase,
            @RequestParam(value = "queryFilter", required = true) String queryFilter) throws IOException {

        String uuid = java.util.UUID.randomUUID().toString();
        ArrayList<Collection<Attribute>> returnList = new ArrayList();
        LdapNetworkConnection connection = null;

        ldapLog.debug(uuid + " ldapSDSQuery (Base:" + queryBase + " Filter:" + queryFilter + ")");

        try {
            connection = new LdapNetworkConnection(ldapUrl, ldapPort, ldapUseSSL);

            if (serverKeyManager == null && trustManager == null) {
                // Create Key Manager
                KeyStore serverKeys = KeyStore.getInstance(keystoreType);
                serverKeys.load(new FileInputStream(keystore), keystorePassword.toCharArray());
                serverKeyManager = KeyManagerFactory.getInstance("SunX509");
                serverKeyManager.init(serverKeys, keystorePassword.toCharArray());

                // Create New Trust Store
                KeyStore serverTrustStore = KeyStore.getInstance(keystoreType);
                serverTrustStore.load(new FileInputStream(keystore), keystorePassword.toCharArray());
                trustManager = TrustManagerFactory.getInstance("SunX509");
                trustManager.init(serverTrustStore);
            }

            // Set SSL Trust and Key stores in the config
            connection.getConfig().setKeyManagers(serverKeyManager.getKeyManagers());
            connection.getConfig().setTrustManagers(trustManager.getTrustManagers());

            connection.bind();

            EntryCursor cursor = connection.search(queryBase, queryFilter, SearchScope.SUBTREE);

            while (cursor.next()) {
                returnList.add(cursor.get().getAttributes());
                for (Attribute attribute : cursor.get().getAttributes()) {
                    ldapLog.debug(attribute.getId() + ":" + attribute.getString());
                }
            }

            connection.unBind();

        } catch (Exception e) {
            ldapLog.error(uuid + " Error - " + e.getMessage());
            if (connection != null) {
                connection.close();
            }
        }

        return returnList;
    }

}
