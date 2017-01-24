package uk.gov.hscic.common.ldap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointResolver {
    private static final Logger LOG = Logger.getLogger("LDAPLog");

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

    @Value("${gp.connect.provider.routing.file:#{null}}")
    protected String providerRoutingFile;

    private Path providerRoutingFilePath;

    private KeyManagerFactory serverKeyManager = null;
    private TrustManagerFactory trustManager = null;

    @PostConstruct
    public void postConstruct() {
        if (providerRoutingFile != null) {
            providerRoutingFilePath = Paths.get(providerRoutingFile);
        }
    }

    @GetMapping("/ldap/endpointLookup")
    public String findEndpointFromODSCode(
            @RequestParam(value = "odsCode", required = true) String odsCode,
            @RequestParam(value = "interactionId", required = true) String interactionId) throws Exception {
        String result = fileLookup(odsCode, interactionId);

        if (result == null) {
            LOG.warn(String.format("File-based endpoint lookup returned no results. Falling back on LDAP (url - %s port - %d)", ldapUrl, ldapPort));
            return ldapLookup(odsCode, interactionId);
        }

        return result;
    }

    private String fileLookup(String odsCode, String interactionId) throws Exception {
        String result = null;

        ArrayNode practicesNode = loadPractices();

        if (practicesNode != null) {
            for (int m = 0; m < practicesNode.size() && result == null; m++) {
                JsonNode practiceNode = practicesNode.get(m);

                if (odsCode.equals(practiceNode.get("odsCode").asText())) {
                    ArrayNode interactionIdsNode = (ArrayNode) practiceNode.get("interactionIds");

                    if (interactionIdsNode.size() > 0) {
                        for (int i = 0; i < interactionIdsNode.size() && result == null; i++) {
                            String interactionIdText = interactionIdsNode.get(i).asText();

                            if (interactionId.equals(interactionIdText) || "*".equals(interactionIdText)) {
                                result = format(practiceNode.get("endpointURL").asText(), practiceNode.get("ASID").asText());
                            }
                        }
                    }
                }
            }
        }

        if (result == null) {
            LOG.warn(String.format("Unable to match one or both of the given odsCode (%s) and interactionId (%s)", odsCode, interactionId));
        }

        return result;
    }

    private ArrayNode loadPractices() throws JsonParseException, JsonMappingException, IOException {
        if (Files.exists(providerRoutingFilePath)) {
            JsonNode rootNode = new ObjectMapper().readValue(Files.readAllBytes(providerRoutingFilePath), JsonNode.class);
            return (ArrayNode) rootNode.get("practices");
        }

        LOG.warn(String.format("The file %s does not exist", providerRoutingFilePath.toUri()));

        return null;
    }

    private String ldapLookup(String odsCode, String interactionId) throws Exception {
        String uuid = java.util.UUID.randomUUID().toString();

        LOG.info(uuid + " Endpoint Lookup - ODSCode:" + odsCode + " InteractionId:" + interactionId);

        String partyKey = null;
        String asid = "";
        String endpointURL = "";

        // Lookup the PartyKey for the Organization ODS Code
        String asidFilter = "(&(nhsIDCode=" + odsCode + ")(objectClass=nhsAS)(nhsAsSvcIA=" + interactionId + "))";

        for (Collection<Attribute> attributes : ldapQueryRequest("ou=services, o=nhs", asidFilter)) {
            for (Attribute attribute : attributes) {
                LOG.debug(uuid + " ASID Arribute - " + attribute.getId() + " : " + attribute.getString());
                // Extract PartyKey
                if ("nhsMhsPartyKey".equalsIgnoreCase(attribute.getId())) {
                    partyKey = attribute.getString();
                } else if ("uniqueIdentifier".equalsIgnoreCase(attribute.getId())) {
                    asid = attribute.getString();
                }
            }
        }

        // Lookup the GP Connect endpoint URL
        if (partyKey != null) {
            String mhsFilter = "(&(nhsMhsPartyKey=" + partyKey + ")(objectClass=nhsMhs)(nhsMhsSvcIA=" + interactionId + "))";

            for (Collection<Attribute> attributes : ldapQueryRequest("ou=services, o=nhs", mhsFilter)) {
                for (Attribute attribute : attributes) {
                    LOG.debug(uuid + " MHS Arribute - " + attribute.getId() + " : " + attribute.getString());

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

        return format(endpointURL, asid);
    }

    private List<Collection<Attribute>> ldapQueryRequest(String queryBase, String queryFilter) throws IOException {
        String uuid = java.util.UUID.randomUUID().toString();
        List<Collection<Attribute>> returnList = new ArrayList<>();
        LdapNetworkConnection connection = null;

        LOG.debug(uuid + " ldapSDSQuery (Base:" + queryBase + " Filter:" + queryFilter + ")");

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
                    LOG.debug(attribute.getId() + ":" + attribute.getString());
                }
            }

            connection.unBind();
        } catch (Exception e) {
            LOG.error(uuid + " Error - " + e.getMessage());
            if (connection != null) {
                connection.close();
            }
        }

        return returnList;
    }

    private String format(String endpointURL, String asid) {
        return "{ \"endpointURL\" : \"" + endpointURL + "\", \"recievingSysASID\" : \"" + asid + "\"}";
    }
}