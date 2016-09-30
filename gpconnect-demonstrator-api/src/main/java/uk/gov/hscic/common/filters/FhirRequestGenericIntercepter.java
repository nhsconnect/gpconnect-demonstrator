package uk.gov.hscic.common.filters;

import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FhirRequestGenericIntercepter extends InterceptorAdapter {

    @Value("${gp.connect.spineproxyconf.path}")
    private String spineroxyConfigPath;
    
    @Value("${gp.connect.interactionwhitelist.path}")
    private String interactionWhiteListPath;
    
    @Value("${gp.connect.ErrorSimulation.path}")
    private String errorSimulationPath;
    
    Logger log = Logger.getLogger(FhirRequestGenericIntercepter.class);

    private String systemSspToHeader = null;
    private HashSet interactionIdWhiteList = null;
    private List<String> errorSimulationCodes = null;
    private int simulationErrorIndex = 0;

    @PostConstruct
    public void postConstruct() {

        // Load config file
        try {
            String configJson = new String(Files.readAllBytes(Paths.get(spineroxyConfigPath)));
            JSONObject configJSonObj = new JSONObject(configJson);
            systemSspToHeader = configJSonObj.getString("toASID");
        } catch (Exception e) {
            log.error("Error loading SSP header values from file: " + e.getMessage());
        }

        // Load interactionId white list
        try {
            String whiteListJson = new String(Files.readAllBytes(Paths.get(interactionWhiteListPath)));
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
        
        // Load Error File if exists
        try {
            String errorSimulationJson = new String(Files.readAllBytes(Paths.get(errorSimulationPath)));
            JSONObject errorSimulationJSonObj = new JSONObject(errorSimulationJson);
            JSONArray errorsJSONArray = (JSONArray) errorSimulationJSonObj.get("errors");
            if (errorsJSONArray.length() > 0) {
                errorSimulationCodes = new ArrayList<>();
                for (int index = 0; index < errorsJSONArray.length(); index++) {
                    errorSimulationCodes.add(errorsJSONArray.getJSONObject(index).getString("errorCode"));
                }
            }

        } catch (Exception e) {
            log.error("Error loading error simulation file: " + e.getMessage());
        }
        
    }

    @Override
    public boolean incomingRequestPreProcessed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        
        String failedMsg = "";
        
        // Check there is a SSP-From header
        String fromASIDHeader = httpRequest.getHeader("SSP-From");
        if (fromASIDHeader == null || fromASIDHeader.isEmpty()) {
            failedMsg = failedMsg + "SSP-From header blank,";
        }

        // Check the SSP-To header is present and directed to our system
        String toASIDHeader = httpRequest.getHeader("SSP-To");
        if (toASIDHeader == null || toASIDHeader.isEmpty()) {
            failedMsg = failedMsg + "SSP-To header blank,";
        }
        if (systemSspToHeader != null && !toASIDHeader.equalsIgnoreCase(systemSspToHeader)) {
            // We loaded our ASID but the SSP-To header does not match the value
            failedMsg = failedMsg + "SSP-To header does not match ASID of system,";
        }

        // Check the interactionID header is valid on our white list of interaction IDs
        String interactionIdHeader = httpRequest.getHeader("SSP-InteractionID");
        if(interactionIdWhiteList != null && !interactionIdWhiteList.contains(interactionIdHeader)){
            // We managed to load our whilte list but the interaction Id in the header does not exist in the list
            failedMsg = failedMsg + "SSP-InteractionID in not a valid interaction ID for the system,";
        }
        
        if(!failedMsg.isEmpty()){
            try{
                httpResponse.sendError(400, failedMsg);
            } catch (Exception e){
                log.error("Error adding response message for Failed validation: ("+failedMsg+") " + e.getMessage());
            }
            return false;
        }
        
        
        // Mock Spine Error test component
        if(errorSimulationCodes != null){
            if(simulationErrorIndex >= 0 && simulationErrorIndex < errorSimulationCodes.size()){
                try{
                    httpResponse.sendError(Integer.parseInt(errorSimulationCodes.get(simulationErrorIndex)));
                    simulationErrorIndex++;
                    if(simulationErrorIndex == errorSimulationCodes.size()) simulationErrorIndex = 0;
                    return false;
                } catch (Exception e){
                    log.error(e);
                }
            }
        }

        return true;
    }

}
