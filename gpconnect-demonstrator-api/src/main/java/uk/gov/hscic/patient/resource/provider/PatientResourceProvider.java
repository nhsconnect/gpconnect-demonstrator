package uk.gov.hscic.patient.resource.provider;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearchFactory;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.search.AllergySearch;
import uk.gov.hscic.patient.allergies.search.AllergySearchFactory;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearch;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearchFactory;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;
import uk.gov.hscic.patient.encounters.search.EncounterSearch;
import uk.gov.hscic.patient.encounters.search.EncounterSearchFactory;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;
import uk.gov.hscic.patient.immunisations.search.ImmunisationSearch;
import uk.gov.hscic.patient.immunisations.search.ImmunisationSearchFactory;
import uk.gov.hscic.patient.investigations.model.InvestigationListHTML;
import uk.gov.hscic.patient.investigations.search.InvestigationSearch;
import uk.gov.hscic.patient.investigations.search.InvestigationSearchFactory;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;
import uk.gov.hscic.patient.medication.search.MedicationSearch;
import uk.gov.hscic.patient.medication.search.MedicationSearchFactory;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;
import uk.gov.hscic.patient.observations.search.ObservationSearch;
import uk.gov.hscic.patient.observations.search.ObservationSearchFactory;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearch;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearchFactory;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.problems.search.ProblemSearchFactory;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;
import uk.gov.hscic.patient.referral.search.ReferralSearch;
import uk.gov.hscic.patient.referral.search.ReferralSearchFactory;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.search.PatientSearch;
import uk.gov.hscic.patient.summary.search.PatientSearchFactory;
import uk.gov.hscic.practitioner.resource.provider.PractitionerResourceProvider;

