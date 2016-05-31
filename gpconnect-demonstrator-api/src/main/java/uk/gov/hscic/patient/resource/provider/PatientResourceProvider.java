package uk.gov.hscic.patient.resource.provider;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearch;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearchFactory;

public class PatientResourceProvider implements IResourceProvider {
    
    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }
    
    ApplicationContext applicationContext;
    
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
            
    @Operation(name="$getcarerecord")
    public Bundle getPatientCareRecord(@ResourceParam Parameters params){
                
        String nhsNumber = null;
        ArrayList<String> sectionsParamList = new ArrayList();
        
        // Extract the parameters
        for(Parameter param : params.getParameter()){
            
            String paramName = param.getName();
            IDatatype value = param.getValue();
            
            if(value instanceof IdentifierDt){
                nhsNumber = ((IdentifierDt)value).getValue();
            }else if(value instanceof StringDt){
                sectionsParamList.add(((StringDt)value).getValue());
            }
        }
        
        // Build Bundle
        Bundle bundle = new Bundle();
        bundle.setType(BundleTypeEnum.SEARCH_RESULTS);
        
        // Validate request fields
        if(nhsNumber == null || nhsNumber.isEmpty()){
            // Build the OperationOutcome response
            
            
        } else {
            
            // Build the Patient Resource and add it to the bundle
            bundle.addEntry(buildPatientEntry(nhsNumber));
            
            //Build the Care Record Composition
            Entry careRecordEntry = new Entry();
            Composition careRecordComposition = new Composition();
                        
            // Set Composition Mandatory Fields
            careRecordComposition.setDate(new DateTimeDt(Calendar.getInstance().getTime()));
            CodingDt coding = new CodingDt();
            coding.setSystem("http://fhir.nhs.net/ValueSet/document-type-codes-1-0");
            coding.setCode("CAR");
            coding.setDisplay("Care Record");
            CodeableConceptDt codableConcept = new CodeableConceptDt();
            codableConcept.addCoding(coding);
            codableConcept.setText("Care Record");
            careRecordComposition.setType(codableConcept);
            careRecordComposition.setTitle("Patient Care Record");
            careRecordComposition.setStatus(CompositionStatusEnum.FINAL);
            careRecordComposition.setSubject(new ResourceReferenceDt());
            careRecordComposition.setAuthor(Collections.singletonList(new ResourceReferenceDt()));
            
            
            // Build requested sections
            if(sectionsParamList.size() > 0){
                ArrayList<Section> sectionsList = new ArrayList();
                
                RepoSource sourceType = RepoSourceType.fromString(null);
                
                for(String sectionName : sectionsParamList){
                    switch(sectionName){
                        case "Summary" :
                                PatientSummarySearch patientSummarySearch = applicationContext.getBean(PatientSummarySearchFactory.class).select(sourceType);
                                List<PatientSummaryListHTML> patientSummaryList = patientSummarySearch.findAllPatientSummaryHTMLTables(nhsNumber);
                                if(patientSummaryList != null && patientSummaryList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Summary");
                                    CodingDt summaryCoding = new CodingDt();
                                    summaryCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    summaryCoding.setCode("SUM");
                                    summaryCoding.setDisplay("Summary");
                                    CodeableConceptDt summaryCodableConcept = new CodeableConceptDt();
                                    summaryCodableConcept.addCoding(coding);
                                    summaryCodableConcept.setText(patientSummaryList.get(0).getProvider());
                                    section.setCode(summaryCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(patientSummaryList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                        case "Diagnosis" :
                            break;
                        case "Events" :
                            break;
                        case "Immunisations" :
                            break;
                        case "Investigations" :
                            break;
                        case "Medications" :
                            break;
                        case "Observations" :
                            break;
                        case "Problems" :
                            break;
                        case "Procedures" :
                            break;
                        case "Risks and Warnings" :
                            break;
                    }
                }
                
                careRecordComposition.setSection(sectionsList);
            }
            
            careRecordEntry.setResource(careRecordComposition);
            bundle.addEntry(careRecordEntry);
            
        }
        
        return bundle;
    }
    
    public Entry buildPatientEntry(String nhsNumber){
            
            // Build the Patient Resource in the response
            Entry patientEntry = new Entry();    
            
            Patient patient = new Patient();
            patient.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", nhsNumber));
            
            patientEntry.setResource(patient);
            return patientEntry;
    }
}
