package uk.gov.hscic.patient;

import uk.gov.hscic.medication.search.MedicationSearchFactory;
import uk.gov.hscic.medication.search.MedicationSearch;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
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
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
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
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.medications.MedicationOrderResourceProvider;
import uk.gov.hscic.medications.MedicationResourceProvider;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.adminitems.model.*;
import uk.gov.hscic.patient.adminitems.search.*;
import uk.gov.hscic.patient.allergies.model.*;
import uk.gov.hscic.patient.allergies.search.*;
import uk.gov.hscic.patient.clinicalitems.model.*;
import uk.gov.hscic.patient.clinicalitems.search.*;
import uk.gov.hscic.patient.encounters.model.*;
import uk.gov.hscic.patient.encounters.search.*;
import uk.gov.hscic.patient.immunisations.model.*;
import uk.gov.hscic.patient.immunisations.search.*;
import uk.gov.hscic.patient.investigations.model.*;
import uk.gov.hscic.patient.investigations.search.*;
import uk.gov.hscic.patient.observations.model.*;
import uk.gov.hscic.patient.observations.search.*;
import uk.gov.hscic.patient.patientsummary.model.*;
import uk.gov.hscic.patient.patientsummary.search.*;
import uk.gov.hscic.patient.problems.model.*;
import uk.gov.hscic.patient.problems.search.*;
import uk.gov.hscic.patient.referral.model.*;
import uk.gov.hscic.patient.referral.search.*;
import uk.gov.hscic.patient.summary.model.*;
import uk.gov.hscic.patient.summary.search.*;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

public class PatientResourceProvider implements IResourceProvider {
        
    ApplicationContext applicationContext;
    PractitionerResourceProvider practitionerResourceProvider;
    OrganizationResourceProvider organizationResourceProvider;
    MedicationResourceProvider medicationResourceProvider;
    MedicationOrderResourceProvider medicationOrderResourceProvider;
    
    public PatientResourceProvider(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
        this.practitionerResourceProvider = (PractitionerResourceProvider)applicationContext.getBean("practitionerResourceProvider", applicationContext);
        this.organizationResourceProvider = (OrganizationResourceProvider)applicationContext.getBean("organizationResourceProvider", applicationContext);
        this.medicationResourceProvider = (MedicationResourceProvider)applicationContext.getBean("medicationResourceProvider", applicationContext);
        this.medicationOrderResourceProvider = (MedicationOrderResourceProvider)applicationContext.getBean("medicationOrderResourceProvider", applicationContext);
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }
    
