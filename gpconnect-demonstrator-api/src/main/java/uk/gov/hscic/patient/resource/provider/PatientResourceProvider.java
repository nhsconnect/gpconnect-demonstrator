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
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.search.AllergySearch;
import uk.gov.hscic.patient.allergies.search.AllergySearchFactory;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearch;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearchFactory;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;
import uk.gov.hscic.patient.encounters.search.EncounterSearch;
import uk.gov.hscic.patient.encounters.search.EncounterSearchFactory;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearch;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearchFactory;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.problems.search.ProblemSearchFactory;

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
            coding.setSystem("http://snomed.info/sct");
            coding.setCode("425173008");
            coding.setDisplay("record extract (record artifact)");
            CodeableConceptDt codableConcept = new CodeableConceptDt();
            codableConcept.addCoding(coding);
            codableConcept.setText("record extract (record artifact)");
            careRecordComposition.setType(codableConcept);
            
            CodingDt classCoding = new CodingDt();
            classCoding.setSystem("http://snomed.info/sct");
            classCoding.setCode("700232004");
            classCoding.setDisplay("general medical service (qualifier value)");
            CodeableConceptDt classCodableConcept = new CodeableConceptDt();
            classCodableConcept.addCoding(classCoding);
            classCodableConcept.setText("general medical service (qualifier value)");
            careRecordComposition.setClassElement(classCodableConcept);
            
            
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
                                    summaryCodableConcept.addCoding(summaryCoding);
                                    summaryCodableConcept.setText(patientSummaryList.get(0).getProvider());
                                    section.setCode(summaryCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(patientSummaryList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                        
                        case "Problems" :
                            ProblemSearch problemSearch = applicationContext.getBean(ProblemSearchFactory.class).select(sourceType);
                                List<ProblemListHTML> problemList = problemSearch.findAllProblemHTMLTables(nhsNumber);
                                if(problemList != null && problemList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Problems");
                                    CodingDt problemCoding = new CodingDt();
                                    problemCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    problemCoding.setCode("PRB");
                                    problemCoding.setDisplay("Problems");
                                    CodeableConceptDt problemCodableConcept = new CodeableConceptDt();
                                    problemCodableConcept.addCoding(problemCoding);
                                    problemCodableConcept.setText(problemList.get(0).getProvider());
                                    section.setCode(problemCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(problemList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Encounters" :
                            EncounterSearch encounterSearch = applicationContext.getBean(EncounterSearchFactory.class).select(sourceType);
                                List<EncounterListHTML> encounterList = encounterSearch.findAllEncounterHTMLTables(nhsNumber);
                                if(encounterList != null && encounterList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Encounters");
                                    CodingDt encounterCoding = new CodingDt();
                                    encounterCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    encounterCoding.setCode("ENC");
                                    encounterCoding.setDisplay("Encounters");
                                    CodeableConceptDt encounterCodableConcept = new CodeableConceptDt();
                                    encounterCodableConcept.addCoding(encounterCoding);
                                    encounterCodableConcept.setText(encounterList.get(0).getProvider());
                                    section.setCode(encounterCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(encounterList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Allergies and Sensitivities" :
                            AllergySearch allergySearch = applicationContext.getBean(AllergySearchFactory.class).select(sourceType);
                                List<AllergyListHTML> allergyList = allergySearch.findAllAllergyHTMLTables(nhsNumber);
                                if(allergyList != null && allergyList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Allergies and Sensitivities");
                                    CodingDt allergyCoding = new CodingDt();
                                    allergyCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    allergyCoding.setCode("ALL");
                                    allergyCoding.setDisplay("Allergies and Sensitivities");
                                    CodeableConceptDt allergyCodableConcept = new CodeableConceptDt();
                                    allergyCodableConcept.addCoding(allergyCoding);
                                    allergyCodableConcept.setText(allergyList.get(0).getProvider());
                                    section.setCode(allergyCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(allergyList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Clinical Items" : //(Clinical Items)
                            ClinicalItemSearch clinicalItemsSearch = applicationContext.getBean(ClinicalItemSearchFactory.class).select(sourceType);
                                List<ClinicalItemListHTML> clinicalItemList = clinicalItemsSearch.findAllClinicalItemHTMLTables(nhsNumber);
                                if(clinicalItemList != null && clinicalItemList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Clinical Items");
                                    CodingDt clinicalItemCoding = new CodingDt();
                                    clinicalItemCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    clinicalItemCoding.setCode("CLI");
                                    clinicalItemCoding.setDisplay("Clinical Items");
                                    CodeableConceptDt clinicalItemCodableConcept = new CodeableConceptDt();
                                    clinicalItemCodableConcept.addCoding(clinicalItemCoding);
                                    clinicalItemCodableConcept.setText(clinicalItemList.get(0).getProvider());
                                    section.setCode(clinicalItemCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(clinicalItemList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                        case "Medications" :    //(Medications)
                            break;
                        case "Referrals" :  //(Referrals)
                            break;
                        case "Observations" :   //(Observations)
                            break;
                        case "Investigations" : //(Investigations)
                            break;
                        case "Immunisations" :  // (Immunisations)
                            break;
                        case "Administrative Items" :   //(Administrative Items)
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
