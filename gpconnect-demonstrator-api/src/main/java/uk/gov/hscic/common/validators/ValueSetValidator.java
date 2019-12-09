package uk.gov.hscic.common.validators;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

@Component
public class ValueSetValidator {
    private static final Logger LOG = Logger.getLogger(ValueSetValidator.class);
    
    @Value("${fhirvaluesets.checkDisk}")
    private Boolean fhirValueSetsCheckDisk;
        
    @Value("${fhirvaluesets.checkWeb}")
    private Boolean fhirValueSetsCheckWeb;
            
    @Value("${fhirvaluesets.checkWebFirst}")
    private Boolean fhirValueSetsCheckWebFirst;
                
    @Value("${fhirvaluesets.path}")
    private String fhirValueSetsPath;
    
    @Value("${fhirvaluesets.url}")
    private String fhirValueSetsUrl;
        
    private Map<String, ValueSet> valueSetCache;
    
    private ValueSet loadValueSet(String systemUrl){
        ValueSet set;
        set = getValueSetFromCache(systemUrl);
        
        if(set == null){
            set = findValueSet(systemUrl);
            
            if(valueSetCache == null){
                valueSetCache = new HashMap<String, ValueSet>();
            }
            valueSetCache.put(systemUrl, set);
        }
        
        return set;
    }
    
    private ValueSet getValueSetFromCache(String systemUrl)
    {  
        if(valueSetCache == null){
            return null;
        }
        
        ValueSet set = valueSetCache.get(systemUrl);
         
        return set;
    }
    
    private ValueSet findValueSet(String systemUrl)
    {
        int valueSetNamePos = systemUrl.lastIndexOf("/") + 1;
        String valueSetFilename = String.format("%s.xml", systemUrl.substring(valueSetNamePos));
        ValueSet valSet = null;
        
        String xmlContent = null;
        
        if(fhirValueSetsCheckWebFirst == true){
            xmlContent = readValueSetFromWeb(valueSetFilename);
        }
        
        if(xmlContent == null){
            xmlContent = readValueSetFromDisk(valueSetFilename);
        }
        
        if(fhirValueSetsCheckWebFirst == false && xmlContent == null){
            xmlContent = readValueSetFromWeb(valueSetFilename);
        }
               
        if (xmlContent != null) {
            try {
                FhirContext fhirCtx = FhirContext.forDstu3();
                IParser parser = fhirCtx.newXmlParser();
                valSet =  parser.parseResource(ValueSet.class, xmlContent);

            } catch (DataFormatException ex) {
                LOG.error(String.format("Error parsing valueSetFilename: %s", valueSetFilename));
            }
        }
        
        if(valSet == null){
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(String.format("Could not find or parse Value Set [SystemUrl: %s] at: %s. See system log for details.", systemUrl, valueSetFilename)),
                    SystemCode.REFERENCE_NOT_FOUND, IssueType.INVALID); 
        }
        
        return valSet;
    }
    
    private String readValueSetFromDisk(String filename){
        
        String valSet = null;
        if(fhirValueSetsCheckDisk == false || fhirValueSetsPath == null){
            return valSet;
        }
        
        String filePathLocation = String.format("%sValueSet-%s", fhirValueSetsPath, filename);
        Path valueSetFilePath = new File(filePathLocation).toPath();
               
        if (valueSetFilePath.toFile().exists()) {
            try {

                valSet = new String(Files.readAllBytes(valueSetFilePath));

            } catch (IOException ex) {
                LOG.error(String.format("Error reading valueSetFilePath: %s. Message: %s", valueSetFilePath, ex.getMessage()));
            }
        }
        
        return valSet;
    }
    
    private String readValueSetFromWeb(String filename){
        
        String valSet = null;
        if(fhirValueSetsCheckWeb == false || fhirValueSetsUrl == null){
            return valSet;
        }
        
        String fileLocation = String.format("%s%s", fhirValueSetsUrl, filename);
        
        try (CloseableHttpResponse httpResponse = createCloseableHttpResponse(fileLocation);
             InputStreamReader reader = new InputStreamReader(httpResponse.getEntity().getContent());
             BufferedReader bufferedReader = new BufferedReader(reader)){
  
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            
            if(responseCode == HttpStatus.SC_OK){
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine())!= null){
                    stringBuilder.append(line);
                }

                valSet = stringBuilder.toString();
                
            }else{
                LOG.error(String.format("fileLocation unknown Error. Status Code: %d", responseCode));
            }
            
        } 
        catch(IOException ex) {
            LOG.error(String.format("fileLocation read Error: %s. Message: %s", fileLocation, ex.getMessage()));
        }
        
        return valSet;
    }
    
    private CloseableHttpResponse createCloseableHttpResponse(String location) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(location);
        return httpclient.execute(httpGet);
    }
    
    public Boolean validateCode(Coding code) {

        String systemUrl = code.getSystem();        
        ValueSet valSet =  loadValueSet(systemUrl);
        
        //Check Code System
        @SuppressWarnings("unused")
        ValueSetComposeComponent codeSys = valSet.getCompose();
        @SuppressWarnings("unused")
        List<ValueSet.ConceptReferenceComponent> concepts;
//
//        for (ValueSet.ConceptReferenceComponent concept : concepts) {
//            String codeEl = concept.getCode();
//            String displayEl = concept.getDisplay();
//            
//            if(codeEl.equals(code.getCode()) && displayEl.equals(code.getDisplay())){
//                return true;
//            }
//        }
        
        //Check Compose Includes
//        ValueSetComposeComponent compose = valSet.getCompose();
//        
//        List<ValueSet.ValueSetComposeComponent> includeConcepts = new ArrayList<>();
//        List<ConceptSetComponent> includes = compose.getInclude();
//        
//        for(ValueSet.ValueSetComposeComponent include : includes){
//            includeConcepts.addAll(include.getConcept());
//        }
//        
//        for (ValueSet.ComposeIncludeConcept includeConcept : includeConcepts) {
//            String incCodeEl = includeConcept.getCode();
//            String incDisplayEl = includeConcept.getDisplay();
//            
//            if(incCodeEl.equals(code.getCode()) && incDisplayEl.equals(code.getDisplay())){
//                return true;
//            }
//        }
        
        return true;
    }
        
}
