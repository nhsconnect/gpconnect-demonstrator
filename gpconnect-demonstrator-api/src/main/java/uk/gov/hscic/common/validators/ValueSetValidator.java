package uk.gov.hscic.common.validators;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

@Component
public class ValueSetValidator {
    private static final Logger LOG = Logger.getLogger(ValueSetValidator.class);
    
    @Value("${fhirvaluesets.path}")
    private String fhirValueSetsPath;
        
    private Map<String, ValueSet> valueSetCache;
    
    private ValueSet loadValueSet(String systemUrl){
        ValueSet set;
        set = getValueSet(systemUrl);
        
        if(set == null){
            set = findValueSet(systemUrl);
            
            if(valueSetCache == null){
                valueSetCache = new HashMap<String, ValueSet>();
            }
            valueSetCache.put(systemUrl, set);
        }
        
        return set;
    }
    
    private ValueSet getValueSet(String systemUrl)
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
        String valueSetFilename = MessageFormat.format("{0}{1}.xml", fhirValueSetsPath, systemUrl.substring(valueSetNamePos));
        Path valueSetFilePath = new File(valueSetFilename).toPath();
        ValueSet valSet = null;
               
        if (valueSetFilePath.toFile().exists()) {
            try {
                FhirContext fhirCtx = FhirContext.forDstu2();
                String xmlContent = new String(Files.readAllBytes(valueSetFilePath));
                IParser parser = fhirCtx.newXmlParser();
                valSet =  parser.parseResource(ValueSet.class, xmlContent);

            } catch (IOException ex) {
                LOG.error(MessageFormat.format("Error reading valueSetFilePath: {0}", valueSetFilename));
            } catch (DataFormatException ex) {
                LOG.error(MessageFormat.format("Error parsing valueSetFilePath: {0}", valueSetFilename));
            }
        }
        
        if(valSet == null){
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(MessageFormat.format("Could not find Value Set [SystemUrl: {0}] at: {1}", systemUrl, valueSetFilename)),
                    SystemCode.REFERENCE_NOT_FOUND, IssueTypeEnum.NOT_FOUND); 
        }
        
        return valSet;
    }
    
    public Boolean validateCode(CodingDt code) {

        String systemUrl = code.getSystem();        
        ValueSet valSet =  loadValueSet(systemUrl);
        
        ValueSet.CodeSystem codeSys = valSet.getCodeSystem();
        List<ValueSet.CodeSystemConcept> concepts = codeSys.getConcept();

        //Boolean match = concepts.stream()
        //                      //.map((codeSysConcept) -> codeSysConcept.getCodeElement())
        //                      .anyMatch((codeSysConcept) -> 
        //                            (codeSysConcept.getCodeElement().equals(code.getCodeElement()) &&
        //                             codeSysConcept.getDisplayElement().equals(code.getDisplayElement())));

        for (ValueSet.CodeSystemConcept concept : concepts) {
            String codeEl = concept.getCode();
            String displayEl = concept.getDisplay();
            
            if(codeEl.equals(code.getCode()) && displayEl.equals(code.getDisplay())){
                return true;
            }
        }
    
        
        return false;
    }
        
}