public class PatientResourceProvider implements IResourceProvider {
    
    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }
    
    ApplicationContext applicationContext;
    PractitionerResourceProvider practitionerResourceProvider;
    
    public void setResourceProviderLinks(ApplicationContext applicationContext, PractitionerResourceProvider practitionerResourceProvider){
        this.applicationContext = applicationContext;
        this.practitionerResourceProvider = practitionerResourceProvider;
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
        if(nhsNumber == null || nhsNumber.isEmpty() || !NhsCodeValidator.nhsNumberValid(nhsNumber)){
            
            OperationOutcome operationOutcome = new OperationOutcome();
            CodingDt errorCoding = new CodingDt();
            errorCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-getrecord-response-code-1-0");
            errorCoding.setCode("GCR-0002");
            CodeableConceptDt errorCodableConcept = new CodeableConceptDt();
            errorCodableConcept.addCoding(errorCoding);
            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setCode(IssueTypeEnum.INVALID_CONTENT).setDetails(errorCodableConcept).setDiagnostics("NHS Number Invalid");
            Entry operationOutcomeEntry = new Entry();
            operationOutcomeEntry.setResource(operationOutcome);
            bundle.addEntry(operationOutcomeEntry);
            
        } else {
            
            // Build the Patient Resource and add it to the bundle
            Entry patientEntry = buildPatientEntry(nhsNumber);
            bundle.addEntry(patientEntry);
            
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
            careRecordComposition.setSubject(new ResourceReferenceDt("Patient/"+nhsNumber));
            
            List<ResourceReferenceDt> careProviderPractitionerList = ((Patient)patientEntry.getResource()).getCareProvider();
            if(careProviderPractitionerList.size() > 0){
                String practitionerReferenceStr = careProviderPractitionerList.get(0).getReference().getValue();
                ResourceReferenceDt practitionerReference = new ResourceReferenceDt(practitionerReferenceStr);
                careRecordComposition.setAuthor(Collections.singletonList(practitionerReference));
                
                Entry practitionerEntry = new Entry();
                practitionerEntry.setResource(practitionerResourceProvider.getPractitionerById(new IdDt(practitionerReferenceStr)));
                practitionerEntry.setFullUrl(practitionerReferenceStr);
                bundle.addEntry(practitionerEntry);
            }
            
            
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
                            
                        case "Clinical Items" :
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
                            
                        case "Medications" :
                            MedicationSearch medicationSearch = applicationContext.getBean(MedicationSearchFactory.class).select(sourceType);
                                List<MedicationListHTML> medicationList = medicationSearch.findMedicationHTMLTables(nhsNumber);
                                if(medicationList != null && medicationList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Medications");
                                    CodingDt medicationCoding = new CodingDt();
                                    medicationCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    medicationCoding.setCode("MED");
                                    medicationCoding.setDisplay("Medications");
                                    CodeableConceptDt medicationCodableConcept = new CodeableConceptDt();
                                    medicationCodableConcept.addCoding(medicationCoding);
                                    medicationCodableConcept.setText(medicationList.get(0).getProvider());
                                    section.setCode(medicationCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(medicationList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Referrals" :
                            ReferralSearch referralSearch = applicationContext.getBean(ReferralSearchFactory.class).select(sourceType);
                                List<ReferralListHTML> referralList = referralSearch.findAllReferralHTMLTables(nhsNumber);
                                if(referralList != null && referralList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Referrals");
                                    CodingDt referralCoding = new CodingDt();
                                    referralCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    referralCoding.setCode("REF");
                                    referralCoding.setDisplay("Referrals");
                                    CodeableConceptDt referralCodableConcept = new CodeableConceptDt();
                                    referralCodableConcept.addCoding(referralCoding);
                                    referralCodableConcept.setText(referralList.get(0).getProvider());
                                    section.setCode(referralCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(referralList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Observations" :
                            ObservationSearch observationSearch = applicationContext.getBean(ObservationSearchFactory.class).select(sourceType);
                                List<ObservationListHTML> observationList = observationSearch.findAllObservationHTMLTables(nhsNumber);
                                if(observationList != null && observationList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Observations");
                                    CodingDt observationCoding = new CodingDt();
                                    observationCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    observationCoding.setCode("OBS");
                                    observationCoding.setDisplay("Observations");
                                    CodeableConceptDt observationCodableConcept = new CodeableConceptDt();
                                    observationCodableConcept.addCoding(observationCoding);
                                    observationCodableConcept.setText(observationList.get(0).getProvider());
                                    section.setCode(observationCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(observationList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Investigations" : //(Investigations)
                            InvestigationSearch investigationSearch = applicationContext.getBean(InvestigationSearchFactory.class).select(sourceType);
                                List<InvestigationListHTML> investigationList = investigationSearch.findAllInvestigationHTMLTables(nhsNumber);
                                if(investigationList != null && investigationList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Investigations");
                                    CodingDt investigationCoding = new CodingDt();
                                    investigationCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    investigationCoding.setCode("INV");
                                    investigationCoding.setDisplay("Investigations");
                                    CodeableConceptDt investigationCodableConcept = new CodeableConceptDt();
                                    investigationCodableConcept.addCoding(investigationCoding);
                                    investigationCodableConcept.setText(investigationList.get(0).getProvider());
                                    section.setCode(investigationCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(investigationList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Immunisations" :
                            ImmunisationSearch immunisationSearch = applicationContext.getBean(ImmunisationSearchFactory.class).select(sourceType);
                                List<ImmunisationListHTML> immunisationList = immunisationSearch.findAllImmunisationHTMLTables(nhsNumber);
                                if(immunisationList != null && immunisationList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Immunisations");
                                    CodingDt immunisationCoding = new CodingDt();
                                    immunisationCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    immunisationCoding.setCode("IMM");
                                    immunisationCoding.setDisplay("Immunisations");
                                    CodeableConceptDt immunisationCodableConcept = new CodeableConceptDt();
                                    immunisationCodableConcept.addCoding(immunisationCoding);
                                    immunisationCodableConcept.setText(immunisationList.get(0).getProvider());
                                    section.setCode(immunisationCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(immunisationList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
                            break;
                            
                        case "Administrative Items" :
                            AdminItemSearch adminItemSearch = applicationContext.getBean(AdminItemSearchFactory.class).select(sourceType);
                                List<AdminItemListHTML> adminItemList = adminItemSearch.findAllAdminItemHTMLTables(nhsNumber);
                                if(adminItemList != null && adminItemList.size() > 0){
                                    //We have a result so build section
                                    Section section = new Section();
                                    section.setTitle("Administrative Items");
                                    CodingDt adminItemCoding = new CodingDt();
                                    adminItemCoding.setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0");
                                    adminItemCoding.setCode("ADM");
                                    adminItemCoding.setDisplay("Administrative Items");
                                    CodeableConceptDt adminItemCodableConcept = new CodeableConceptDt();
                                    adminItemCodableConcept.addCoding(adminItemCoding);
                                    adminItemCodableConcept.setText(adminItemList.get(0).getProvider());
                                    section.setCode(adminItemCodableConcept);
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(adminItemList.get(0).getHtml());
                                    section.setText(narrative);
                                    sectionsList.add(section);
                                }
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
    
    @Read()
    public Patient getResourceById(@IdParam IdDt patientId) {
        
        Patient patient = new Patient();
        
        // Get patient details from dataabase
        RepoSource sourceType = RepoSourceType.fromString(null);
        PatientSearch patientSearch = applicationContext.getBean(PatientSearchFactory.class).select(sourceType);
        PatientDetails patientDetails = patientSearch.findPatient(patientId.getIdPart());
        
        patient.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", patientId.getIdPart()));

        HumanNameDt name = patient.addName();
        name.setText(patientDetails.getName());
        name.addFamily("FHIRTestSurname");
        name.addGiven("FHIRTestForename");
        name.addPrefix("Mr");
        name.setUse(NameUseEnum.USUAL);
        
        patient.setBirthDate(new DateDt(patientDetails.getDateOfBirth()));

        AddressDt address = patient.addAddress();
        address.setUse(AddressUseEnum.HOME);
        address.setType(AddressTypeEnum.PHYSICAL);
        address.setText(patientDetails.getAddress());
        
        patient.getCareProvider().add(new ResourceReferenceDt("Practitioner/"+patientDetails.getGpId()));
        
        return patient;
    }
    
    
    private Entry buildPatientEntry(String nhsNumber){
            
            // Build the Patient Resource in the response
            Entry patientEntry = new Entry();    
            patientEntry.setResource(getResourceById(new IdDt("Patient/"+nhsNumber)));
            patientEntry.setFullUrl("Patient/"+nhsNumber);
            return patientEntry;
    }
}
