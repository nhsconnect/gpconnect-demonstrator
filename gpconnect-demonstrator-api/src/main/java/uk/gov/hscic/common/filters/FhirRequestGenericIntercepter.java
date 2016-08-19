package uk.gov.hscic.common.filters;

import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class FhirRequestGenericIntercepter extends InterceptorAdapter {

    Logger log = Logger.getLogger(FhirRequestGenericIntercepter.class);

    private String systemSspToHeader = null;
    private HashSet interactionIdWhiteList = null;

    public FhirRequestGenericIntercepter() {
        super();

        // Load config file
        try {
            String configJson = new String(Files.readAllBytes(Paths.get("gpc/gpconnect-demonstrator-api/spineproxy.json")));
            JSONObject configJSonObj = new JSONObject(configJson);
            systemSspToHeader = configJSonObj.getString("toASID");
        } catch (Exception e) {
            log.error("Error loading SSP header values from file: " + e.getMessage());
        }

        // Load interactionId white list
        try {
            String whiteListJson = new String(Files.readAllBytes(Paths.get("gpc/gpconnect-demonstrator-api/interactionIdWhiteList.json")));
            JSONObject whiteListJSonObj = new JSONObject(whiteListJson);
            JSONArray whiteListJSONArray = (JSONArray) whiteListJSonObj.get("interactionIds");
            if (whiteListJSONArray.length() > 0) {
                interactionIdWhiteList = new HashSet();
                for (int index = 0; index < whiteListJSONArray.length(); index++) {
                    interactionIdWhiteList.add(whiteListJSONArray.getJSONObject(index).getString("interactionId"));
                }
            }

        } catch (Exception e) {
            log.error("Error loading interactionID White List from file: " + e.getMessage());
        }
    }

    @Override
    public boolean incomingRequestPreProcessed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        
        // Check there is a SSP-From header
        String fromASIDHeader = httpRequest.getHeader("SSP-From");
        if (fromASIDHeader == null || fromASIDHeader.isEmpty()) {
            // SSP-From header does not exist in request
            return false;
        }

        // Check the SSP-To header is present and directed to our system
        String toASIDHeader = httpRequest.getHeader("SSP-To");
        if (toASIDHeader == null || toASIDHeader.isEmpty()) {
            // SSP-To header does not exist in request
            return false;
        }
        if (systemSspToHeader != null && !toASIDHeader.equalsIgnoreCase(systemSspToHeader)) {
            // We loaded our ASID but the SSP-To header does not match the value
            return false;
        }

        // Check the interactionID header is valid on our white list of interaction IDs
        String interactionIdHeader = httpRequest.getHeader("SSP-InteractionID");
        if(interactionIdWhiteList != null && !interactionIdWhiteList.contains(interactionIdHeader)){
            // We managed to load our whilte list but the interaction Id in the header does not exist in the list
            try{
                httpResponse.sendError(400, "Invalid interactionId");
            } catch (Exception e){
                log.error("Error adding response message for Failed InteractionID validation: " + e.getMessage());
            }
            return false;
        }

        return true;
    }

}
