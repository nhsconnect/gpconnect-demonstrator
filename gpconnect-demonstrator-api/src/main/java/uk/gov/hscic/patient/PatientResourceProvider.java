package uk.gov.hscic.patient;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
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
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
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
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.activation.UnsupportedDataTypeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.CodableConceptText;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.medications.MedicationAdministrationResourceProvider;
import uk.gov.hscic.medications.MedicationDispenseResourceProvider;
import uk.gov.hscic.medications.MedicationOrderResourceProvider;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.details.search.PatientSearch;
import uk.gov.hscic.patient.details.store.PatientStore;
import uk.gov.hscic.patient.html.FhirSectionBuilder;
import uk.gov.hscic.patient.html.Page;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.model.PatientSummary;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

@Component
public class PatientResourceProvider implements IResourceProvider {
    private static final String TEMPORARY_RESIDENT_REGISTRATION_TYPE = "T";
    private static final String ACTIVE_REGISTRATION_STATUS = "A";

    private static final List<String> MANDATORY_PARAM_NAMES = Arrays.asList("patientNHSNumber", "recordSection");
    private static final List<String> PERMITTED_PARAM_NAMES = new ArrayList<String>(MANDATORY_PARAM_NAMES) {{
        add("timePeriod");
    }};

    @Autowired
    private PractitionerResourceProvider practitionerResourceProvider;

    @Autowired
    private OrganizationResourceProvider organizationResourceProvider;

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
    private PageSectionFactory pageSectionFactory;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient getPatientById(@IdParam IdDt internalId) {
        PatientDetails patientDetails = patientSearch.findPatientByInternalID(internalId.getIdPart());

        if (patientDetails == null) {
            throw new ResourceNotFoundException("No patient details found for patient ID: " + internalId.getIdPart(),
                    OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                            SystemCode.PATIENT_NOT_FOUND, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                            SystemURL.SD_GPC_PATIENT, IssueTypeEnum.NOT_FOUND));
        }

