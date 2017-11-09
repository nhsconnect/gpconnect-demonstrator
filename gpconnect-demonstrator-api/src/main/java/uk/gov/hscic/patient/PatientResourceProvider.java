package uk.gov.hscic.patient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import uk.gov.hscic.common.filters.model.Parameter;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Composition.CompositionStatus;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationDispense;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.common.helpers.StaticElementsHelper;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.medications.MedicationAdministrationResourceProvider;
import uk.gov.hscic.medications.MedicationDispenseResourceProvider;
import uk.gov.hscic.medications.MedicationOrderResourceProvider;
import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.model.patient.PatientSummary;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.details.PatientSearch;
import uk.gov.hscic.patient.details.PatientStore;
import uk.gov.hscic.patient.html.FhirSectionBuilder;
import uk.gov.hscic.patient.html.Page;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;
import uk.gov.hscic.util.NhsCodeValidator;

@Component
public class PatientResourceProvider implements IResourceProvider {
    private static final String REGISTER_PATIENT_OPERATION_NAME = "$gpc.registerpatient";
    private static final String GET_CARE_RECORD_OPERATION_NAME = "$gpc.getcarerecord";

    private static final String TEMPORARY_RESIDENT_REGISTRATION_TYPE = "T";
    private static final String ACTIVE_REGISTRATION_STATUS = "A";
    private static final int ENCOUNTERS_SUMMARY_LIMIT = 3;

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

    @Autowired
    private StaticElementsHelper staticElHelper;

    private NhsNumber nhsNumber;

    private Map<String, Boolean> getCareRecordParams;
    private Map<String, Boolean> registerPatientParams;

    public static Set<String> getCustomReadOperations() {
        Set<String> customReadOperations = new HashSet<String>();
        customReadOperations.add(GET_CARE_RECORD_OPERATION_NAME);

        return customReadOperations;
    }

    public static Set<String> getCustomWriteOperations() {
        Set<String> customWriteOperations = new HashSet<String>();
        customWriteOperations.add(REGISTER_PATIENT_OPERATION_NAME);

        return customWriteOperations;
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @PostConstruct
    public void postConstruct() {
        nhsNumber = new NhsNumber();

        boolean mandatory = true;
        boolean optional = false;

        getCareRecordParams = new HashMap<>();
        getCareRecordParams.put("patientNHSNumber", mandatory);
        getCareRecordParams.put("recordSection", mandatory);
        getCareRecordParams.put("timePeriod", optional);

        registerPatientParams = new HashMap<>();
        registerPatientParams.put("registerPatient", true);
    }

    @Read(version = true)
    public Patient getPatientById(@IdParam IdDt internalId) {
        PatientDetails patientDetails = patientSearch.findPatientByInternalID(internalId.getIdPart());

        if (patientDetails == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No patient details found for patient ID: " + internalId.getIdPart()),
                    SystemCode.PATIENT_NOT_FOUND, IssueType.NOTFOUND);
        }

        return IdentifierValidator.versionComparison(internalId,
                patientDetailsToPatientResourceConverter(patientDetails));
    }

    @Search
    public List<Patient> getPatientsByPatientId(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam tokenParam) {

        Patient patient = getPatientByPatientId(nhsNumber.fromToken(tokenParam));

        return null == patient || patient.getDeceased() != null ? Collections.emptyList()
                : Collections.singletonList(patient);
    }

    private Patient getPatientByPatientId(String patientId) {
        PatientDetails patientDetails = patientSearch.findPatient(patientId);

        return null == patientDetails ? null : patientDetailsToPatientResourceConverter(patientDetails);
    }

