package uk.gov.hscic.patient;

import uk.gov.hscic.medication.search.*;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.common.types.*;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.medications.*;
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
        
    private final ApplicationContext applicationContext;
    private final PractitionerResourceProvider practitionerResourceProvider;
    private final OrganizationResourceProvider organizationResourceProvider;
    private final MedicationResourceProvider medicationResourceProvider;
    private final MedicationOrderResourceProvider medicationOrderResourceProvider;
    private final MedicationDispenseResourceProvider medicationDispenseResourceProvider;
    private final MedicationAdministrationResourceProvider medicationAdministrationResourceProvider;
    private final AppointmentResourceProvider appointmentResourceProvider;
    
    public PatientResourceProvider(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
        this.practitionerResourceProvider = (PractitionerResourceProvider)applicationContext.getBean("practitionerResourceProvider", applicationContext);
        this.organizationResourceProvider = (OrganizationResourceProvider)applicationContext.getBean("organizationResourceProvider", applicationContext);
        this.medicationResourceProvider = (MedicationResourceProvider)applicationContext.getBean("medicationResourceProvider", applicationContext);
        this.medicationOrderResourceProvider = (MedicationOrderResourceProvider)applicationContext.getBean("medicationOrderResourceProvider", applicationContext);
        this.medicationDispenseResourceProvider = (MedicationDispenseResourceProvider)applicationContext.getBean("medicationDispenseResourceProvider", applicationContext);
        this.medicationAdministrationResourceProvider = (MedicationAdministrationResourceProvider)applicationContext.getBean("medicationAdministrationResourceProvider", applicationContext);
        this.appointmentResourceProvider = (AppointmentResourceProvider)applicationContext.getBean("appointmentResourceProvider", applicationContext);
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }
    
    @Read()
    public Patient getPatientById(@IdParam IdDt internalId) {
        Patient patient = new Patient();
        // Get patient details from dataabase
        RepoSource sourceType = RepoSourceType.fromString(null);
        PatientSearch patientSearch = applicationContext.getBean(PatientSearchFactory.class).select(sourceType);
        PatientDetails patientDetails = patientSearch.findPatientByInternalID(internalId.getIdPart());
        if(patientDetails == null){
            OperationOutcome operationOutcome = new OperationOutcome();
            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No patient details found for patient ID: "+internalId.getIdPart());
            throw new InternalErrorException("No patient details found for patient ID: "+internalId.getIdPart(), operationOutcome);
        }
        return patientDetailsToPatientResourceConverter(patientDetails);
    }
    
    
    @Search
    public List<Patient> getPatientByPatientId(@RequiredParam(name=Patient.SP_IDENTIFIER) TokenParam patientId) {
        RepoSource sourceType = RepoSourceType.fromString(null);
        PatientSearch patientSearch = applicationContext.getBean(PatientSearchFactory.class).select(sourceType);
        ArrayList<Patient> patients = new ArrayList();
        List<PatientDetails> PatientDetailsList = Collections.singletonList(patientSearch.findPatient(patientId.getValue()));
        if (PatientDetailsList != null && PatientDetailsList.size() > 0) {
            for(PatientDetails patientDetails : PatientDetailsList){
                Patient patient = patientDetailsToPatientResourceConverter(patientDetails);
                patient.setId(patientDetails.getId());
                patients.add(patient);
                
            }
        }
        return patients;
    }
    
    
    @Operation(name="$gpc.getcarerecord")
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
                String patientID;
                Entry patientEntry = new Entry();    
                List<Patient> patients = getPatientByPatientId(new TokenParam("",nhsNumber));
                if(patients != null && patients.size() > 0){
                    patientEntry.setResource(patients.get(0));
                    patientEntry.setFullUrl("Patient/"+patients.get(0).getId().getIdPart());
                    patientID = patients.get(0).getId().getIdPart();
                } else {
                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No patient details found for patient NHS Number: "+nhsNumber);
                    throw new InternalErrorException("No patient details found for patient NHS Number: "+nhsNumber, operationOutcome);
                
                }
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
                careRecordComposition.setSubject(new ResourceReferenceDt("Patient/"+patientID));

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
                                // HTML Section Search
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
                                
                                // Sructured Data Search
                                List<MedicationOrder> medicationOrders = medicationOrderResourceProvider.getMedicationOrdersForPatientId(patientID, null, null);
                                HashSet<String> medicationOrderMedicationsList = new HashSet();
                                HashSet<String> medicationOrderList = new HashSet();
                                for(MedicationOrder medicationOrder : medicationOrders){
                                    medicationOrderList.add(medicationOrder.getId().getIdPart());
                                }
                                List<MedicationDispense> medicationDispenses = medicationDispenseResourceProvider.getMedicationDispensesForPatientId(patientID, null, null);
                                for(MedicationDispense medicationDispense : medicationDispenses){
                                    if(section == null) section = new Section();
                                    // Add the medication Order to the bundle
                                    Entry medicationDispenseEntry = new Entry();
                                    medicationDispenseEntry.setFullUrl("MedicationDispense/"+medicationDispense.getId().getIdPart());
                                    medicationDispenseEntry.setResource(medicationDispense);
                                    section.addEntry().setReference(medicationDispenseEntry.getFullUrl());
                                    bundle.addEntry(medicationDispenseEntry);
                                    // If we have any new medicationOrders which were not found in the search for MedicationOrders for a patient we need to add them.
                                    if(!medicationOrderList.contains(medicationDispense.getAuthorizingPrescription().get(0).getReference().getIdPart())){
                                        try{
                                            MedicationOrder medicationOrder = medicationOrderResourceProvider.getMedicationOrderById(medicationDispense.getAuthorizingPrescription().get(0).getReference());
                                            medicationOrders.add(medicationOrder);
                                            medicationOrderList.add(medicationOrder.getId().getIdPart());
                                        } catch (Exception ex){
                                            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("MedicationOrder for MedicaitonDispense (id: "+medicationDispense.getId().getIdPart()+") could not be found in database");
                                        }
                                    }
                                }
                                List<MedicationAdministration> medicationAdministrations = medicationAdministrationResourceProvider.getMedicationAdministrationsForPatientId(patientID, null, null);
                                for(MedicationAdministration medicationAdministration : medicationAdministrations){
                                    if(section == null) section = new Section();
                                    Entry medicationAdministrationEntry = new Entry();
                                    medicationAdministrationEntry.setFullUrl("MedicationAdministration/"+medicationAdministration.getId().getIdPart());
                                    medicationAdministrationEntry.setResource(medicationAdministration);
                                    section.addEntry().setReference(medicationAdministrationEntry.getFullUrl());
                                    bundle.addEntry(medicationAdministrationEntry);
                                    // If we have any new medicationOrders which were not found in the search for MedicationOrders for a patient we need to add them.
                                    if(!medicationOrderList.contains(medicationAdministration.getPrescription().getReference().getIdPart())){
                                        try{
                                            MedicationOrder medicationOrder = medicationOrderResourceProvider.getMedicationOrderById(medicationAdministration.getPrescription().getReference());
                                            medicationOrders.add(medicationOrder);
                                            medicationOrderList.add(medicationOrder.getId().getIdPart());
                                        } catch (Exception ex){
                                            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("MedicationOrder for MedicaitonAdministration (id: "+medicationAdministration.getId().getIdPart()+") could not be found in database");
                                        }
                                    }
                                }
                                for(MedicationOrder medicationOrder : medicationOrders){
                                    if(section == null) section = new Section();
                                    // Add the medication Order to the bundle
                                    Entry medicationOrderEntry = new Entry();
                                    medicationOrderEntry.setFullUrl("MedicationOrder/"+medicationOrder.getId().getIdPart());
                                    medicationOrderEntry.setResource(medicationOrder);
                                    section.addEntry().setReference(medicationOrderEntry.getFullUrl());
                                    bundle.addEntry(medicationOrderEntry);
                                    // Store the referenced medicaitons in a set so we can get all the medications once and we won't have duplicates
                                    IdDt medicationId = ((ResourceReferenceDt)medicationOrder.getMedication()).getReference();
                                    medicationOrderMedicationsList.add(medicationId.getValue());
                                    medicationId = ((ResourceReferenceDt)medicationOrder.getDispenseRequest().getMedication()).getReference();
                                    medicationOrderMedicationsList.add(medicationId.getValue());
                                }
                                for(String medicationId : medicationOrderMedicationsList){
                                    try{
                                        Entry medicationEntry = new Entry();
                                        medicationEntry.setFullUrl(medicationId);
                                        medicationEntry.setResource(medicationResourceProvider.getMedicationById(new IdDt(medicationId)));
                                        section.addEntry().setReference(medicationEntry.getFullUrl());
                                        bundle.addEntry(medicationEntry);
                                    } catch (Exception ex){
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("Medication (ID: "+medicationId+") for MedicaitonOrder could not be found in database");
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
    public List<MedicationOrder> getPatientMedicationOrders(@IdParam IdDt patientLocalId) {
        return medicationOrderResourceProvider.getMedicationOrdersForPatientId(patientLocalId.getIdPart(), null, null);
    }
    
    @Search(compartmentName="MedicationDispense")
    public List<MedicationDispense> getPatientMedicationDispenses(@IdParam IdDt patientLocalId) {
        return medicationDispenseResourceProvider.getMedicationDispensesForPatientId(patientLocalId.getIdPart(), null, null);
    }
    
    @Search(compartmentName="MedicationAdministration")
    public List<MedicationAdministration> getPatientMedicationAdministration(@IdParam IdDt patientLocalId) {
        return medicationAdministrationResourceProvider.getMedicationAdministrationsForPatientId(patientLocalId.getIdPart(), null, null);
    }
    
    @Search(compartmentName="Appointment")
    public List<Appointment> getPatientAppointments(@IdParam IdDt patientLocalId) {
        return appointmentResourceProvider.getAppointmentsForPatientId(patientLocalId.getIdPart(), null, null);
    }
    
    
    public Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails){
        Patient patient = new Patient();
        patient.setId(patientDetails.getId());
        patient.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", patientDetails.getNhsNumber()));
        
        patient.getMeta().setLastUpdated(patientDetails.getLastUpdated());
        patient.getMeta().setVersionId(String.valueOf(patientDetails.getLastUpdated().getTime()));

        HumanNameDt name = patient.addName();
        name.setText(patientDetails.getName());
        name.addFamily(patientDetails.getSurname());
        name.addGiven(patientDetails.getForename());
        name.addPrefix(patientDetails.getTitle());
        name.setUse(NameUseEnum.USUAL);
        
        patient.setBirthDate(new DateDt(patientDetails.getDateOfBirth()));

        AddressDt address = patient.addAddress();
        address.setUse(AddressUseEnum.HOME);
        address.setType(AddressTypeEnum.PHYSICAL);
        address.setText(patientDetails.getAddress());
        
        patient.getCareProvider().add(new ResourceReferenceDt("Practitioner/"+patientDetails.getGpId()));
        
        return patient;
    }
}