        return patientDetailsToPatientResourceConverter(patientDetails);
    }

    @Search
    public List<Patient> getPatientsByPatientId(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam patientId) {
        return Collections.singletonList(getPatientByPatientId(patientId));
    }

    private Patient getPatientByPatientId(TokenParam patientId) {
        PatientDetails patientDetails = patientSearch.findPatient(patientId.getValue());

        if (null == patientDetails) {
            throw new ResourceNotFoundException("No patient details found for patient ID: " + patientId,
                    OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                            SystemCode.PATIENT_NOT_FOUND, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                            SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
        }

        return patientDetailsToPatientResourceConverter(patientDetails);
    }

    @SuppressWarnings("deprecation")
    @Operation(name = "$gpc.getcarerecord")
    public Bundle getPatientCareRecord(@ResourceParam Parameters params) throws UnsupportedDataTypeException {
        List<String> parameters = params.getParameter()
                .stream()
                .map(Parameter::getName)
                .collect(Collectors.toList());

        if (!PERMITTED_PARAM_NAMES.containsAll(parameters)) {
            throw new UnprocessableEntityException("Invalid parameters",
                    OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.INVALID_PARAMETER,
                            CodableConceptText.INVALID_PARAMETER, SystemURL.SD_GPC_OPERATIONOUTCOME,
                            IssueTypeEnum.INVALID_CONTENT));
        }

        if (!parameters.containsAll(MANDATORY_PARAM_NAMES)) {
            throw new InvalidRequestException("Missing parameters",
                    OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.INVALID_PARAMETER,
                            CodableConceptText.INVALID_PARAMETER, SystemURL.SD_GPC_OPERATIONOUTCOME,
                            IssueTypeEnum.INVALID_CONTENT));
        }

        String nhsNumber = null;
        String sectionName = null;
        Date fromDate = null;
        Date toDate = null;
        Date requestedFromDate = null;
        Date requestedToDate = null;

        for (Parameter param : params.getParameter()) {
            IDatatype value = param.getValue();

            if (value instanceof IdentifierDt) {
                if (null != nhsNumber) {
                    throw new InvalidRequestException("Bad Request Exception",
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.INVALID_IDENTIFIER_SYSTEM,
                                    CodableConceptText.NHS_NUMBER_INVALID,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME,
                                    IssueTypeEnum.INVALID_CONTENT));
                }

                nhsNumber = ((IdentifierDt) value).getValue();

                if (StringUtils.isBlank(nhsNumber)) {
                    throw new InvalidRequestException("System Invalid",
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.INVALID_NHS_NUMBER, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                    throw new InvalidRequestException("NHS number Invalid", OperationOutcomeFactory.buildOperationOutcome(
                            SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.INVALID_NHS_NUMBER,
                            CodableConceptText.NHS_NUMBER_INVALID,
                            SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }

                PatientSummary patientSummary = patientSearch.findPatientSummary(nhsNumber);

                if (null == patientSummary) {
                    throw new ResourceNotFoundException("No patient details found for patient ID: " + nhsNumber,
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.PATIENT_NOT_FOUND, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (patientSummary.isSensitive()) {
                    throw new ForbiddenOperationException("No patient consent", OperationOutcomeFactory.buildOperationOutcome(
                            SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.NO_PATIENT_CONSENT,
                            CodableConceptText.PATIENT_DATA_CONFIDENTIAL,
                            SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.FORBIDDEN));
                }
            } else if (value instanceof CodeableConceptDt) {
                if (null != sectionName) {
                    throw new InvalidRequestException("Bad Request Exception",
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.INVALID_IDENTIFIER_SYSTEM,
                                    CodableConceptText.MULTIPLE_SECTIONS_ADDED,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME,
                                    IssueTypeEnum.INVALID_CONTENT));
                }

                CodingDt coding = ((CodeableConceptDt) value).getCodingFirstRep();

                String system = coding.getSystem();
                sectionName = coding.getCode();

                if (system == null || sectionName == null) {
                    throw new UnprocessableEntityException("System Invalid",
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.INVALID_PARAMETER, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!sectionName.equals(sectionName.toUpperCase(Locale.UK))) {
                    throw new UnprocessableEntityException("Section Case Invalid: " + sectionName,
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.INVALID_PARAMETER, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!system.equals(SystemURL.VS_GPC_RECORD_SECTION)) {
                    throw new InvalidRequestException("System Invalid");
                }
            } else if (value instanceof PeriodDt) {
                PeriodDt period = (PeriodDt) value;

                fromDate = period.getStart();
                toDate = period.getEnd();
                requestedFromDate = period.getStart();
                requestedToDate = period.getEnd();

                if (fromDate != null && toDate != null && fromDate.after(toDate)) {
                    throw new UnprocessableEntityException("Dates are invalid: " + fromDate + ", " + toDate,
                            OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                    SystemCode.INVALID_PARAMETER, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                                    SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (toDate != null) {
                    toDate = period.getEndElement().getPrecision().add(toDate, 1);

                    requestedToDate = period.getEndElement().getPrecision().add(requestedToDate, 1);
                    Calendar toDateCalendar = Calendar.getInstance();
                    toDateCalendar.setTime(requestedToDate);
                    toDateCalendar.add(Calendar.DATE, -1);
                    requestedToDate = toDateCalendar.getTime();
                }
            } else {
                throw new InvalidRequestException("Invalid datatype",
                        OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.INVALID_PARAMETER,
                                CodableConceptText.INVALID_PARAMETER, SystemURL.SD_GPC_OPERATIONOUTCOME,
                                IssueTypeEnum.INVALID_CONTENT));
            }
        }

        // Build requested section
        Page page;

        switch (sectionName) {
            case "SUM":
                page = new Page("Summary", sectionName);
                page.addPageSection(pageSectionFactory.getPRBActivePageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getMEDCurrentPageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getMEDRepeatPageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getALLCurrentPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getENCPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "PRB":
                page = new Page("Problems", sectionName);
                page.addPageSection(pageSectionFactory.getPRBActivePageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getPRBInctivePageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "ENC":
                page = new Page("Encounters", sectionName);
                page.addPageSection(pageSectionFactory.getENCPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "ALL":
                page = new Page("Allergies and Adverse Reactions", sectionName);
                page.addPageSection(pageSectionFactory.getALLCurrentPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getALLHistoricalPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "CLI":
                page = new Page("Clinical Items", sectionName);
                page.addPageSection(pageSectionFactory.getCLIPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "MED":
                page = new Page("Medications", sectionName);
                page.addPageSection(pageSectionFactory.getMEDCurrentPageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getMEDRepeatPageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getMEDPastPageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "REF":
                page = new Page("Referrals", sectionName);
                page.addPageSection(pageSectionFactory.getREFPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "OBS":
                page = new Page("Observations", sectionName);
                page.addPageSection(pageSectionFactory.getOBSPageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "INV":
                page = new Page("Investigations", sectionName);
                page.addPageSection(pageSectionFactory.getINVPageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "IMM":
                page = new Page("Immunisations", sectionName);
                page.addPageSection(pageSectionFactory.getIMMPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "ADM":
                page = new Page("Administrative Items", sectionName);
                page.addPageSection(pageSectionFactory.getADMPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            default:
                throw new UnprocessableEntityException("Invalid section code: " + sectionName,
                        OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                SystemCode.INVALID_PARAMETER,
                                CodableConceptText.INVALID_SECTION_CODE,
                                SystemURL.SD_GPC_OPERATIONOUTCOME,
                                IssueTypeEnum.NOT_FOUND));
        }

        // Build the Patient Resource and add it to the bundle
        Patient patient = getPatientByPatientId(new TokenParam("", nhsNumber));
        String patientId = patient.getId().getIdPart();

        Bundle.Entry patientEntry = new Bundle.Entry()
                .setResource(patient)
                .setFullUrl("Patient/" + patientId);

        CodingDt coding = new CodingDt()
                .setSystem(SystemURL.SNOMED)
                .setCode("425173008")
                .setDisplay("record extract (record artifact)");

        CodeableConceptDt codableConcept = new CodeableConceptDt()
                .addCoding(coding)
                .setText("record extract (record artifact)");

        CodingDt classCoding = new CodingDt()
                .setSystem(SystemURL.SNOMED)
                .setCode("700232004")
                .setDisplay("general medical service (qualifier value)");

        CodeableConceptDt classCodableConcept = new CodeableConceptDt().addCoding(classCoding)
                .setText("general medical service (qualifier value)");

        Composition careRecordComposition = new Composition()
                .setDate(new DateTimeDt(Calendar.getInstance().getTime()))
                .setType(codableConcept)
                .setClassElement(classCodableConcept)
                .setTitle("Patient Care Record")
                .setStatus(CompositionStatusEnum.FINAL)
                .setSubject(new ResourceReferenceDt("Patient/" + patientId));

        careRecordComposition.getMeta().addProfile(SystemURL.SD_GPC_CARERECORD_COMPOSITION);

        careRecordComposition.setSection(Collections.singletonList(FhirSectionBuilder.buildFhirSection(page)));

        // Build the Care Record Composition
        Bundle bundle = new Bundle()
                .setType(BundleTypeEnum.DOCUMENT)
                .addEntry(new Bundle.Entry().setResource(careRecordComposition));

        List<ResourceReferenceDt> careProviderPractitionerList = ((Patient) patientEntry.getResource()).getCareProvider();

        if (!careProviderPractitionerList.isEmpty()) {
            String id = careProviderPractitionerList.get(0).getReference().getValue();
            careRecordComposition.setAuthor(Collections.singletonList(new ResourceReferenceDt(id)));

            Practitioner practitioner = practitionerResourceProvider.getPractitionerById(new IdDt(id));

            if (practitioner == null) {
                throw new ResourceNotFoundException("Practitioner Reference returning null",
                        OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                SystemCode.REFERENCE_NOT_FOUND,
                                CodableConceptText.INVALID_REFERENCE,
                                SystemURL.SD_GPC_PRACTITIONER,
                                IssueTypeEnum.NOT_FOUND));
            }

            practitioner.getMeta().addProfile(SystemURL.SD_GPC_PRACTITIONER);

            bundle.addEntry(new Bundle.Entry().setResource(practitioner).setFullUrl(id));

            IdDt organizationId = practitioner.getPractitionerRoleFirstRep().getManagingOrganization().getReference();

            Bundle.Entry organizationEntry = new Bundle.Entry()
                    .setResource(organizationResourceProvider.getOrganizationById(organizationId))
                    .setFullUrl(organizationId);

            if (organizationEntry.getResource() == null || organizationEntry.getFullUrl() == null) {
                throw new ResourceNotFoundException("organizationResource returning null",
                        OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                                SystemCode.REFERENCE_NOT_FOUND,
                                CodableConceptText.INVALID_REFERENCE,
                                SystemURL.SD_GPC_PRACTITIONER,
                                IssueTypeEnum.NOT_FOUND));
            }

            bundle.addEntry(organizationEntry);
        }

        return bundle.addEntry(patientEntry);
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
        Patient registeredPatient = null;

        Patient unregisteredPatient = params.getParameter()
                .stream()
                .filter(param -> "registerPatient".equalsIgnoreCase(param.getName()))
                .map(Parameter::getResource)
                .map(Patient.class::cast)
                .findFirst()
                .orElse(null);

        if (unregisteredPatient != null) {
            // check if the patient already exists
            PatientDetails patientDetails = patientSearch.findPatient(unregisteredPatient.getIdentifierFirstRep().getValue());

            if (patientDetails == null) {
                patientStore.create(registerPatientResourceConverterToPatientDetail(unregisteredPatient));
                registeredPatient = patientDetailsToRegisterPatientResourceConverter(
                        patientSearch.findPatient(unregisteredPatient.getIdentifierFirstRep().getValue()));
            } else {
                registeredPatient = patientDetailsToRegisterPatientResourceConverter(patientDetails);
            }
        } else {
            throw new UnprocessableEntityException("Section Case Invalid: ",
                    OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                            SystemCode.INVALID_PARAMETER, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                            SystemURL.SD_GPC_PRACTITIONER, IssueTypeEnum.NOT_FOUND));
        }

        Bundle bundle = new Bundle().setType(BundleTypeEnum.TRANSACTION_RESPONSE);
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

        List<ExtensionDt> registrationPeriodExtensions = patientResource
                .getUndeclaredExtensionsByUrl(SystemURL.SD_EXTENSION_REGISTRATION_PERIOD);
        ExtensionDt registrationPeriodExtension = registrationPeriodExtensions.get(0);
        PeriodDt registrationPeriod = (PeriodDt) registrationPeriodExtension.getValue();

        Date registrationStart = registrationPeriod.getStart();

        if (registrationStart.compareTo(new Date()) <= 1) {
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
                .getUndeclaredExtensionsByUrl(SystemURL.SD_EXTENSION_REGISTRATION_STATUS);
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
                .getUndeclaredExtensionsByUrl(SystemURL.SD_EXTENSION_REGISTRATION_TYPE);
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
        Patient patient = new Patient()
                .addIdentifier(new IdentifierDt(SystemURL.ID_NHS_NUMBER, patientDetails.getNhsNumber()))
                .setBirthDate(new DateDt(patientDetails.getDateOfBirth()))
                .setGender(AdministrativeGenderEnum.forCode(patientDetails.getGender().toLowerCase(Locale.UK)));

        patient.setId(patientDetails.getId());
        patient.addName().addFamily(patientDetails.getSurname()).addGiven(patientDetails.getForename()).setUse(NameUseEnum.USUAL);

        PeriodDt registrationPeriod = new PeriodDt()
                .setStartWithSecondsPrecision(patientDetails.getRegistrationStartDateTime())
                .setEndWithSecondsPrecision(patientDetails.getRegistrationEndDateTime());
        patient.addUndeclaredExtension(true, SystemURL.SD_EXTENSION_REGISTRATION_PERIOD, registrationPeriod);

        patient.addUndeclaredExtension(true, SystemURL.SD_EXTENSION_REGISTRATION_STATUS, new CodeableConceptDt(
                SystemURL.VS_REGISTRATION_STATUS, patientDetails.getRegistrationStatus()));

        patient.addUndeclaredExtension(true, SystemURL.SD_EXTENSION_REGISTRATION_TYPE, new CodeableConceptDt(
                SystemURL.VS_REGISTRATION_TYPE, patientDetails.getRegistrationType()));

        return patient;
    }

    private Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = new Patient();
        patient.setId(patientDetails.getId());
        patient.addIdentifier(new IdentifierDt(SystemURL.ID_NHS_NUMBER, patientDetails.getNhsNumber()));

        Date lastUpdated = patientDetails.getLastUpdated();

        if (lastUpdated != null) {
            patient.getMeta()
                    .setLastUpdated(lastUpdated)
                    .setVersionId(String.valueOf(lastUpdated.getTime()));
        }

        patient.addName()
                .setText(patientDetails.getName())
                .addFamily(patientDetails.getSurname())
                .addGiven(patientDetails.getForename())
                .addPrefix(patientDetails.getTitle())
                .setUse(NameUseEnum.USUAL);

        patient.setBirthDate(new DateDt(patientDetails.getDateOfBirth()));
        patient.getMeta().addProfile(SystemURL.SD_GPC_PATIENT);

        String addressLines = patientDetails.getAddress();

        if (addressLines != null) {
            patient.addAddress()
                    .setUse(AddressUseEnum.HOME)
                    .setType(AddressTypeEnum.PHYSICAL)
                    .setText(addressLines);
        }

        Long gpId = patientDetails.getGpId();

        if (gpId != null) {
            HumanNameDt practitionerName = practitionerResourceProvider.getPractitionerById(new IdDt(gpId)).getName();

            ResourceReferenceDt practitionerReference = new ResourceReferenceDt("Practitioner/" + gpId)
                    .setDisplay(practitionerName.getPrefixFirstRep() + " " + practitionerName.getGivenFirstRep() + " " + practitionerName.getFamilyFirstRep());

            patient.getCareProvider().add(practitionerReference);
        } else {
            throw new ResourceNotFoundException("No GP record exists",
                    OperationOutcomeFactory.buildOperationOutcome(SystemURL.VS_GPC_ERROR_WARNING_CODE,
                            SystemCode.PATIENT_NOT_FOUND, CodableConceptText.PATIENT_RECORD_NOT_FOUND,
                            SystemURL.SD_GPC_PRACTITIONER, IssueTypeEnum.NOT_FOUND));
        }

        String gender = patientDetails.getGender();
        if (gender != null) {
            patient.setGender(AdministrativeGenderEnum.forCode(gender.toLowerCase(Locale.UK)));
        }

        String telephoneNumber = patientDetails.getTelephone();
        if (telephoneNumber != null) {
            ContactPointDt telephone = new ContactPointDt()
                    .setSystem(ContactPointSystemEnum.PHONE)
                    .setValue(telephoneNumber)
                    .setUse(ContactPointUseEnum.HOME);

            patient.setTelecom(Collections.singletonList(telephone));
        }

        Date registrationStartDateTime = patientDetails.getRegistrationStartDateTime();
        if (registrationStartDateTime != null) {
            PeriodDt registrationPeriod = new PeriodDt()
                    .setStartWithSecondsPrecision(registrationStartDateTime)
                    .setEndWithSecondsPrecision(patientDetails.getRegistrationEndDateTime());

            patient.addUndeclaredExtension(true, SystemURL.SD_EXTENSION_REGISTRATION_PERIOD, registrationPeriod);
        }

        String registrationStatusValue = patientDetails.getRegistrationStatus();
        if (registrationStatusValue != null) {
            patient.addUndeclaredExtension(true, SystemURL.SD_EXTENSION_REGISTRATION_STATUS, new CodeableConceptDt(
                    SystemURL.VS_REGISTRATION_STATUS, registrationStatusValue));
        }

        String registrationTypeValue = patientDetails.getRegistrationType();
        if (registrationTypeValue != null) {
            patient.addUndeclaredExtension(true, SystemURL.SD_EXTENSION_REGISTRATION_TYPE, new CodeableConceptDt(
                    SystemURL.VS_REGISTRATION_TYPE, registrationTypeValue));
        }

        return patient;
    }
}