    private void validateParameterNames(Parameters parameters, Map<String, Boolean> parameterDefinitions) {
        List<String> parameterNames = parameters.getParameter().stream().map(ParametersParameterComponent::getName)
                .collect(Collectors.toList());

        Set<String> parameterDefinitionNames = parameterDefinitions.keySet();

        if (parameterNames.isEmpty() == false) {
            for (String parameterDefinition : parameterDefinitionNames) {
                boolean mandatory = parameterDefinitions.get(parameterDefinition);

                if (mandatory) {
                    if (parameterNames.contains(parameterDefinition) == false) {
                        throw OperationOutcomeFactory.buildOperationOutcomeException(
                                new InvalidRequestException("Not all mandatory parameters have been provided"),
                                SystemCode.INVALID_PARAMETER, IssueType.INVALID);
                    }
                }
            }

            if (parameterDefinitionNames.containsAll(parameterNames) == false) {
                parameterNames.removeAll(parameterDefinitionNames);
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(
                                "Unrecognised parameters have been provided - " + parameterNames.toString()),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }
        } else {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Not all mandatory parameters have been provided"),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        }
    }

    @Operation(name = GET_CARE_RECORD_OPERATION_NAME)
    public Bundle getPatientCareRecord(@ResourceParam Parameters params) throws UnsupportedDataTypeException {
        validateParameterNames(params, getCareRecordParams);

        String nhsNumber = null;
        String sectionName = null;
        Date fromDate = null;
        Date toDate = null;
        Date requestedFromDate = null;
        Date requestedToDate = null;

        for (ParametersParameterComponent param : params.getParameter()) {
            IDatatype value = (IDatatype) param.getValue();

            if (value instanceof Identifier) {
                nhsNumber = getNhsNumber(value);

                if (StringUtils.isBlank(nhsNumber) || !NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("NHS number Invalid"), SystemCode.INVALID_NHS_NUMBER,
                            IssueType.INVALID);
                }

                PatientSummary patientSummary = patientSearch.findPatientSummary(nhsNumber);

                if (null == patientSummary) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new ResourceNotFoundException("No patient details found for patient ID: " + nhsNumber),
                            SystemCode.PATIENT_NOT_FOUND, IssueType.NOTFOUND);
                }

                if (patientSummary.isSensitive()) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new ForbiddenOperationException("No patient consent"), SystemCode.NO_PATIENT_CONSENT,
                            IssueType.NOTFOUND);
                }
            } else if (value instanceof CodeableConcept) {
                if (null != sectionName) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Section name set twice!"),
                            SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueType.INVALID);
                }

                Coding coding = ((CodeableConcept) value).getCodingFirstRep();

                String system = coding.getSystem();
                sectionName = coding.getCode();

                if (system == null || sectionName == null) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("System is null"), SystemCode.INVALID_PARAMETER,
                            IssueType.INVALID);
                }

                if (!sectionName.equals(sectionName.toUpperCase(Locale.UK))) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("Section Case Invalid: " + sectionName),
                            SystemCode.INVALID_PARAMETER, IssueType.INVALID);
                }

                if (!system.equals(SystemURL.VS_GPC_RECORD_SECTION)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Provided system, " + system + " is not the expected "
                                    + SystemURL.VS_GPC_RECORD_SECTION),
                            SystemCode.INVALID_PARAMETER, IssueType.INVALID);
                }
            } else if (value instanceof Period) {
                Period period = (Period) value;

                fromDate = period.getStart();
                toDate = period.getEnd();
                requestedFromDate = period.getStart();
                requestedToDate = period.getEnd();
                if (requestedFromDate != null && fromDate.toString().equals("Thu Jan 01 00:00:00 GMT 1970")
                        && requestedToDate != null && toDate.toString().equals("Thu Jan 01 00:00:00 GMT 1970")) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid datatype"), SystemCode.INVALID_PARAMETER,
                            IssueType.INVALID);
                } else if (requestedToDate != null && toDate.toString().equals("Thu Jan 01 00:00:00 GMT 1970")) {
                    toDate = null;
                    requestedToDate = null;

                } else if (requestedFromDate != null && fromDate.toString().equals("Thu Jan 01 00:00:00 GMT 1970")) {
                    requestedFromDate = null;
                    fromDate = null;

                }

                if (fromDate != null && toDate != null && fromDate.after(toDate)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("Dates are invalid: " + fromDate + ", " + toDate),
                            SystemCode.INVALID_PARAMETER, IssueType.INVALID);
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
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid datatype"), SystemCode.INVALID_PARAMETER,
                        IssueType.INVALID);
            }
        }

        // Build requested section
        Page page;

        switch (sectionName) {
        case "SUM":
            page = new Page("Summary", sectionName);
            page.addPageSection(
                    pageSectionFactory.getPRBActivePageSection(nhsNumber, requestedFromDate, requestedToDate));
            page.addPageSection(
                    pageSectionFactory.getMEDCurrentPageSection(nhsNumber, requestedFromDate, requestedToDate));
            page.addPageSection(
                    pageSectionFactory.getMEDRepeatPageSection(nhsNumber, requestedFromDate, requestedToDate));
            page.addPageSection(pageSectionFactory.getALLCurrentPageSection(nhsNumber, fromDate, toDate,
                    requestedFromDate, requestedToDate));
            page.addPageSection(pageSectionFactory.getENCPageSection("Last 3 Encounters", nhsNumber, fromDate, toDate,
                    requestedFromDate, requestedToDate, ENCOUNTERS_SUMMARY_LIMIT));

            break;

        case "PRB":
            page = new Page("Problems", sectionName);
            page.addPageSection(
                    pageSectionFactory.getPRBActivePageSection(nhsNumber, requestedFromDate, requestedToDate));
            page.addPageSection(
                    pageSectionFactory.getPRBInctivePageSection(nhsNumber, requestedFromDate, requestedToDate));

            break;

        case "ENC":
            page = new Page("Encounters", sectionName);
            page.addPageSection(pageSectionFactory.getENCPageSection("Encounters", nhsNumber, fromDate, toDate,
                    requestedFromDate, requestedToDate, Integer.MAX_VALUE));

            break;

        case "ALL":
            page = new Page("Allergies and Adverse Reactions", sectionName);
            page.addPageSection(pageSectionFactory.getALLCurrentPageSection(nhsNumber, fromDate, toDate,
                    requestedFromDate, requestedToDate));
            page.addPageSection(pageSectionFactory.getALLHistoricalPageSection(nhsNumber, fromDate, toDate,
                    requestedFromDate, requestedToDate));

            break;

        case "CLI":
            page = new Page("Clinical Items", sectionName);
            page.addPageSection(pageSectionFactory.getCLIPageSection(nhsNumber, fromDate, toDate, requestedFromDate,
                    requestedToDate));

            break;

        case "MED":
            page = new Page("Medications", sectionName);
            page.addPageSection(
                    pageSectionFactory.getMEDCurrentPageSection(nhsNumber, requestedFromDate, requestedToDate));
            page.addPageSection(
                    pageSectionFactory.getMEDRepeatPageSection(nhsNumber, requestedFromDate, requestedToDate));
            page.addPageSection(
                    pageSectionFactory.getMEDPastPageSection(nhsNumber, requestedFromDate, requestedToDate));

            break;

        case "REF":
            page = new Page("Referrals", sectionName);
            page.addPageSection(pageSectionFactory.getREFPageSection(nhsNumber, fromDate, toDate, requestedFromDate,
                    requestedToDate));

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
            page.addPageSection(pageSectionFactory.getIMMPageSection(nhsNumber, fromDate, toDate, requestedFromDate,
                    requestedToDate));

            break;

        case "ADM":
            page = new Page("Administrative Items", sectionName);
            page.addPageSection(pageSectionFactory.getADMPageSection(nhsNumber, fromDate, toDate, requestedFromDate,
                    requestedToDate));

            break;

        default:
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Invalid section code: " + sectionName),
                    SystemCode.INVALID_PARAMETER, IssueType.INVALID);
        }

        // Build the Patient Resource and add it to the bundle
        Patient patient = getPatientByPatientId(nhsNumber);

        if (null == patient) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No patient details found for patient ID: " + nhsNumber),
                    SystemCode.PATIENT_NOT_FOUND, IssueType.INVALID);
        }

        String patientId = patient.getId();

        BundleEntryComponent patientEntry = new BundleEntryComponent().setResource(patient).setFullUrl("Patient/" + patientId);

        Coding coding = new Coding().setSystem("valueSet").setCode("425173008")
                .setDisplay("record extract (record artifact)");

        CodeableConcept codableConcept = new CodeableConcept().addCoding(coding)
                .setText("record extract (record artifact)");

        Composition careRecordComposition = new Composition().setDate(Calendar.getInstance().getTime())
                .setTitle("Patient Care Record").setType(codableConcept).setStatus(CompositionStatus.FINAL)
                .setSubject(new Reference("Patient/" + patientId));

        careRecordComposition.getMeta().addProfile(SystemURL.SD_GPC_CARERECORD_COMPOSITION);

        //careRecordComposition.setSection(Collections.singletonList(FhirSectionBuilder.buildFhirSection(page)));

        // Build the Care Record Composition
        Bundle bundle = new Bundle().setType(BundleType.TRANSACTIONRESPONSE)
                .addEntry(new BundleEntryComponent().setResource(careRecordComposition));

        List<Reference> careProviderPractitionerList = null;
                
               // ((Patient) patientEntry.getResource()).getCareProvider();

        if (!careProviderPractitionerList.isEmpty()) {
            String id = careProviderPractitionerList.get(0).getReference();
            careRecordComposition.setAuthor(Collections.singletonList(new Reference(id)));

            Practitioner practitioner = practitionerResourceProvider.getPractitionerById(new IdDt(id));

            if (practitioner == null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new ResourceNotFoundException("Practitioner Reference returning null"),
                        SystemCode.REFERENCE_NOT_FOUND, IssueType.INVALID);
            }

            bundle.addEntry(new BundleEntryComponent().setResource(practitioner).setFullUrl(id));