    @Read()
    public Patient getPatientById(@IdParam IdDt patientId) {
        
        Patient patient = new Patient();
        
        // Get patient details from dataabase
        RepoSource sourceType = RepoSourceType.fromString(null);
        PatientSearch patientSearch = applicationContext.getBean(PatientSearchFactory.class).select(sourceType);
        PatientDetails patientDetails = patientSearch.findPatient(patientId.getIdPart());
        
        if(patientDetails == null){
            OperationOutcome operationOutcome = new OperationOutcome();
            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No patient details found for patient ID: "+patientId.getIdPart());
            throw new InternalErrorException("No patient details found for patient ID: "+patientId.getIdPart(), operationOutcome);
        }
        
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
    
    
    @Operation(name="$getcarerecord")
    public Bundle getPatientCareRecord(@ResourceParam Parameters params){
                
        OperationOutcome operationOutcome = new OperationOutcome();
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
        
        if(nhsNumber == null || nhsNumber.isEmpty() || !NhsCodeValidator.nhsNumberValid(nhsNumber)){
            
            CodingDt errorCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-getrecord-response-code-1-0").setCode("GCR-0002");
            CodeableConceptDt errorCodableConcept = new CodeableConceptDt().addCoding(errorCoding);
            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setCode(IssueTypeEnum.INVALID_CONTENT).setDetails(errorCodableConcept).setDiagnostics("NHS Number Invalid");
            
        } else {
            
            // Build the Patient Resource and add it to the bundle
            try{
                Entry patientEntry = buildPatientEntry(nhsNumber);
                bundle.addEntry(patientEntry);

                //Build the Care Record Composition
                Entry careRecordEntry = new Entry();
                Composition careRecordComposition = new Composition();
                careRecordComposition.setDate(new DateTimeDt(Calendar.getInstance().getTime()));
                
                CodingDt coding = new CodingDt().setSystem("http://snomed.info/sct").setCode("425173008").setDisplay("record extract (record artifact)");
                CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding).setText("record extract (record artifact)");
                careRecordComposition.setType(codableConcept);

                CodingDt classCoding = new CodingDt().setSystem("http://snomed.info/sct").setCode("700232004").setDisplay("general medical service (qualifier value)");
                CodeableConceptDt classCodableConcept = new CodeableConceptDt().addCoding(classCoding).setText("general medical service (qualifier value)");
                careRecordComposition.setClassElement(classCodableConcept);

                careRecordComposition.setTitle("Patient Care Record");
                careRecordComposition.setStatus(CompositionStatusEnum.FINAL);
                careRecordComposition.setSubject(new ResourceReferenceDt("Patient/"+nhsNumber));

                List<ResourceReferenceDt> careProviderPractitionerList = ((Patient)patientEntry.getResource()).getCareProvider();
                if(careProviderPractitionerList.size() > 0){
                    careRecordComposition.setAuthor(Collections.singletonList(new ResourceReferenceDt(careProviderPractitionerList.get(0).getReference().getValue())));
                    try{
                        Practitioner practitioner = practitionerResourceProvider.getPractitionerById(new IdDt(careProviderPractitionerList.get(0).getReference().getValue()));
                        Entry practitionerEntry = new Entry().setResource(practitioner).setFullUrl(careProviderPractitionerList.get(0).getReference().getValue());
                        bundle.addEntry(practitionerEntry);
                        
                        Entry organizationEntry = new Entry();
                        organizationEntry.setResource(organizationResourceProvider.getOrganizationById(practitioner.getPractitionerRoleFirstRep().getManagingOrganization().getReference()));
                        organizationEntry.setFullUrl(practitioner.getPractitionerRoleFirstRep().getManagingOrganization().getReference());
                        bundle.addEntry(organizationEntry);
                    }catch(InternalErrorException ex){
                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(ex.getLocalizedMessage());
                    }
                }

                // Build requested sections
                if(sectionsParamList.size() > 0){
                    
                    ArrayList<Section> sectionsList = new ArrayList();
                    RepoSource sourceType = RepoSourceType.fromString(null);
                    
                    for(String sectionName : sectionsParamList){
                        
                        Section section = new Section();
                        
                        switch(sectionName){
                            case "Summary" :
                                    PatientSummarySearch patientSummarySearch = applicationContext.getBean(PatientSummarySearchFactory.class).select(sourceType);
                                    List<PatientSummaryListHTML> patientSummaryList = patientSummarySearch.findAllPatientSummaryHTMLTables(nhsNumber);
                                    if(patientSummaryList != null && patientSummaryList.size() > 0){
                                        CodingDt summaryCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("SUM").setDisplay("Summary");
                                        CodeableConceptDt summaryCodableConcept = new CodeableConceptDt().addCoding(summaryCoding).setText(patientSummaryList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(patientSummaryList.get(0).getHtml());
                                        section.setTitle("Summary").setCode(summaryCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Summary");
                                    }
                                break;

                            case "Problems" :
                                ProblemSearch problemSearch = applicationContext.getBean(ProblemSearchFactory.class).select(sourceType);
                                    List<ProblemListHTML> problemList = problemSearch.findAllProblemHTMLTables(nhsNumber);
                                    if(problemList != null && problemList.size() > 0){
                                        CodingDt problemCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("PRB").setDisplay("Problems");
                                        CodeableConceptDt problemCodableConcept = new CodeableConceptDt().addCoding(problemCoding).setText(problemList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(problemList.get(0).getHtml());
                                        section.setTitle("Problems").setCode(problemCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Problems");
                                    }
                                break;

                            case "Encounters" :
                                EncounterSearch encounterSearch = applicationContext.getBean(EncounterSearchFactory.class).select(sourceType);
                                    List<EncounterListHTML> encounterList = encounterSearch.findAllEncounterHTMLTables(nhsNumber);
                                    if(encounterList != null && encounterList.size() > 0){
                                        CodingDt encounterCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("ENC").setDisplay("Encounters");
                                        CodeableConceptDt encounterCodableConcept = new CodeableConceptDt().addCoding(encounterCoding).setText(encounterList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(encounterList.get(0).getHtml());
                                        section.setTitle("Encounters").setCode(encounterCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Encounters");
                                    }
                                break;

                            case "Allergies and Sensitivities" :
                                AllergySearch allergySearch = applicationContext.getBean(AllergySearchFactory.class).select(sourceType);
                                    List<AllergyListHTML> allergyList = allergySearch.findAllAllergyHTMLTables(nhsNumber);
                                    if(allergyList != null && allergyList.size() > 0){
                                        CodingDt allergyCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("ALL").setDisplay("Allergies and Sensitivities");
                                        CodeableConceptDt allergyCodableConcept = new CodeableConceptDt().addCoding(allergyCoding).setText(allergyList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(allergyList.get(0).getHtml());
                                        section.setTitle("Allergies and Sensitivities").setCode(allergyCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Allergies and Sensitivities");
                                    }
                                break;

                            case "Clinical Items" :
                                ClinicalItemSearch clinicalItemsSearch = applicationContext.getBean(ClinicalItemSearchFactory.class).select(sourceType);
                                    List<ClinicalItemListHTML> clinicalItemList = clinicalItemsSearch.findAllClinicalItemHTMLTables(nhsNumber);
                                    if(clinicalItemList != null && clinicalItemList.size() > 0){
                                        CodingDt clinicalItemCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("CLI").setDisplay("Clinical Items");
                                        CodeableConceptDt clinicalItemCodableConcept = new CodeableConceptDt().addCoding(clinicalItemCoding).setText(clinicalItemList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(clinicalItemList.get(0).getHtml());
                                        section.setTitle("Clinical Items").setCode(clinicalItemCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Clinical Items");
                                    }
                                break;

                            case "Medications" :
                                
                                section = null;
                                
                                MedicationSearch medicationSearch = applicationContext.getBean(MedicationSearchFactory.class).select(sourceType);
                                List<PatientMedicationHTML> medicationList = medicationSearch.findPatientMedicationHTML(nhsNumber);
                                if(medicationList != null && medicationList.size() > 0){
                                    section = new Section();
                                    CodingDt medicationCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("MED").setDisplay("Medications");
                                    CodeableConceptDt medicationCodableConcept = new CodeableConceptDt().addCoding(medicationCoding).setText(medicationList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(medicationList.get(0).getHtml());
                                    section.setTitle("Medications").setCode(medicationCodableConcept).setText(narrative);
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Medication");
                                }
                                
                                List<MedicationOrder> medicationOrders = medicationOrderResourceProvider.getMedicationOrdersForPatientId(nhsNumber, null, null);
                                HashSet<IdDt> medicationOrderMedicationsList = new HashSet();
                                for(MedicationOrder medicationOrder : medicationOrders){
                                    if(section == null) section = new Section();

                                    // Add the medication Order to the bundle
                                    Entry medicationOrderEntry = new Entry();
                                    medicationOrderEntry.setFullUrl("MedicationOrder/"+medicationOrder.getId().getValue());
                                    medicationOrderEntry.setResource(medicationOrder);

                                    section.addEntry().setReference(medicationOrderEntry.getFullUrl());
                                    bundle.addEntry(medicationOrderEntry);

                                    // Store the referenced medicaitons in a set so we can get all the medications once and we won't have duplicates
                                    IdDt medicationId = ((ResourceReferenceDt)medicationOrder.getMedication()).getReference();
                                    medicationOrderMedicationsList.add(medicationId);

                                    medicationId = ((ResourceReferenceDt)medicationOrder.getDispenseRequest().getMedication()).getReference();
                                    medicationOrderMedicationsList.add(medicationId);
                                }

                                if(medicationOrderMedicationsList.size() > 0){
                                    for(IdDt medicationId : medicationOrderMedicationsList){

                                        try{
                                            Entry medicationEntry = new Entry();
                                            medicationEntry.setFullUrl(medicationId.getValue());
                                            medicationEntry.setResource(medicationResourceProvider.getMedicationById(medicationId));
                                            section.addEntry().setReference(medicationEntry.getFullUrl());
                                            bundle.addEntry(medicationEntry);
                                        } catch (Exception ex){
                                            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Medication for MedicaitonOrder could not be found in database");
                                        }
                                    }
                                }
                                
                                if(section != null){
                                    sectionsList.add(section);
                                }
                                break;

                            case "Referrals" :
                                ReferralSearch referralSearch = applicationContext.getBean(ReferralSearchFactory.class).select(sourceType);
                                    List<ReferralListHTML> referralList = referralSearch.findAllReferralHTMLTables(nhsNumber);
                                    if(referralList != null && referralList.size() > 0){
                                        CodingDt referralCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("REF").setDisplay("Referrals");
                                        CodeableConceptDt referralCodableConcept = new CodeableConceptDt().addCoding(referralCoding).setText(referralList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(referralList.get(0).getHtml());
                                        section.setTitle("Referrals").setCode(referralCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Referrals");
                                    }
                                break;

                            case "Observations" :
                                ObservationSearch observationSearch = applicationContext.getBean(ObservationSearchFactory.class).select(sourceType);
                                    List<ObservationListHTML> observationList = observationSearch.findAllObservationHTMLTables(nhsNumber);
                                    if(observationList != null && observationList.size() > 0){
                                        CodingDt observationCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("OBS").setDisplay("Observations");
                                        CodeableConceptDt observationCodableConcept = new CodeableConceptDt().addCoding(observationCoding).setText(observationList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(observationList.get(0).getHtml());
                                        section.setTitle("Observations").setCode(observationCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Observations");
                                    }
                                break;

                            case "Investigations" :
                                InvestigationSearch investigationSearch = applicationContext.getBean(InvestigationSearchFactory.class).select(sourceType);
                                    List<InvestigationListHTML> investigationList = investigationSearch.findAllInvestigationHTMLTables(nhsNumber);
                                    if(investigationList != null && investigationList.size() > 0){
                                        CodingDt investigationCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("INV").setDisplay("Investigations");
                                        CodeableConceptDt investigationCodableConcept = new CodeableConceptDt().addCoding(investigationCoding).setText(investigationList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(investigationList.get(0).getHtml());
                                        section.setTitle("Investigations").setCode(investigationCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Investigations");
                                    }
                                break;

                            case "Immunisations" :
                                ImmunisationSearch immunisationSearch = applicationContext.getBean(ImmunisationSearchFactory.class).select(sourceType);
                                    List<ImmunisationListHTML> immunisationList = immunisationSearch.findAllImmunisationHTMLTables(nhsNumber);
                                    if(immunisationList != null && immunisationList.size() > 0){
                                        CodingDt immunisationCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("IMM").setDisplay("Immunisations");
                                        CodeableConceptDt immunisationCodableConcept = new CodeableConceptDt().addCoding(immunisationCoding).setText(immunisationList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(immunisationList.get(0).getHtml());
                                        section.setTitle("Immunisations").setCode(immunisationCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: Immunisations");
                                    }
                                break;

                            case "Administrative Items" :
                                AdminItemSearch adminItemSearch = applicationContext.getBean(AdminItemSearchFactory.class).select(sourceType);
                                    List<AdminItemListHTML> adminItemList = adminItemSearch.findAllAdminItemHTMLTables(nhsNumber);
                                    if(adminItemList != null && adminItemList.size() > 0){
                                        CodingDt adminItemCoding = new CodingDt().setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1-0").setCode("ADM").setDisplay("Administrative Items");
                                        CodeableConceptDt adminItemCodableConcept = new CodeableConceptDt().addCoding(adminItemCoding).setText(adminItemList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(adminItemList.get(0).getHtml());
                                        section.setTitle("Administrative Items").setCode(adminItemCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No data available for the requested section: AdministrativeItems");
                                    }
                                break;
                                
                            default :
                                operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("The requested section '" + sectionName + "' is not a valid section.");
                                break;

                        }
                    }

                    careRecordComposition.setSection(sectionsList);
                }

                careRecordEntry.setResource(careRecordComposition);
                bundle.addEntry(careRecordEntry);
                
            }catch(InternalErrorException ex){
                
                //If the patient details could not be found
                operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(ex.getMessage());
            }
        }
        
        if(operationOutcome.getIssue().size() > 0){
            Entry operationOutcomeEntry = new Entry();
            operationOutcomeEntry.setResource(operationOutcome);
            bundle.addEntry(operationOutcomeEntry);
        }
        
        return bundle;
    }
    
    @Search(compartmentName="MedicationOrder")
    public List<MedicationOrder> getPatientMedicationOrders(@IdParam IdDt patientId) {
        return medicationOrderResourceProvider.getMedicationOrdersForPatientId(patientId.getIdPart(), null, null);
    }
    
    private Entry buildPatientEntry(String nhsNumber){
            
            // Build the Patient Resource in the response
            Entry patientEntry = new Entry();    
            patientEntry.setResource(getPatientById(new IdDt("Patient/"+nhsNumber)));
            patientEntry.setFullUrl("Patient/"+nhsNumber);
            return patientEntry;
    }
}
