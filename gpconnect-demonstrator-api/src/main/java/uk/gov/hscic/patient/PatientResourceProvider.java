package uk.gov.hscic.patient;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.CompositionStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.activation.UnsupportedDataTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import uk.gov.hscic.medication.search.MedicationSearch;
import uk.gov.hscic.medications.MedicationAdministrationResourceProvider;
import uk.gov.hscic.medications.MedicationDispenseResourceProvider;
import uk.gov.hscic.medications.MedicationOrderResourceProvider;
import uk.gov.hscic.medications.MedicationResourceProvider;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.search.AllergySearch;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearch;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;
import uk.gov.hscic.patient.encounters.search.EncounterSearch;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;
import uk.gov.hscic.patient.immunisations.search.ImmunisationSearch;
import uk.gov.hscic.patient.investigations.model.InvestigationListHTML;
import uk.gov.hscic.patient.investigations.search.InvestigationSearch;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;
import uk.gov.hscic.patient.observations.search.ObservationSearch;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearch;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;
import uk.gov.hscic.patient.referral.search.ReferralSearch;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.search.PatientSearch;
import uk.gov.hscic.patient.summary.store.PatientStore;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

@Component
public class PatientResourceProvider implements IResourceProvider {
    private static final String REGISTRATION_TYPE_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1";
    private static final String REGISTRATION_STATUS_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1";
    private static final String REGISTRATION_PERIOD_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1";
    private static final String TEMPORARY_RESIDENT_REGISTRATION_TYPE = "T";
    private static final String ACTIVE_REGISTRATION_STATUS = "A";

    @Autowired
    private PractitionerResourceProvider practitionerResourceProvider;

    @Autowired
    private OrganizationResourceProvider organizationResourceProvider;

    @Autowired
    private MedicationResourceProvider medicationResourceProvider;

    @Autowired
    private MedicationOrderResourceProvider medicationOrderResourceProvider;

    @Autowired
    private MedicationDispenseResourceProvider medicationDispenseResourceProvider;

    @Autowired
    private MedicationAdministrationResourceProvider medicationAdministrationResourceProvider;

    @Autowired
    private AppointmentResourceProvider appointmentResourceProvider;

    @Autowired
    private PatientStore patientStore;

    @Autowired
    private PatientSearch patientSearch;

    @Autowired
    private PatientSummarySearch patientSummarySearch;

    @Autowired
    private ProblemSearch problemSearch;

    @Autowired
    private EncounterSearch encounterSearch;

    @Autowired
    private AllergySearch allergySearch;

    @Autowired
    private ClinicalItemSearch clinicalItemsSearch;

    @Autowired
    private MedicationSearch medicationSearch;

    @Autowired
    private ReferralSearch referralSearch;

    @Autowired
    private ObservationSearch observationSearch;

    @Autowired
    private InvestigationSearch investigationSearch;

    @Autowired
    private ImmunisationSearch immunisationSearch;

    @Autowired
    private AdminItemSearch adminItemSearch;