//            IdDt organizationId = practitioner
//                    
//                  ///  getPractitionerRoleFirstRep().getManagingOrganization().getReference();
//
//            BundleEntryComponent organizationEntry = new BundleEntryComponent()
//                    .setResource(organizationResourceProvider.getOrganizationById(organizationId))
//                    .setFullUrl(organizationId.toString());
//
//            if (organizationEntry.getResource() == null || organizationEntry.getFullUrl() == null) {
//                throw OperationOutcomeFactory.buildOperationOutcomeException(
//                        new ResourceNotFoundException("organizationResource returning null"),
//                        SystemCode.REFERENCE_NOT_FOUND, IssueType.INVALID);
//            }
//
//            bundle.addEntry(organizationEntry);
        }

        return bundle.addEntry(patientEntry);
    }

    @Search(compartmentName = "MedicationOrder")
    public List<MedicationRequest> getPatientMedicationOrders(@IdParam IdDt patientLocalId) {
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
    public List<Appointment> getPatientAppointments(@IdParam IdDt patientLocalId, @Sort SortSpec sort,
            @Count Integer count, @OptionalParam(name = "start") DateRangeParam startDate) {
        return appointmentResourceProvider.getAppointmentsForPatientIdAndDates(patientLocalId, sort, count, startDate);
    }

    @Operation(name = REGISTER_PATIENT_OPERATION_NAME)
    public Bundle registerPatient(@ResourceParam Parameters params) {
        Patient registeredPatient = null;

        validateParameterNames(params, registerPatientParams);

        Patient unregisteredPatient = null;
//                params.getParameter().stream()
//                .filter(param -> "registerPatient".equalsIgnoreCase(param.getName())).map(Parameter::getResource)
//                .map(Patient.class::cast).findFirst().orElse(null);

        if (unregisteredPatient != null) {
            validatePatient(unregisteredPatient);

            // check if the patient already exists

            PatientDetails patientDetails = patientSearch
                    .findPatient(nhsNumber.fromPatientResource(unregisteredPatient));

            if (patientDetails == null || IsInactiveTemporaryPatient(patientDetails)) {

                if (patientDetails == null) {
                    patientDetails = registerPatientResourceConverterToPatientDetail(unregisteredPatient);
                    patientStore.create(patientDetails);
                } else {
                    patientDetails.setRegistrationStatus(ACTIVE_REGISTRATION_STATUS);
                    patientStore.update(patientDetails);
                }
                registeredPatient = patientDetailsToRegisterPatientResourceConverter(
                        patientSearch.findPatient(unregisteredPatient.getIdentifierFirstRep().getValue()));
            } else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(String.format("Patient (NHS number - %s) already exists",
                                nhsNumber.fromPatientResource(unregisteredPatient))),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }
        } else {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Patient record not found"), SystemCode.INVALID_PARAMETER,
                    IssueType.INVALID);
        }

        Bundle bundle = new Bundle().setType(BundleType.SEARCHSET);
        bundle.getMeta().addProfile(SystemURL.SD_GPC_SRCHSET_BUNDLE);
        bundle.addEntry().setResource(registeredPatient);
        return bundle;
    }

    private Boolean IsInactiveTemporaryPatient(PatientDetails patientDetails) {

        return patientDetails.getRegistrationType() != null
                && TEMPORARY_RESIDENT_REGISTRATION_TYPE.equals(patientDetails.getRegistrationType())
                && patientDetails.getRegistrationStatus() != null
                && ACTIVE_REGISTRATION_STATUS.equals(patientDetails.getRegistrationStatus()) == false;
    }

    public String getNhsNumber(Object source) {
        return nhsNumber.getNhsNumber(source);
    }

    private void validatePatient(Patient patient) {
        validateIdentifiers(patient);
        validateRegistrationDetails(patient);
        validateConstrainedOutProperties(patient);
        checkValidExtensions(patient.getExtension());
        validateNames(patient);
        validateDateOfBirth(patient);
        valiateGender(patient);
    }

    private void validateRegistrationDetails(Patient patient) {

        List<Extension> registrationPeriodExtensions = patient
                .getExtensionsByUrl(SystemURL.SD_EXTENSION_CC_REG_DETAILS);

        if (registrationPeriodExtensions.size() > 0) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(
                            "An extension of type registrationDetails is invalid for a registration request."),
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }
    }

    private void valiateGender(Patient patient) {
        AdministrativeGender gender = patient.getGender();

        if (gender != null) {

            EnumSet<AdministrativeGender> genderList = EnumSet.allOf(AdministrativeGender.class);
            Boolean valid = false;
            for (AdministrativeGender genderItem : genderList) {

        
                if (genderItem.toCode().equalsIgnoreCase(gender.toString())) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(
                                String.format("The supplied Patient gender %s is an unrecognised type.", gender)),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }
        }
    }

    private void validateDateOfBirth(Patient patient) {
        Date birthDate = patient.getBirthDate();

        if (birthDate == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The Patient date of birth must be supplied"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }
    }

    private void validateIdentifiers(Patient patient) {
        List<Identifier> identifiers = patient.getIdentifier();
        if (identifiers.isEmpty() == false) {
            boolean identifiersValid = identifiers.stream()
                    .allMatch(identifier -> identifier.getSystem() != null && identifier.getValue() != null);

            if (identifiersValid == false) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(
                                "One or both of the system and/or value on some of the provided identifiers is null"),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }
        } else {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("At least one identifier must be supplied on a Patient resource"),
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }
    }

    private void checkValidExtensions(List<Extension> undeclaredExtensions) {

        List<String> extensionURLs = undeclaredExtensions.stream().map(Extension::getUrl)
                .collect(Collectors.toList());

        extensionURLs.remove(SystemURL.SD_EXTENSION_CC_REG_DETAILS);
        extensionURLs.remove(SystemURL.SD_CC_EXT_ETHNIC_CATEGORY);
        extensionURLs.remove(SystemURL.SD_CC_EXT_RELIGIOUS_AFFILI);
        extensionURLs.remove(SystemURL.SD_PATIENT_CADAVERIC_DON);
        extensionURLs.remove(SystemURL.SD_CC_EXT_RESIDENTIAL_STATUS);
        extensionURLs.remove(SystemURL.SD_CC_EXT_TREATMENT_CAT);
        extensionURLs.remove(SystemURL.SD_CC_EXT_NHS_COMMUNICATION);

        if (!extensionURLs.isEmpty()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(
                            "Invalid/multiple patient extensions found. The following are in excess or invalid: "
                                    + extensionURLs.stream().collect(Collectors.joining(", "))),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }
    }

    private void validateConstrainedOutProperties(Patient patient) {

        Set<String> invalidFields = new HashSet<String>();

        // ## The above can exist in the patient resource but can be ignored. If
        // they are saved by the provider then they should be returned in the
        // response!

        if (patient.getPhoto().isEmpty() == false)
            invalidFields.add("photo");
        if (patient.getAnimal().isEmpty() == false)
            invalidFields.add("animal");
        if (patient.getCommunication().isEmpty() == false)
            invalidFields.add("communication");
        if (patient.getLink().isEmpty() == false)
            invalidFields.add("link");
        if (patient.getDeceased() != null)
            invalidFields.add("deceased");

        if (invalidFields.isEmpty() == false) {
            String message = String.format(
                    "The following properties have been constrained out on the Patient resource - %s",
                    String.join(", ", invalidFields));
            throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(message),
                    SystemCode.BAD_REQUEST, IssueType.NOTSUPPORTED);
        }
    }

    private void validateNames(Patient patient) {
        List<HumanName> names = patient.getName();

        if (names.size() < 1) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The patient must have at least one Name."), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        List<HumanName> usualActiveNames = names.stream().filter(nm -> IsActiveName(nm))
                .filter(nm -> NameUse.USUAL.toCode().equals(nm.getUse())).collect(Collectors.toList());

        if (usualActiveNames.size() != 1) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The patient must have one Active Name with a Use of USUAL"),
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }

        HumanName name = names.iterator().next();
        //validateNameCount(name.getFamily(), "family");
    }

  /*  private void validateNameCount(List<?> names, String nameType) {
        if (names.size() != 1) {
            String message = String.format("The patient can only have one %s name property. Found %s", nameType,
                    names.size());
            throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(message),
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }
    }*/

    private Boolean IsActiveName(HumanName name) {

        Period period = name.getPeriod();

        if (null == period) {
            return true;
        }

        Date start = period.getStart();
        Date end = period.getEnd();

        if ((null == end || end.after(new Date()))
                && (null == start || start.equals(new Date()) || start.before(new Date()))) {
            return true;
        }

        return false;
    }

    private PatientDetails registerPatientResourceConverterToPatientDetail(Patient patientResource) {
        PatientDetails patientDetails = new PatientDetails();
        HumanName name = patientResource.getNameFirstRep();

        String givenNames = name.getGiven().stream().map(n -> n.getValue()).collect(Collectors.joining(","));

        patientDetails.setForename(givenNames);

        patientDetails.setSurname(name.getGivenAsSingleString());
        patientDetails.setDateOfBirth(patientResource.getBirthDate());
        //patientDetails.setGender(patientResource.getGender());
        patientDetails.setNhsNumber(patientResource.getIdentifierFirstRep().getValue());

        Type multipleBirth = patientResource.getMultipleBirth();
        if (multipleBirth != null) {
            try {
                patientDetails.setMultipleBirth(((ContactPoint) multipleBirth).getValue());
            } catch (ClassCastException cce) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException("The multiple birth property is expected to be a boolean"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }
        }

        Type deceased =  patientResource.getDeceased();
        if (deceased != null) {
            try {
                patientDetails.setDeceased(((ContactPoint) deceased).getValue());
            } catch (ClassCastException cce) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException("The multiple deceased property is expected to be a datetime"),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }
        }

        patientDetails.setRegistrationStartDateTime(new Date());
        // patientDetails.setRegistrationEndDateTime(getRegistrationEndDate(patientResource));
        patientDetails.setRegistrationStatus(ACTIVE_REGISTRATION_STATUS);
        patientDetails.setRegistrationType(TEMPORARY_RESIDENT_REGISTRATION_TYPE);

        return patientDetails;
    }

    // a cut-down Patient
    private Patient patientDetailsToRegisterPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = patientDetailsToMinimalPatient(patientDetails);

        HumanName name = getPatientNameFromPatientDetails(patientDetails);

        patient.addName(name);

        patient = setStaticPatientData(patient);

        return patient;
    }

    private Patient setStaticPatientData(Patient patient) {

        patient.setLanguage(("en-GB"));

        patient.addExtension(createCodingExtension("CG", "Greek Cypriot", SystemURL.CS_CC_ETHNIC_CATEGORY,
                SystemURL.SD_CC_EXT_ETHNIC_CATEGORY));
        patient.addExtension(createCodingExtension("SomeSnomedCode", "Some Snomed Code",
                SystemURL.CS_CC_RELIGIOUS_AFFILI, SystemURL.SD_CC_EXT_RELIGIOUS_AFFILI));
        patient.addExtension(new Extension(SystemURL.SD_PATIENT_CADAVERIC_DON, new BooleanDt(false)));
        patient.addExtension(createCodingExtension("H", "UK Resident", SystemURL.CS_CC_RESIDENTIAL_STATUS,
                SystemURL.SD_CC_EXT_RESIDENTIAL_STATUS));
        patient.addExtension(createCodingExtension("3", "To pay hotel fees only", SystemURL.CS_CC_TREATMENT_CAT,
                SystemURL.SD_CC_EXT_TREATMENT_CAT));

        Extension nhsCommExtension = new Extension();
        nhsCommExtension.setUrl(SystemURL.SD_CC_EXT_NHS_COMMUNICATION);
        nhsCommExtension.addExtension(
                createCodingExtension("en", "English", SystemURL.CS_CC_HUMAN_LANG, SystemURL.SD_CC_EXT_COMM_LANGUAGE));
        nhsCommExtension
                .addExtension(new Extension(SystemURL.SD_CC_COMM_PREFERRED, new BooleanDt(false)));
        nhsCommExtension.addExtension(createCodingExtension("RWR", "Received written",
                SystemURL.CS_CC_LANG_ABILITY_MODE, SystemURL.SD_CC_MODE_OF_COMM));
        nhsCommExtension.addExtension(createCodingExtension("E", "Excellent",
                SystemURL.CS_CC_LANG_ABILITY_PROFI, SystemURL.SD_CC_COMM_PROFICIENCY));
        nhsCommExtension.addExtension(
                new Extension(SystemURL.SD_CC_INTERPRETER_REQUIRED, new BooleanDt(false)));

        patient.addExtension(nhsCommExtension);

        Identifier localIdentifier = new Identifier();
        localIdentifier.setUse(IdentifierUse.USUAL);
        localIdentifier.setSystem(SystemURL.ID_LOCAL_PATIENT_IDENTIFIER);
        localIdentifier.setValue("123456");

        CodeableConcept liType = new CodeableConcept();
        Coding liTypeCoding = new Coding();
        liTypeCoding.setCode("EN");
        liTypeCoding.setDisplay("Employer number");
        liTypeCoding.setSystem(SystemURL.VS_IDENTIFIER_TYPE);
        liType.addCoding(liTypeCoding);
        localIdentifier.setType(liType);

        localIdentifier.setAssigner(new Reference("Organization/1"));
        patient.addIdentifier(localIdentifier);

        Calendar calendar = Calendar.getInstance();

        calendar.set(2017, 1, 1);
        DateTimeDt endDate = new DateTimeDt(calendar.getTime());

        calendar.set(2016, 1, 1);
        DateTimeDt startDate = new DateTimeDt(calendar.getTime());

        Period pastPeriod = new Period().setStart(calendar.getTime()).setEnd(calendar.getTime());

        patient.addName().setFamily("AnotherUsualFamilyName").addGiven("AnotherUsualGivenName").setUse(NameUse.USUAL)
                .setPeriod(pastPeriod);

        patient.addName().setFamily("AdditionalFamily").addGiven("AdditionalGiven").setUse(NameUse.TEMP);

        patient.addTelecom(staticElHelper.getValidTelecom());
        patient.addAddress(staticElHelper.getValidAddress());
       // patient.setContact(getValidContact());

        return patient;
    }

    private ContactDetail getValidContact() {

        HumanName ctName = new HumanName();
        ctName.setUse(NameUse.USUAL);
        ctName.setFamily("FamilyName");

        List<CodeableConcept> ctRelList = new ArrayList<>();
        ctRelList.add(createCoding("family", "Family", SystemURL.VS_PATIENT_CONTACT_REL));

        ContactDetail contact = new ContactDetail();
        contact.setName(ctName.toString());
        contact.addTelecom(staticElHelper.getValidTelecom());
     //   contact.setAddress(staticElHelper.getValidAddress().toString());
      //  contact.setRelationship(ctRelList.toString());
       // contact.setGender(AdministrativeGender.FEMALE);

        return contact;
    }

    private Extension createCodingExtension(String code, String display, String vsSystem, String extSystem) {

        Extension ext = new Extension(extSystem, createCoding(code, display, vsSystem));

        return ext;
    }

    private CodeableConcept createCoding(String code, String display, String vsSystem) {

        Coding coding = new Coding();
        coding.setCode(code);
        coding.setDisplay(display);
        coding.setSystem(vsSystem);
        CodeableConcept concept = new CodeableConcept();
        concept.addCoding(coding);

        return concept;
    }

    private Patient patientDetailsToMinimalPatient(PatientDetails patientDetails) {
        Patient patient = new Patient();

        String versionId;
        Date lastUpdated = patientDetails.getLastUpdated();
        if (lastUpdated == null) {
            versionId = String.valueOf(new Date().getTime());

            patient.setId(patientDetails.getId());
        } else {
            versionId = String.valueOf(lastUpdated.getTime());

          //  patient.setId(new IdDt(patient.getResourceName(), patientDetails.getId(), versionId));
            patient.getMeta().setLastUpdated(lastUpdated);
        }

        patient.getMeta().addProfile(SystemURL.SD_GPC_PATIENT);
        patient.getMeta().setVersionId(versionId);
        Identifier patientNhsNumber = new Identifier().setSystem(SystemURL.ID_NHS_NUMBER).setValue(patientDetails.getNhsNumber());
        patientNhsNumber.addExtension(createCodingExtension("01", "Number present and verified",
                SystemURL.CS_CC_NHS_NUMBER_VERIF, SystemURL.SD_CC_EXT_NHS_NUMBER_VERIF));
        patient.addIdentifier(patientNhsNumber);

        patient.setBirthDate(patientDetails.getDateOfBirth());

        String gender = patientDetails.getGender();
        if (gender != null) {
           // patient.setGender(AdministrativeGender.fromCode(gender.toLowerCase(Locale.UK)));
        }

        Date registrationEndDateTime = patientDetails.getRegistrationEndDateTime();
        Date registrationStartDateTime = patientDetails.getRegistrationStartDateTime();

        Extension regDetailsExtension = new Extension(SystemURL.SD_EXTENSION_CC_REG_DETAILS);

        Period registrationPeriod = new Period().setStart(registrationStartDateTime)
                .setEnd(registrationEndDateTime);

        Extension regPeriodExt = new Extension(SystemURL.SD_CC_EXT_REGISTRATION_PERIOD, registrationPeriod);
        regDetailsExtension.addExtension(regPeriodExt);

        String registrationStatusValue = patientDetails.getRegistrationStatus();
        patient.setActive(
                ACTIVE_REGISTRATION_STATUS.equals(registrationStatusValue) || null == registrationStatusValue);

        String registrationTypeValue = patientDetails.getRegistrationType();
        if (registrationTypeValue != null) {

            Coding regTypeCode = new Coding();
            regTypeCode.setCode(registrationTypeValue);
            regTypeCode.setDisplay("Temporary"); // Should always be Temporary
            regTypeCode.setSystem(SystemURL.CS_REGISTRATION_TYPE);
            CodeableConcept regTypeConcept = new CodeableConcept();
            regTypeConcept.addCoding(regTypeCode);

            Extension regTypeExt = new Extension(SystemURL.SD_CC_EXT_REGISTRATION_TYPE, regTypeConcept);
            regDetailsExtension.addExtension(regTypeExt);
        }

        patient.addExtension(regDetailsExtension);

        String maritalStatus = patientDetails.getMaritalStatus();
        if (maritalStatus != null) {
            CodeableConcept marital = new CodeableConcept();
            Coding maritalCoding = new Coding();
            maritalCoding.setSystem(SystemURL.VS_CC_MARITAL_STATUS);
            maritalCoding.setCode(patientDetails.getMaritalStatus());
            maritalCoding.setDisplay("Married");
            marital.addCoding(maritalCoding);

            patient.setMaritalStatus(marital);
        }

      //  patient.setMultipleBirth(patientDetails.isMultipleBirth());

        if (patientDetails.isDeceased()) {
           // patient.setDeceased(new DateTimeDt(patientDetails.getDeceased()));
        }

        return patient;
    }

    private Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = patientDetailsToMinimalPatient(patientDetails);

        HumanName name = getPatientNameFromPatientDetails(patientDetails);

        patient.addName(name);

        String addressLines = patientDetails.getAddress();
        if (addressLines != null) {
            patient.addAddress().setUse(AddressUse.HOME).setType(AddressType.PHYSICAL).setText(addressLines);
        }

        Long gpId = patientDetails.getGpId();
        if (gpId != null) {
            HumanName practitionerName = (HumanName) practitionerResourceProvider.getPractitionerById(new IdDt(gpId)).getName();

            Reference practitionerReference = new Reference("Practitioner/" + gpId)
                    .setDisplay(practitionerName.getPrefix() + " " + practitionerName.getGivenAsSingleString() + " "
                            + practitionerName.getFamily());

           // patient.getCareProvider().add(practitionerReference);
        }

        String telephoneNumber = patientDetails.getTelephone();
        if (telephoneNumber != null) {
            ContactPoint telephone = new ContactPoint().setSystem(ContactPointSystem.PHONE).setValue(telephoneNumber)
                    .setUse(ContactPointUse.HOME);

            patient.setTelecom(Collections.singletonList(telephone));
        }

        String managingOrganization = patientDetails.getManagingOrganization();

        if (managingOrganization != null) {
            patient.setManagingOrganization(new Reference("Organization/" + managingOrganization));
        }

        return patient;
    }

    private HumanName getPatientNameFromPatientDetails(PatientDetails patientDetails) {
        HumanName name = new HumanName();

        name.setText(patientDetails.getName()).setFamily(patientDetails.getSurname())
                .addPrefix(patientDetails.getTitle()).setUse(NameUse.USUAL);

        List<String> givenNames = patientDetails.getForenames();

        givenNames.forEach((givenName) -> {
            name.addGiven(givenName);
        });

        return name;
    }

    private class NhsNumber {

        private NhsNumber() {
            super();
        }

        private String getNhsNumber(Object source) {
            String nhsNumber = fromIdDt(source);

            if (nhsNumber == null) {
                nhsNumber = fromToken(source);

                if (nhsNumber == null) {
                    nhsNumber = fromParameters(source);

                    if (nhsNumber == null) {
                        nhsNumber = fromIdentifier(source);
                    }
                }
            }

            if (nhsNumber != null && !NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid NHS number submitted: " + nhsNumber),
                        SystemCode.INVALID_NHS_NUMBER, IssueType.INVALID);
            }

            return nhsNumber;
        }

        private String fromIdentifier(Object source) {
            String nhsNumber = null;

            if (source instanceof Identifier) {
                Identifier Identifier = (Identifier) source;

                String identifierSystem = Identifier.getSystem();
                if (identifierSystem != null && SystemURL.ID_NHS_NUMBER.equals(identifierSystem)) {
                    nhsNumber = Identifier.getValue();
                } else {
                    String message = String.format(
                            "The given identifier system code (%s) does not match the expected code - %s",
                            identifierSystem, SystemURL.ID_NHS_NUMBER);

                    throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(message),
                            SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueType.INVALID);
                }
            }

            return nhsNumber;
        }

        private String fromIdDt(Object source) {
            String nhsNumber = null;

            if (source instanceof IdDt) {
                IdDt idDt = (IdDt) source;

                PatientDetails patientDetails = patientSearch.findPatientByInternalID(idDt.getIdPart());
                if (patientDetails != null) {
                    nhsNumber = patientDetails.getNhsNumber();
                }
            }

            return nhsNumber;
        }

        private String fromToken(Object source) {
            String nhsNumber = null;

            if (source instanceof TokenParam) {
                TokenParam tokenParam = (TokenParam) source;

                if (!SystemURL.ID_NHS_NUMBER.equals(tokenParam.getSystem())) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid system code"), SystemCode.INVALID_PARAMETER,
                            IssueType.INVALID);
                }

                nhsNumber = tokenParam.getValue();
            }

            return nhsNumber;
        }

        private String fromParameters(Object source) {
            String nhsNumber = null;

            if (source instanceof Parameters) {
                Parameters parameters = (Parameters) source;
                List<Parameter> params = new ArrayList<Parameter>();
                //params.addAll(parameters.getParameter());
                Parameter parameter = getParameterByName(params, "patientNHSNumber");
                if (parameter != null) {
                    //nhsNumber = fromIdentifier(parameter.getValue());
                } else {
                    //parameter = getParameterByName(parameters.getParameter(), "registerPatient");
                    if (parameter != null) {
                       // nhsNumber = fromPatientResource(parameter.getResource());
                    } else {
                        throw OperationOutcomeFactory.buildOperationOutcomeException(
                                new InvalidRequestException(
                                        "Unable to read parameters. Expecting one of patientNHSNumber or registerPatient both of which are case-sensitive"),
                                SystemCode.INVALID_PARAMETER, IssueType.INVALID);
                    }
                }
            }

            return nhsNumber;
        }

        private String fromPatientResource(Object source) {
            String nhsNumber = null;

            if (source instanceof Patient) {
                Patient patient = (Patient) source;

                nhsNumber = patient.getIdentifierFirstRep().getValue();
            }

            return nhsNumber;
        }

        private Parameter getParameterByName(List<Parameter> parameters, String parameterName) {
            Parameter parameter = null;

            List<Parameter> filteredParameters = parameters.stream()
                    .filter(currentParameter -> parameterName.equals(currentParameter.getName()))
                    .collect(Collectors.toList());

            if (filteredParameters != null) {
                if (filteredParameters.size() == 1) {
                    parameter = filteredParameters.iterator().next();
                } else if (filteredParameters.size() > 1) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException(
                                    "The parameter " + parameterName + " cannot be set more than once"),
                            SystemCode.BAD_REQUEST, IssueType.INVALID);
                }
            }

            return parameter;
        }
    }
}