    @Autowired
    private OperationOutcomeCreation operationOutcomeCreation;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read()
    public Patient getPatientById(@IdParam IdDt internalId) {
        PatientDetails patientDetails = patientSearch.findPatientByInternalID(internalId.getIdPart());

        if (patientDetails == null) {
            String system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
            String code = "PATIENT_NOT_FOUND";
            String codableConceptText = "Patient Record Not Found";
            String metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-patient-1";

            OperationOutcome operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                    codableConceptText, metaProfile);
            throw new ResourceNotFoundException("No patient details found for patient ID: " + internalId.getIdPart(),
                    operationOutcome);
        }
        return patientDetailsToPatientResourceConverter(patientDetails);
    }

    @Search
    public List<Patient> getPatientByPatientId(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam patientId) {
        List<Patient> patients = new ArrayList<>();
        PatientDetails patientDetailsReturned = patientSearch.findPatient(patientId.getValue());

        if (patientDetailsReturned != null) {
            List<PatientDetails> patientDetailsList = Collections.singletonList(patientDetailsReturned);

            for (PatientDetails patientDetails : patientDetailsList) {
                Patient patient = patientDetailsToPatientResourceConverter(patientDetails);
                patient.setId(patientDetails.getId());
                patients.add(patient);
            }
        }

        if (patients.isEmpty()) {
            String system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
            String code = "PATIENT_NOT_FOUND";
            String codableConceptText = "Patient Record Not Found";
            String metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";

            OperationOutcome operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                    codableConceptText, metaProfile);
            throw new ResourceNotFoundException("No patient details found for patient ID: ", operationOutcome);
        }

        return patients;
    }

    @SuppressWarnings("deprecation")
    @Operation(name = "$gpc.getcarerecord")
    public Bundle getPatientCareRecord(@ResourceParam Parameters params) throws UnsupportedDataTypeException {
        OperationOutcome operationOutcome = new OperationOutcome();
        ArrayList<String> nhsNumber = new ArrayList<>();
        ArrayList<String> sectionsParamList = new ArrayList<>();
        ArrayList<Entry> medicationsToBundle = new ArrayList<>();
        String system = null;
        String code = null;
        String codableConceptText = null;
        String metaProfile = null;

        Date fromDate = null;
        Date toDate = null;
        // Extract the parameters
        boolean recordSectionNotPresent = true;

        for (Parameter param : params.getParameter()) {
            if (!param.getName().equals("patientNHSNumber") && !param.getName().equals("recordSection")
                    && !param.getName().equals("timePeriod")) {

                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                code = "INVALID_NHS_NUMBER";
                codableConceptText = "Patient Record Not Found";
                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";

                operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                        codableConceptText, metaProfile);
                throw new UnprocessableEntityException("Parameters are incorrect", operationOutcome);
            }

            IDatatype value = param.getValue();
            if (value instanceof IdentifierDt) {
                nhsNumber.add(((IdentifierDt) value).getValue());
                String nhsNumberSystemCheck = ((IdentifierDt) value).getSystem();

                // if (nhsNumberSystemCheck == null) {
                // system =
                // "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                // code = "INVALID_IDENTIFIER_SYSTEM";
                // codableConceptText = "Patient Record Not Found";
                // String metaProfile =
                // "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                //
                // operationOutcome =
                // operationOutcomeCreation.buildOperationOutcomeNotFound(system,
                // code,
                // codableConceptText, metaProfile);
                // throw new InvalidRequestException("System Invalid ",
                // operationOutcome);
                // }

                if (((IdentifierDt) value).getValue() == null) {
                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_NHS_NUMBER";
                    codableConceptText = "Patient Record Not Found";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                            codableConceptText, metaProfile);
                    throw new InvalidRequestException("System Invalid ", operationOutcome);

                }

                if (nhsNumber.get(0).isEmpty()) {
                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_NHS_NUMBER";
                    codableConceptText = "Patient Record Not Found";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                            codableConceptText, metaProfile);
                    throw new InvalidRequestException("System Invalid ", operationOutcome);
                }

                // if ((nhsNumberSystemCheck != null &&
                // nhsNumberSystemCheck.isEmpty() == true)
                // ||
                // !"http://fhir.nhs.net/Id/nhs-number".equals(nhsNumberSystemCheck))
                // {
                //
                // system =
                // "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                // code = "INVALID_IDENTIFIER_SYSTEM";
                // codableConceptText = "Patient Record Not Found";
                // metaProfile =
                // "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                // operationOutcome =
                // operationOutcomeCreation.buildOperationOutcomeNotFound(system,
                // code,
                // codableConceptText, metaProfile);
                // throw new InvalidRequestException("System Invalid ",
                // operationOutcome);
                // }

                if (nhsNumber.size() > 1) {
                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_IDENTIFIER_SYSTEM";
                    codableConceptText = "NHS Number Invalid";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeInvalidContent(system, code,
                            codableConceptText, metaProfile);
                    throw new InvalidRequestException("Bad Request Exception", operationOutcome);
                }
            } else if (value instanceof CodeableConceptDt) {
                recordSectionNotPresent = false;

                List<CodingDt> coading = ((CodeableConceptDt) value).getCoding();

                String systemCheck = coading.get(0).getSystem();
                String sectionName = coading.get(0).getCode();

                if (sectionName == null || systemCheck == null) {
                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_PARAMETER";
                    codableConceptText = "Patient Record Not Found";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                            codableConceptText, metaProfile);
                    throw new UnprocessableEntityException("System Invalid ", operationOutcome);
                }

                String testSectionName = sectionName;

                if (!sectionName.equals(testSectionName.toUpperCase())) {

                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_PARAMETER";
                    codableConceptText = "Invalid Section Code";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                            codableConceptText, metaProfile);
                    throw new UnprocessableEntityException("Section Case Invalid: ", operationOutcome);
                }

                if (!coading.get(0).getSystem().equals("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")) {
                    throw new InvalidRequestException("System Invalid ");
                }

                sectionsParamList.add(coading.get(0).getCode());

                if (sectionsParamList.size() > 1) {

                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_IDENTIFIER_SYSTEM";
                    codableConceptText = "Multiple Sections Added";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeInvalidContent(system, code,
                            codableConceptText, metaProfile);
                    throw new InvalidRequestException("Bad Request Exception", operationOutcome);

                }
            } else if (value instanceof PeriodDt) {

                // String sectionCheckValidTimeNotSet =
                // sectionsParamList.get(0);

                fromDate = ((PeriodDt) value).getStart();
                Calendar toCalendar = Calendar.getInstance();
                toDate = ((PeriodDt) value).getEnd();

                // if ((fromDate != null && toDate != null) &&
                // (sectionCheckValidTimeNotSet.equals("ALL")
                // || sectionCheckValidTimeNotSet.equals("IMM") ||
                // sectionCheckValidTimeNotSet.equals("MED")
                // || sectionCheckValidTimeNotSet.equals("OBS") ||
                // sectionCheckValidTimeNotSet.equals("PRB"))) {
                // throw new InvalidRequestException(
                // "Bad Request Exception, Time not allowed to be set for the
                // sections");
                // }

                if ((fromDate != null && toDate != null) && fromDate.after(toDate)) {
                    system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                    code = "INVALID_PARAMETER";
                    codableConceptText = "Patient Record Not Found";
                    metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                    operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                            codableConceptText, metaProfile);
                    throw new UnprocessableEntityException("Dates are invalid: ", operationOutcome);
                }

                if (toDate != null) {
                    toCalendar.setTime(toDate);

                    if (null != ((PeriodDt) value).getEndElement().getPrecision()) {
                        switch (((PeriodDt) value).getEndElement().getPrecision()) {
                        case YEAR:
                            toCalendar.add(Calendar.YEAR, 1);
                            break;
                        case MONTH:
                            toCalendar.add(Calendar.MONTH, 1);
                            break;
                        case DAY:
                            toCalendar.add(Calendar.DATE, 1);
                            break;
                        default:
                            break;
                        }
                    }
                    toDate = toCalendar.getTime();
                }
            }
        }

        if (recordSectionNotPresent) {
            throw new InvalidRequestException("No record section");
        }

        // Build Bundle
        Bundle bundle = new Bundle();
        bundle.setType(BundleTypeEnum.DOCUMENT);

        for (int i = 0; i < sectionsParamList.size(); i++) {
            if (sectionsParamList.get(i) == null || sectionsParamList.get(i).length() != 3) {
                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                code = "INVALID_NHS_NUMBER";
                codableConceptText = "NHS Number Invalid";
                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                operationOutcome = operationOutcomeCreation.buildOperationOutcomeInvalidContent(system, code,
                        codableConceptText, metaProfile);
                throw new ResourceNotFoundException("NHS number Invalid " + operationOutcome);
            }
        }

        if (nhsNumber.isEmpty()) {
            throw new InvalidRequestException("NHS number not supplied");
        } else {
            if (NhsValidation(nhsNumber.get(0))) {
                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                code = "INVALID_NHS_NUMBER";
                codableConceptText = "NHS Number Invalid";
                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
                operationOutcome = operationOutcomeCreation.buildOperationOutcomeInvalidContent(system, code,
                        codableConceptText, metaProfile);
                throw new InvalidRequestException("NHS number Invalid " + operationOutcome);
            } else {
                // Build the Patient Resource and add it to the bundle
                try {
                    String patientID;
                    Entry patientEntry = new Entry();
                    List<Patient> patients = getPatientByPatientId(new TokenParam("", nhsNumber.get(0)));
                    if (patients != null && patients.size() > 0) {
                        patientEntry.setResource(patients.get(0));
                        patientEntry.setFullUrl("Patient/" + patients.get(0).getId().getIdPart());
                        patientID = patients.get(0).getId().getIdPart();
                    } else {
                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                .setDetails("No patient details found for patient NHS Number: " + nhsNumber.get(0));
                        operationOutcome.getMeta()
                                .addProfile("http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1");
                        throw new InternalErrorException(
                                "No patient details found for patient NHS Number: " + nhsNumber.get(0),
                                operationOutcome);
                    }
                    // Build the Care Record Composition
                    Entry careRecordEntry = new Entry();

                    Composition careRecordComposition = new Composition();
                    careRecordComposition.setDate(new DateTimeDt(Calendar.getInstance().getTime()));

                    CodingDt coding = new CodingDt().setSystem("http://snomed.info/sct").setCode("425173008")
                            .setDisplay("record extract (record artifact)");
                    CodeableConceptDt codableConcept = new CodeableConceptDt().addCoding(coding)
                            .setText("record extract (record artifact)");
                    careRecordComposition.setType(codableConcept);

                    CodingDt classCoding = new CodingDt().setSystem("http://snomed.info/sct").setCode("700232004")
                            .setDisplay("general medical service (qualifier value)");
                    CodeableConceptDt classCodableConcept = new CodeableConceptDt().addCoding(classCoding)
                            .setText("general medical service (qualifier value)");
                    careRecordComposition.setClassElement(classCodableConcept);

                    careRecordComposition.setTitle("Patient Care Record");
                    careRecordComposition.setStatus(CompositionStatusEnum.FINAL);
                    careRecordComposition.setSubject(new ResourceReferenceDt("Patient/" + patientID));
                    careRecordComposition.getMeta()
                            .addProfile("http://fhir.nhs.net/StructureDefinition/gpconnect-carerecord-composition-1");

                    // Build requested sections
                    if (sectionsParamList.size() > 0) {
                        ArrayList<Section> sectionsList = new ArrayList<>();
                        for (String sectionName : sectionsParamList) {
                            String testSectionName = sectionName;

                            if (!sectionName.equals(testSectionName.toUpperCase())) {

                                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                                code = "INVALID_PARAMETER";
                                codableConceptText = "Patient Record Not Found";
                                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";

                                operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                                        codableConceptText, metaProfile);
                                throw new UnprocessableEntityException("Section Case Invalid: ", operationOutcome);
                            }

                            Section section = new Section();

                            switch (sectionName) {
                            case "SUM":
                                if (nhsNumber.get(0) == null) {
                                    throw new AssertionError();
                                }

                                List<PatientSummaryListHTML> patientSummaryList = patientSummarySearch
                                        .findAllPatientSummaryHTMLTables(nhsNumber.get(0));

                                if (patientSummaryList != null && patientSummaryList.size() > 0) {
                                    CodingDt summaryCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("SUM").setDisplay("Summary");
                                    CodeableConceptDt summaryCodableConcept = new CodeableConceptDt()
                                            .addCoding(summaryCoding).setText(patientSummaryList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);

                                    if (patientSummaryList.get(0).getHtml().contains("This is confidential")) {

                                        system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                                        code = "NO_PATIENT_CONSENT";
                                        codableConceptText = "Patient Data Confidential";
                                        metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";

                                        operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(
                                                system, code, codableConceptText, metaProfile);
                                        throw new ForbiddenOperationException("This Data Is Confidential",
                                                operationOutcome);

                                    } else {
                                        narrative.setDivAsString(patientSummaryList.get(0).getHtml());
                                        section.setTitle("Summary").setCode(summaryCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    }

                                } else {

                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                            .setDetails("No data available for the requested section: Summary");
                                }

                                break;

                            case "PRB":
                                if (toDate != null && fromDate != null) {
                                    throw new InvalidRequestException("Date Ranges not allowed to be set");
                                } else {
                                    List<ProblemListHTML> problemList = problemSearch
                                            .findAllProblemHTMLTables(nhsNumber.get(0));

                                    if (problemList != null && problemList.size() > 0) {
                                        CodingDt problemCoding = new CodingDt()
                                                .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                                .setCode("PRB").setDisplay("Problems");
                                        CodeableConceptDt problemCodableConcept = new CodeableConceptDt()
                                                .addCoding(problemCoding).setText(problemList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(problemList.get(0).getHtml());
                                        section.setTitle("Problems").setCode(problemCodableConcept).setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                                .setDetails("No data available for the requested section: Problems");
                                    }
                                }

                                break;

                            case "ENC":
                                List<EncounterListHTML> encounterList = encounterSearch
                                        .findAllEncounterHTMLTables(nhsNumber.get(0), fromDate, toDate);

                                if (encounterList != null && encounterList.size() > 0) {
                                    CodingDt encounterCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("ENC").setDisplay("Encounters");
                                    CodeableConceptDt encounterCodableConcept = new CodeableConceptDt()
                                            .addCoding(encounterCoding).setText(encounterList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(encounterList.get(0).getHtml());
                                    section.setTitle("Encounters").setCode(encounterCodableConcept).setText(narrative);
                                    sectionsList.add(section);
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                            .setDetails("No data available for the requested section: Encounters");
                                }

                                break;

                            case "ALL":
                                if (toDate != null && fromDate != null) {
                                    throw new InvalidRequestException("Date Ranges not allowed to be set");
                                } else {
                                    List<AllergyListHTML> allergyList = allergySearch
                                            .findAllAllergyHTMLTables(nhsNumber.get(0));
                                    if (allergyList != null && allergyList.size() > 0) {
                                        CodingDt allergyCoding = new CodingDt()
                                                .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                                .setCode("ALL").setDisplay("Allergies and Sensitivities");
                                        CodeableConceptDt allergyCodableConcept = new CodeableConceptDt()
                                                .addCoding(allergyCoding).setText(allergyList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(allergyList.get(0).getHtml());

                                        section.setTitle("Allergies and Sensitivities").setCode(allergyCodableConcept)
                                                .setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(
                                                "No data available for the requested section: Allergies and Sensitivities");
                                    }
                                }

                                break;

                            case "CLI":
                                List<ClinicalItemListHTML> clinicalItemList = clinicalItemsSearch
                                        .findAllClinicalItemHTMLTables(nhsNumber.get(0));

                                if (clinicalItemList != null && clinicalItemList.size() > 0) {
                                    CodingDt clinicalItemCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("CLI").setDisplay("Clinical Items");
                                    CodeableConceptDt clinicalItemCodableConcept = new CodeableConceptDt()
                                            .addCoding(clinicalItemCoding)
                                            .setText(clinicalItemList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(clinicalItemList.get(0).getHtml());
                                    section.setTitle("Clinical Items").setCode(clinicalItemCodableConcept)
                                            .setText(narrative);
                                    sectionsList.add(section);
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                            .setDetails("No data available for the requested section: Clinical Items");
                                }

                                break;

                            case "MED":
                                section = null;
                                // HTML Section Search

                                if (toDate != null && fromDate != null) {
                                    throw new InvalidRequestException("Date Ranges not allowed to be set");
                                }

                                List<PatientMedicationHTML> medicationList = medicationSearch
                                        .findPatientMedicationHTML(nhsNumber.get(0));

                                if (medicationList != null && medicationList.size() > 0) {
                                    section = new Section();
                                    CodingDt medicationCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("MED").setDisplay("Medications");
                                    CodeableConceptDt medicationCodableConcept = new CodeableConceptDt()
                                            .addCoding(medicationCoding).setText(medicationList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(medicationList.get(0).getHtml());
                                    section.setTitle("Medications").setCode(medicationCodableConcept)
                                            .setText(narrative);
                                    sectionsList.add(section);
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                            .setDetails("No data available for the requested section: Medication");
                                }

                                // Sructured Data Search
                                List<MedicationOrder> medicationOrders = medicationOrderResourceProvider
                                        .getMedicationOrdersForPatientId(patientID);
                                HashSet<String> medicationOrderMedicationsList = new HashSet();
                                HashSet<String> medicationOrderList = new HashSet();

                                for (MedicationOrder medicationOrder : medicationOrders) {
                                    medicationOrderList.add(medicationOrder.getId().getIdPart());
                                }

                                List<MedicationDispense> medicationDispenses = medicationDispenseResourceProvider
                                        .getMedicationDispensesForPatientId(patientID);

                                for (MedicationDispense medicationDispense : medicationDispenses) {
                                    if (section == null) {
                                        section = new Section();
                                    }
                                    // Add the medication Order to the bundle
                                    Entry medicationDispenseEntry = new Entry();
                                    medicationDispenseEntry
                                            .setFullUrl("MedicationDispense/" + medicationDispense.getId().getIdPart());
                                    medicationDispenseEntry.setResource(medicationDispense);

                                    medicationsToBundle.add(medicationDispenseEntry);
                                    section.addEntry().setReference(medicationDispenseEntry.getFullUrl());
                                    // If we have any new medicationOrders which
                                    // were not found in the
                                    // search for MedicationOrders for a patient
                                    // we need to add them.
                                    if (!medicationOrderList.contains(medicationDispense.getAuthorizingPrescription()
                                            .get(0).getReference().getIdPart())) {
                                        try {
                                            MedicationOrder medicationOrder = medicationOrderResourceProvider
                                                    .getMedicationOrderById(medicationDispense
                                                            .getAuthorizingPrescription().get(0).getReference());
                                            medicationOrders.add(medicationOrder);
                                            medicationOrderList.add(medicationOrder.getId().getIdPart());
                                        } catch (Exception ex) {
                                            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                                    .setDetails("MedicationOrder for MedicaitonDispense (id: "
                                                            + medicationDispense.getId().getIdPart()
                                                            + ") could not be found in database");
                                        }
                                    }
                                }

                                List<MedicationAdministration> medicationAdministrations = medicationAdministrationResourceProvider
                                        .getMedicationAdministrationsForPatientId(patientID);

                                for (MedicationAdministration medicationAdministration : medicationAdministrations) {
                                    if (section == null) {
                                        section = new Section();
                                    }

                                    Entry medicationAdministrationEntry = new Entry();
                                    medicationAdministrationEntry.setFullUrl(
                                            "MedicationAdministration/" + medicationAdministration.getId().getIdPart());
                                    medicationAdministrationEntry.setResource(medicationAdministration);
                                    section.addEntry().setReference(medicationAdministrationEntry.getFullUrl());
                                    medicationsToBundle.add(medicationAdministrationEntry);

                                    // If we have any new medicationOrders which
                                    // were not found in the
                                    // search for MedicationOrders for a patient
                                    // we need to add them.
                                    if (!medicationOrderList.contains(
                                            medicationAdministration.getPrescription().getReference().getIdPart())) {
                                        try {
                                            MedicationOrder medicationOrder = medicationOrderResourceProvider
                                                    .getMedicationOrderById(
                                                            medicationAdministration.getPrescription().getReference());
                                            medicationOrders.add(medicationOrder);
                                            medicationOrderList.add(medicationOrder.getId().getIdPart());
                                        } catch (Exception ex) {
                                            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                                    .setDetails("MedicationOrder for MedicaitonAdministration (id: "
                                                            + medicationAdministration.getId().getIdPart()
                                                            + ") could not be found in database");
                                        }
                                    }
                                }

                                for (MedicationOrder medicationOrder : medicationOrders) {
                                    if (section == null) {
                                        section = new Section();
                                    }
                                    // Add the medication Order to the bundle
                                    Entry medicationOrderEntry = new Entry();
                                    medicationOrderEntry
                                            .setFullUrl("MedicationOrder/" + medicationOrder.getId().getIdPart());
                                    medicationOrderEntry.setResource(medicationOrder);
                                    section.addEntry().setReference(medicationOrderEntry.getFullUrl());
                                    medicationsToBundle.add(medicationOrderEntry);

                                    // Store the referenced medicaitons in a set
                                    // so we can get
                                    // all the medications once and we won't
                                    // have duplicates
                                    IdDt medicationId = ((ResourceReferenceDt) medicationOrder.getMedication())
                                            .getReference();
                                    medicationOrderMedicationsList.add(medicationId.getValue());
                                    medicationId = ((ResourceReferenceDt) medicationOrder.getDispenseRequest()
                                            .getMedication()).getReference();
                                    medicationOrderMedicationsList.add(medicationId.getValue());
                                }

                                for (String medicationId : medicationOrderMedicationsList) {
                                    try {
                                        Entry medicationEntry = new Entry();
                                        medicationEntry.setFullUrl(medicationId);
                                        medicationEntry.setResource(
                                                medicationResourceProvider.getMedicationById(new IdDt(medicationId)));
                                        section.addEntry().setReference(medicationEntry.getFullUrl());
                                        medicationsToBundle.add(medicationEntry);

                                    } catch (Exception ex) {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                                .setDetails("Medication (ID: " + medicationId
                                                        + ") for MedicaitonOrder could not be found in database");
                                    }
                                }

                                if (section != null) {
                                    sectionsList.add(section);
                                }

                                break;

                            case "REF":
                                List<ReferralListHTML> referralList = referralSearch
                                        .findAllReferralHTMLTables(nhsNumber.get(0));

                                if (referralList != null && referralList.size() > 0) {
                                    CodingDt referralCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("REF").setDisplay("Referrals");
                                    CodeableConceptDt referralCodableConcept = new CodeableConceptDt()
                                            .addCoding(referralCoding).setText(referralList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(referralList.get(0).getHtml());
                                    section.setTitle("Referrals").setCode(referralCodableConcept).setText(narrative);
                                    sectionsList.add(section);
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                            .setDetails("No data available for the requested section: Referrals");
                                }

                                break;

                            case "OBS":
                                if (toDate != null && fromDate != null) {
                                    throw new InvalidRequestException("Date Ranges not allowed to be set");
                                } else {
                                    List<ObservationListHTML> observationList = observationSearch
                                            .findAllObservationHTMLTables(nhsNumber.get(0));

                                    if (observationList != null && observationList.size() > 0) {
                                        CodingDt observationCoding = new CodingDt()
                                                .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                                .setCode("OBS").setDisplay("Observations");
                                        CodeableConceptDt observationCodableConcept = new CodeableConceptDt()
                                                .addCoding(observationCoding)
                                                .setText(observationList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(observationList.get(0).getHtml());
                                        section.setTitle("Observations").setCode(observationCodableConcept)
                                                .setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(
                                                "No data available for the requested section: Observations");
                                    }
                                }

                                break;

                            case "INV":
                                List<InvestigationListHTML> investigationList = investigationSearch
                                        .findAllInvestigationHTMLTables(nhsNumber.get(0));

                                if (investigationList != null && investigationList.size() > 0) {
                                    CodingDt investigationCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("INV").setDisplay("Investigations");
                                    CodeableConceptDt investigationCodableConcept = new CodeableConceptDt()
                                            .addCoding(investigationCoding)
                                            .setText(investigationList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(investigationList.get(0).getHtml());
                                    section.setTitle("Investigations").setCode(investigationCodableConcept)
                                            .setText(narrative);
                                    sectionsList.add(section);
                                    throw new InvalidRequestException("Too many sets of investivations");
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                            .setDetails("No data available for the requested section: Investigations");
                                }

                                break;

                            case "IMM":
                                if (toDate != null && fromDate != null) {
                                    throw new InvalidRequestException("Date Ranges not allowed to be set");
                                } else {
                                    List<ImmunisationListHTML> immunisationList = immunisationSearch
                                            .findAllImmunisationHTMLTables(nhsNumber.get(0));
                                    if (immunisationList != null && immunisationList.size() > 0) {
                                        CodingDt immunisationCoding = new CodingDt()
                                                .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                                .setCode("IMM").setDisplay("Immunisations");
                                        CodeableConceptDt immunisationCodableConcept = new CodeableConceptDt()
                                                .addCoding(immunisationCoding)
                                                .setText(immunisationList.get(0).getProvider());
                                        NarrativeDt narrative = new NarrativeDt();
                                        narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                        narrative.setDivAsString(immunisationList.get(0).getHtml());
                                        section.setTitle("Immunisations").setCode(immunisationCodableConcept)
                                                .setText(narrative);
                                        sectionsList.add(section);
                                    } else {
                                        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(
                                                "No data available for the requested section: Immunisations");
                                    }
                                }

                                break;

                            case "ADM":
                                List<AdminItemListHTML> adminItemList = adminItemSearch
                                        .findAllAdminItemHTMLTables(nhsNumber.get(0));

                                if (adminItemList != null && adminItemList.size() > 0) {
                                    CodingDt adminItemCoding = new CodingDt()
                                            .setSystem("http://fhir.nhs.net/ValueSet/gpconnect-record-section-1")
                                            .setCode("ADM").setDisplay("Administrative Items");
                                    CodeableConceptDt adminItemCodableConcept = new CodeableConceptDt()
                                            .addCoding(adminItemCoding).setText(adminItemList.get(0).getProvider());
                                    NarrativeDt narrative = new NarrativeDt();
                                    narrative.setStatus(NarrativeStatusEnum.GENERATED);
                                    narrative.setDivAsString(adminItemList.get(0).getHtml());
                                    section.setTitle("Administrative Items").setCode(adminItemCodableConcept)
                                            .setText(narrative);
                                    sectionsList.add(section);
                                } else {
                                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(
                                            "No data available for the requested section: AdministrativeItems");
                                }

                                break;

                            default:
                                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                                code = "INVALID_PARAMETER";
                                codableConceptText = "Patient Record Not Found";
                                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";

                                operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                                        codableConceptText, metaProfile);
                                throw new UnprocessableEntityException("Dates are invalid: ", operationOutcome);
                            }
                        }

                        careRecordComposition.setSection(sectionsList);
                    }

                    careRecordEntry.setResource(careRecordComposition);

                    bundle.addEntry(careRecordEntry);

                    for (Entry e : medicationsToBundle) {
                        bundle.addEntry(e);
                    }

                    List<ResourceReferenceDt> careProviderPractitionerList = ((Patient) patientEntry.getResource())
                            .getCareProvider();

                    if (careProviderPractitionerList.size() > 0) {
                        careRecordComposition.setAuthor(Collections.singletonList(new ResourceReferenceDt(
                                careProviderPractitionerList.get(0).getReference().getValue())));
                        try {
                            Practitioner practitioner = practitionerResourceProvider.getPractitionerById(
                                    new IdDt(careProviderPractitionerList.get(0).getReference().getValue()));

                            if (practitioner == null) {
                                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                                code = "REFERENCE_NOT_FOUND";
                                codableConceptText = "Invalid Reference";
                                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-practitioner-1";

                                operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                                        codableConceptText, metaProfile);
                                throw new ResourceNotFoundException("Practitioner Reference returning null");
                            }
                            practitioner.getMeta()
                                    .addProfile("http://fhir.nhs.net/StructureDefinition/gpconnect-practitioner-1");

                            Entry practitionerEntry = new Entry().setResource(practitioner)
                                    .setFullUrl(careProviderPractitionerList.get(0).getReference().getValue());

                            bundle.addEntry(practitionerEntry);

                            Entry organizationEntry = new Entry();
                            organizationEntry.setResource(organizationResourceProvider.getOrganizationById(practitioner
                                    .getPractitionerRoleFirstRep().getManagingOrganization().getReference()));

                            organizationEntry.setFullUrl(practitioner.getPractitionerRoleFirstRep()
                                    .getManagingOrganization().getReference());

                            if (organizationEntry.getResource() == null || organizationEntry.getFullUrl() == null) {

                                system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
                                code = "REFERENCE_NOT_FOUND";
                                codableConceptText = "Invalid Reference";
                                metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-practitioner-1";

                                operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                                        codableConceptText, metaProfile);
                                throw new ResourceNotFoundException("organizationResource returning null",
                                        operationOutcome);
                            }

                            bundle.addEntry(organizationEntry);
                        } catch (InternalErrorException ex) {
                            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                                    .setDetails(ex.getLocalizedMessage());
                        }
                    }

                    bundle.addEntry(patientEntry);
                } catch (InternalErrorException ex) {
                    // If the patient details could not be found
                    operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails(ex.getMessage());
                }
            }
        }

        if (operationOutcome.getIssue().size() > 0) {
            Entry operationOutcomeEntry = new Entry();
            operationOutcomeEntry.setResource(operationOutcome);
            Entry hi = new Entry();
            bundle.addEntry(hi);
            bundle.addEntry(operationOutcomeEntry);
        }

        return bundle;
    }

    private boolean NhsValidation(String nhsNumber) {
        return nhsNumber == null || !NhsCodeValidator.nhsNumberValid(nhsNumber) || nhsNumber.length() != 10
                || nhsNumber.contains("[a-zA-Z]+");
    }

    @Search(compartmentName = "MedicationOrder")
    public List<MedicationOrder> getPatientMedicationOrders(@IdParam IdDt patientLocalId) {
        return medicationOrderResourceProvider.getMedicationOrdersForPatientId(patientLocalId.getIdPart());
    }

    @Search(compartmentName = "MedicationDispense")
    public List<MedicationDispense> getPatientMedicationDispenses(@IdParam IdDt patientLocalId) {
        return medicationDispenseResourceProvider.getMedicationDispensesForPatientId(patientLocalId.getIdPart());
    }

    @Search(compartmentName = "MedicationAdministration")
    public List<MedicationAdministration> getPatientMedicationAdministration(@IdParam IdDt patientLocalId) {
        return medicationAdministrationResourceProvider
                .getMedicationAdministrationsForPatientId(patientLocalId.getIdPart());
    }

    @Search(compartmentName = "Appointment")
    public List<Appointment> getPatientAppointments(@IdParam IdDt patientLocalId,
            @OptionalParam(name = "start") DateRangeParam startDate) {
        return appointmentResourceProvider.getAppointmentsForPatientIdAndDates(patientLocalId, startDate);
    }

    @Operation(name = "$gpc.registerpatient")
    public Bundle registerPatient(@ResourceParam Parameters params) {
        Patient unregisteredPatient = null;
        Patient registeredPatient = null;

        for (Parameter param : params.getParameter()) {
            if ("registerPatient".equalsIgnoreCase(param.getName())) {
                unregisteredPatient = (Patient) param.getResource();
            }
        }

        if (unregisteredPatient != null) {
            // check if the patient already exists
            PatientDetails patientDetails = patientSearch
                    .findPatient(unregisteredPatient.getIdentifierFirstRep().getValue());

            if (patientDetails == null) {
                patientStore.create(registerPatientResourceConverterToPatientDetail(unregisteredPatient));
                registeredPatient = patientDetailsToRegisterPatientResourceConverter(
                        patientSearch.findPatient(unregisteredPatient.getIdentifierFirstRep().getValue()));
            } else {
                registeredPatient = patientDetailsToRegisterPatientResourceConverter(patientDetails);
            }
        } else {

            String system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
            String code = "INVALID_PARAMETER";
            String codableConceptText = "Patient Record Not Found";
            String metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-practitioner-1";

            OperationOutcome operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                    codableConceptText, metaProfile);

            throw new UnprocessableEntityException("Section Case Invalid: ", operationOutcome);
        }

        Bundle bundle = new Bundle();
        bundle.setType(BundleTypeEnum.TRANSACTION_RESPONSE);

        bundle.addEntry().setResource(registeredPatient);
        return bundle;
    }

    private PatientDetails registerPatientResourceConverterToPatientDetail(Patient patientResource) {
        PatientDetails patientDetails = new PatientDetails();
        HumanNameDt name = patientResource.getNameFirstRep();
        patientDetails.setForename(name.getGivenAsSingleString());
        patientDetails.setSurname(name.getFamilyAsSingleString());
        patientDetails.setDateOfBirth(patientResource.getBirthDate());
        patientDetails.setGender(patientResource.getGender());
        patientDetails.setNhsNumber(patientResource.getIdentifierFirstRep().getValue());

        Date now = new Date();
        List<ExtensionDt> registrationPeriodExtensions = patientResource
                .getUndeclaredExtensionsByUrl(REGISTRATION_PERIOD_EXTENSION_URL);
        ExtensionDt registrationPeriodExtension = registrationPeriodExtensions.get(0);
        PeriodDt registrationPeriod = (PeriodDt) registrationPeriodExtension.getValue();

        Date registrationStart = registrationPeriod.getStart();

        if (registrationStart.compareTo(now) <= 1) {
            patientDetails.setRegistrationStartDateTime(registrationStart);
        } else {
            throw new IllegalArgumentException(String.format(
                    "The given registration start (%c) is not valid. The registration start cannot be in the future.",
                    registrationStart));
        }

        Date registrationEnd = registrationPeriod.getEnd();

        if (registrationEnd != null) {
            throw new IllegalArgumentException(String.format(
                    "The given registration end (%c) is not valid. The registration end should be left blank to indicate an open-ended registration period.",
                    registrationStart));
        }

        List<ExtensionDt> registrationStatusExtensions = patientResource
                .getUndeclaredExtensionsByUrl(REGISTRATION_STATUS_EXTENSION_URL);
        ExtensionDt registrationStatusExtension = registrationStatusExtensions.get(0);
        CodeableConceptDt registrationStatusCode = (CodeableConceptDt) registrationStatusExtension.getValue();
        String registrationStatus = registrationStatusCode.getCodingFirstRep().getCode();

        if (ACTIVE_REGISTRATION_STATUS.equals(registrationStatus)) {
            patientDetails.setRegistrationStatus(registrationStatus);
        } else {
            throw new IllegalArgumentException(String.format(
                    "The given registration status is not valid. Expected - A. Actual - %s", registrationStatus));
        }

        List<ExtensionDt> registrationTypeExtensions = patientResource
                .getUndeclaredExtensionsByUrl(REGISTRATION_TYPE_EXTENSION_URL);
        ExtensionDt registrationTypeExtension = registrationTypeExtensions.get(0);
        CodeableConceptDt registrationTypeCode = (CodeableConceptDt) registrationTypeExtension.getValue();
        String registrationType = registrationTypeCode.getCodingFirstRep().getCode();

        if (TEMPORARY_RESIDENT_REGISTRATION_TYPE.equals(registrationType)) {
            patientDetails.setRegistrationType(registrationType);
        } else {
            throw new IllegalArgumentException(String
                    .format("The given registration type is not valid. Expected - T. Actual - %s", registrationType));
        }

        return patientDetails;
    }

    // a cut-down Patient
    private Patient patientDetailsToRegisterPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = new Patient();
        patient.setId(patientDetails.getId());
        patient.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", patientDetails.getNhsNumber()));

        HumanNameDt name = patient.addName();
        name.addFamily(patientDetails.getSurname());
        name.addGiven(patientDetails.getForename());
        name.setUse(NameUseEnum.USUAL);

        patient.setBirthDate(new DateDt(patientDetails.getDateOfBirth()));

        patient.setGender(AdministrativeGenderEnum.forCode(patientDetails.getGender().toLowerCase()));

        PeriodDt registrationPeriod = new PeriodDt();
        registrationPeriod.setStartWithSecondsPrecision(patientDetails.getRegistrationStartDateTime());
        registrationPeriod.setEndWithSecondsPrecision(patientDetails.getRegistrationEndDateTime());
        patient.addUndeclaredExtension(true, REGISTRATION_PERIOD_EXTENSION_URL, registrationPeriod);

        CodeableConceptDt registrationStatus = new CodeableConceptDt(
                "http://fhir.nhs.net/ValueSet/registration-status-1", patientDetails.getRegistrationStatus());
        patient.addUndeclaredExtension(true, REGISTRATION_STATUS_EXTENSION_URL, registrationStatus);

        CodeableConceptDt registrationType = new CodeableConceptDt("http://fhir.nhs.net/ValueSet/registration-type-1",
                patientDetails.getRegistrationType());
        patient.addUndeclaredExtension(true, REGISTRATION_TYPE_EXTENSION_URL, registrationType);

        return patient;
    }

    public Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = new Patient();
        patient.setId(patientDetails.getId());
        patient.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", patientDetails.getNhsNumber()));

        Date lastUpdated = patientDetails.getLastUpdated();

        if (lastUpdated != null) {
            patient.getMeta().setLastUpdated(lastUpdated);
            patient.getMeta().setVersionId(String.valueOf(lastUpdated.getTime()));
        }

        HumanNameDt name = patient.addName();
        name.setText(patientDetails.getName());
        name.addFamily(patientDetails.getSurname());
        name.addGiven(patientDetails.getForename());
        name.addPrefix(patientDetails.getTitle());
        name.setUse(NameUseEnum.USUAL);

        patient.setBirthDate(new DateDt(patientDetails.getDateOfBirth()));
        patient.getMeta().addProfile("http://fhir.nhs.net/StructureDefinition/gpconnect-patient-1");

        String addressLines = patientDetails.getAddress();

        if (addressLines != null) {
            AddressDt address = patient.addAddress();
            address.setUse(AddressUseEnum.HOME);
            address.setType(AddressTypeEnum.PHYSICAL);
            address.setText(addressLines);
        }

        Long gpId = patientDetails.getGpId();

        if (gpId != null) {
            Practitioner practitioner = practitionerResourceProvider.getPractitionerById(new IdDt(gpId));
            ResourceReferenceDt practitionerReference = new ResourceReferenceDt("Practitioner/" + gpId);
            practitionerReference.setDisplay(practitioner.getName().getPrefixFirstRep() + " "
                    + practitioner.getName().getGivenFirstRep() + " " + practitioner.getName().getFamilyFirstRep());
            patient.getCareProvider().add(practitionerReference);
        } else {

            String system = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
            String code = "PATIENT_NOT_FOUND";
            String codableConceptText = "Patient Record Not Found";
            String metaProfile = "http://fhir.nhs.net/StructureDefinition/gpconnect-practitioner-1";

            OperationOutcome operationOutcome = operationOutcomeCreation.buildOperationOutcomeNotFound(system, code,
                    codableConceptText, metaProfile);
            throw new ResourceNotFoundException("No GP record exists " + operationOutcome);
        }

        String gender = patientDetails.getGender();
        if (gender != null) {
            patient.setGender(AdministrativeGenderEnum.forCode(gender.toLowerCase()));
        }

        String telephoneNumber = patientDetails.getTelephone();
        if (telephoneNumber != null) {
            ContactPointDt telephone = new ContactPointDt();
            telephone.setSystem(ContactPointSystemEnum.PHONE);
            telephone.setValue(telephoneNumber);
            telephone.setUse(ContactPointUseEnum.HOME);
            patient.setTelecom(Collections.singletonList(telephone));
        }

        Date registrationStartDateTime = patientDetails.getRegistrationStartDateTime();
        if (registrationStartDateTime != null) {
            PeriodDt registrationPeriod = new PeriodDt();
            registrationPeriod.setStartWithSecondsPrecision(registrationStartDateTime);
            registrationPeriod.setEndWithSecondsPrecision(patientDetails.getRegistrationEndDateTime());
            patient.addUndeclaredExtension(true, REGISTRATION_PERIOD_EXTENSION_URL, registrationPeriod);
        }

        String registrationStatusValue = patientDetails.getRegistrationStatus();
        if (registrationStatusValue != null) {
            CodeableConceptDt registrationStatus = new CodeableConceptDt(
                    "http://fhir.nhs.net/ValueSet/registration-status-1", registrationStatusValue);
            patient.addUndeclaredExtension(true, REGISTRATION_STATUS_EXTENSION_URL, registrationStatus);
        }

        String registrationTypeValue = patientDetails.getRegistrationType();
        if (registrationTypeValue != null) {
            CodeableConceptDt registrationType = new CodeableConceptDt(
                    "http://fhir.nhs.net/ValueSet/registration-type-1", registrationTypeValue);
            patient.addUndeclaredExtension(true, REGISTRATION_TYPE_EXTENSION_URL, registrationType);
        }

        return patient;
    }

}
