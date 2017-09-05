package uk.gov.hscic.patient;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt;
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
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.activation.UnsupportedDataTypeException;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
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

    //private static final List<String> MANDATORY_PARAM_NAMES = Arrays.asList("patientNHSNumber", "recordSection");
    //private static final List<String> OPTIONAL_PARAM_NAMES = Arrays.asList("timePeriod");

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
                    SystemCode.PATIENT_NOT_FOUND, IssueTypeEnum.NOT_FOUND);
        }

        return IdentifierValidator.versionComparison(internalId, patientDetailsToPatientResourceConverter(patientDetails));
    }

    @Search
    public List<Patient> getPatientsByPatientId(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam tokenParam) {
//        if (!SystemURL.ID_NHS_NUMBER.equals(tokenParam.getSystem())) {
//            throw OperationOutcomeFactory.buildOperationOutcomeException(
//                new InvalidRequestException("Invalid system code"),
//                SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
//        }

        Patient patient = getPatientByPatientId(nhsNumber.fromToken(tokenParam));

        return null == patient
                ? Collections.emptyList()
                : Collections.singletonList(patient);
    }

    private Patient getPatientByPatientId(String patientId) {
        PatientDetails patientDetails = patientSearch.findPatient(patientId);

        return null == patientDetails
                ? null
                : patientDetailsToPatientResourceConverter(patientDetails);
    }

    private void validateParameterNames(Parameters parameters, Map<String, Boolean> parameterDefinitions) {
       List<String> parameterNames = parameters.getParameter()
                                                    .stream()
                                                    .map(Parameter::getName)
                                                    .collect(Collectors.toList());

       Set<String> parameterDefinitionNames = parameterDefinitions.keySet();

       if(parameterNames.isEmpty() == false) {
           for(String parameterDefinition : parameterDefinitionNames) {
               boolean mandatory = parameterDefinitions.get(parameterDefinition);
    
               if(mandatory) {
                   if(parameterNames.contains(parameterDefinition) == false) {
                       throw OperationOutcomeFactory.buildOperationOutcomeException(
                               new InvalidRequestException("Not all mandatory parameters have been provided"),
                               SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
                   }
               }
           }
    
           if(parameterDefinitionNames.containsAll(parameterNames) == false) {
               parameterNames.removeAll(parameterDefinitionNames);
               throw OperationOutcomeFactory.buildOperationOutcomeException(
                       new InvalidRequestException("Unrecognised parameters have been provided - " + parameterNames.toString()),
                       SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
           }
       }
       else {
           throw OperationOutcomeFactory.buildOperationOutcomeException(
                   new InvalidRequestException("Not all mandatory parameters have been provided"),
                   SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT); 
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

        for (Parameter param : params.getParameter()) {
            IDatatype value = param.getValue();

            if (value instanceof IdentifierDt) {
                nhsNumber = getNhsNumber(value);

                if (StringUtils.isBlank(nhsNumber) || !NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("NHS number Invalid"),
                            SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
                }

                PatientSummary patientSummary = patientSearch.findPatientSummary(nhsNumber);

                if (null == patientSummary) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new ResourceNotFoundException("No patient details found for patient ID: " + nhsNumber),
                            SystemCode.PATIENT_NOT_FOUND, IssueTypeEnum.NOT_FOUND);
                }

                if (patientSummary.isSensitive()) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new ForbiddenOperationException("No patient consent"),
                            SystemCode.NO_PATIENT_CONSENT, IssueTypeEnum.FORBIDDEN);
                }
            } else if (value instanceof CodeableConceptDt) {
                if (null != sectionName) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Section name set twice!"),
                            SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueTypeEnum.INVALID_CONTENT);
                }

                CodingDt coding = ((CodeableConceptDt) value).getCodingFirstRep();

                String system = coding.getSystem();
                sectionName = coding.getCode();

                if (system == null || sectionName == null) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("System is null"),
                            SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
                }

                if (!sectionName.equals(sectionName.toUpperCase(Locale.UK))) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("Section Case Invalid: " + sectionName),
                            SystemCode.INVALID_PARAMETER, IssueTypeEnum.NOT_FOUND);
                }

                if (!system.equals(SystemURL.VS_GPC_RECORD_SECTION)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Provided system, " + system + " is not the expected " + SystemURL.VS_GPC_RECORD_SECTION),
                            SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
                }
            } else if (value instanceof PeriodDt) {
                PeriodDt period = (PeriodDt) value;

                fromDate = period.getStart();
                toDate = period.getEnd();
                requestedFromDate = period.getStart();
                requestedToDate = period.getEnd();

                if (fromDate != null && toDate != null && fromDate.after(toDate)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new UnprocessableEntityException("Dates are invalid: " + fromDate + ", " + toDate),
                            SystemCode.INVALID_PARAMETER, IssueTypeEnum.NOT_FOUND);
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
                        new InvalidRequestException("Invalid datatype"),
                        SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
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
                page.addPageSection(pageSectionFactory.getENCPageSection("Last 3 Encounters", nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate, ENCOUNTERS_SUMMARY_LIMIT));

                break;

            case "PRB":
                page = new Page("Problems", sectionName);
                page.addPageSection(pageSectionFactory.getPRBActivePageSection(nhsNumber, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getPRBInctivePageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "ENC":
                page = new Page("Encounters", sectionName);
                page.addPageSection(pageSectionFactory.getENCPageSection("Encounters", nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate, Integer.MAX_VALUE));

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
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException("Invalid section code: " + sectionName),
                        SystemCode.INVALID_PARAMETER, IssueTypeEnum.NOT_FOUND);
        }

        // Build the Patient Resource and add it to the bundle
        Patient patient = getPatientByPatientId(nhsNumber);

        if (null == patient) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No patient details found for patient ID: " + nhsNumber),
                    SystemCode.PATIENT_NOT_FOUND, IssueTypeEnum.NOT_FOUND);
        }

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
                .setType(BundleTypeEnum.SEARCH_RESULTS)
                .addEntry(new Bundle.Entry().setResource(careRecordComposition));

        List<ResourceReferenceDt> careProviderPractitionerList = ((Patient) patientEntry.getResource()).getCareProvider();

        if (!careProviderPractitionerList.isEmpty()) {
            String id = careProviderPractitionerList.get(0).getReference().getValue();
            careRecordComposition.setAuthor(Collections.singletonList(new ResourceReferenceDt(id)));

            Practitioner practitioner = practitionerResourceProvider.getPractitionerById(new IdDt(id));

            if (practitioner == null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException("Practitioner Reference returning null"),
                        SystemCode.REFERENCE_NOT_FOUND, IssueTypeEnum.NOT_FOUND);
            }

            bundle.addEntry(new Bundle.Entry().setResource(practitioner).setFullUrl(id));

            IdDt organizationId = practitioner.getPractitionerRoleFirstRep().getManagingOrganization().getReference();

            Bundle.Entry organizationEntry = new Bundle.Entry()
                    .setResource(organizationResourceProvider.getOrganizationById(organizationId))
                    .setFullUrl(organizationId);

            if (organizationEntry.getResource() == null || organizationEntry.getFullUrl() == null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new ResourceNotFoundException("organizationResource returning null"),
                        SystemCode.REFERENCE_NOT_FOUND, IssueTypeEnum.NOT_FOUND);
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

    @Operation(name = REGISTER_PATIENT_OPERATION_NAME)
    public Bundle registerPatient(@ResourceParam Parameters params) {
        Patient registeredPatient = null;
        
        validateParameterNames(params, registerPatientParams);

        Patient unregisteredPatient = params.getParameter()
                .stream()
                .filter(param -> "registerPatient".equalsIgnoreCase(param.getName()))
                .map(Parameter::getResource)
                .map(Patient.class::cast)
                .findFirst()
                .orElse(null);

        if (unregisteredPatient != null) {
            validatePatient(unregisteredPatient);

            // check if the patient already exists
           
            PatientDetails patientDetails = patientSearch.findPatient(nhsNumber.fromPatientResource(unregisteredPatient));

            if (patientDetails == null) {
                patientStore.create(registerPatientResourceConverterToPatientDetail(unregisteredPatient));
                registeredPatient = patientDetailsToRegisterPatientResourceConverter(
                        patientSearch.findPatient(unregisteredPatient.getIdentifierFirstRep().getValue()));
            } else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(String.format("Patient (NHS number - %s) already exists", nhsNumber.fromPatientResource(unregisteredPatient))),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
            }
        } else {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Patient record not found"),
                    SystemCode.INVALID_PARAMETER, IssueTypeEnum.NOT_FOUND);
        }

        Bundle bundle = new Bundle().setType(BundleTypeEnum.SEARCH_RESULTS);
        bundle.addEntry().setResource(registeredPatient);
        return bundle;
    }

    public String getNhsNumber(Object source) {
        return nhsNumber.getNhsNumber(source);
    }

    private void validatePatient(Patient patient) {
        validateIdentifiers(patient);
        validateRegistrationPeriod(patient);
        validateRegistrationStatusAndType(patient);
        validateConstrainedOutProperties(patient);
        valiateNames(patient);
        validateDateOfBirth(patient);
        valiateGender(patient);
      
    }

    private void validateRegistrationPeriod(Patient patient) {
        List<ExtensionDt> registrationPeriodExtensions = patient
                .getUndeclaredExtensionsByUrl(SystemURL.SD_EXTENSION_REGISTRATION_PERIOD);
        
        if(registrationPeriodExtensions.size() == 1) {
            // we need a non null period
            ExtensionDt registrationPeriodExtension = registrationPeriodExtensions.get(0);
            PeriodDt registrationPeriod = (PeriodDt) registrationPeriodExtension.getValue();
            
            if(registrationPeriod == null) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("A registration period must be supplied"),
                                                    SystemCode.BAD_REQUEST,
                                                    IssueTypeEnum.INVALID_CONTENT); 
            }
            
            // note that the spec says nothing about the content of the start or end dates
            // therefore we do not carry out any further validation
            
        }
        else if(registrationPeriodExtensions.size() > 1) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(String.format("Duplicate undeclared extensions (url scheme - %s) found on patient resource", SystemURL.SD_EXTENSION_REGISTRATION_PERIOD)),
                                                SystemCode.BAD_REQUEST,
                                                IssueTypeEnum.INVALID_CONTENT); 
            
        }
        else if(registrationPeriodExtensions.size() < 1) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Registration period is mandatory"),
                                                SystemCode.BAD_REQUEST,
                                                IssueTypeEnum.INVALID_CONTENT); 
            
        }
        
    }

    private void valiateGender(Patient patient) {
        String gender = patient.getGender();
        
        if(gender == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("A Patient's gender must be supplied"),
                                                SystemCode.BAD_REQUEST,
                                                IssueTypeEnum.INVALID_CONTENT); 
        }
    }

    private void validateDateOfBirth(Patient patient) {
        Date birthDate = patient.getBirthDate();

        if(birthDate == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("A Patient's date of birth must be supplied"),
                                                SystemCode.BAD_REQUEST,
                                                IssueTypeEnum.INVALID_CONTENT); 
        }
    }

    private void validateIdentifiers(Patient patient) {
        List<IdentifierDt> identifiers = patient.getIdentifier();
        if(identifiers.isEmpty() == false) {
            boolean identifiersValid = identifiers
                    .stream()
                    .allMatch(identifier -> identifier.getSystem() != null && identifier.getValue() != null);
    
            if(identifiersValid == false) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("One or both of the system and/or value on some of the provided identifiers is null"),
                                                    SystemCode.BAD_REQUEST,
                                                    IssueTypeEnum.INVALID_CONTENT);
            }
        }
        else {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("At least one identifier must be supplied on a Patient resource"),
                                                SystemCode.BAD_REQUEST,
                                                IssueTypeEnum.INVALID_CONTENT);    
        }
    }

    private void validateRegistrationStatusAndType(Patient patient) {
        String registrationStatus = getRegistrationStatus(patient);
        if (ACTIVE_REGISTRATION_STATUS.equals(registrationStatus) == false) {
            String message = String.format("The given registration status is not valid. Expected - %s. Actual - %s", ACTIVE_REGISTRATION_STATUS, registrationStatus);

            throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(message),
                                                                         SystemCode.BAD_REQUEST,
                                                                         IssueTypeEnum.INVALID_CONTENT);
        }

        String registrationType = getRegistrationType(patient);
        if (TEMPORARY_RESIDENT_REGISTRATION_TYPE.equals(registrationType) == false) {
            String message = String.format("The given registration type is not valid. Expected - %s. Actual - %s", TEMPORARY_RESIDENT_REGISTRATION_TYPE, registrationType);

            throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(message),
                                                                         SystemCode.BAD_REQUEST,
                                                                         IssueTypeEnum.INVALID_CONTENT);
        }
    }

    private void validateConstrainedOutProperties(Patient patient) {

        Set<String> invalidFields = new HashSet<String>();

        if (patient.getActive() != null) invalidFields.add("active");
        if (patient.getTelecom().isEmpty() == false) invalidFields.add("telecom");
        if (patient.getDeceased() != null && patient.getDeceased().isEmpty() == false) invalidFields.add("deceased");
        if (patient.getAddress().isEmpty() == false) invalidFields.add("address");
        if (patient.getMaritalStatus().isEmpty() == false) invalidFields.add("marital status");
        if (patient.getMultipleBirth() != null && patient.getMultipleBirth().isEmpty() == false) invalidFields.add("multiple birth");
        if (patient.getPhoto().isEmpty() == false) invalidFields.add("photo");
        if (patient.getContact().isEmpty() == false) invalidFields.add("contact");
        if (patient.getAnimal().isEmpty() == false) invalidFields.add("animal");
        if (patient.getCommunication().isEmpty() == false) invalidFields.add("communication");
        if (patient.getCareProvider().isEmpty() == false) invalidFields.add("care provider");
        if (patient.getManagingOrganization().isEmpty() == false) invalidFields.add("managing organisation");
        if (patient.getLink().isEmpty() == false) invalidFields.add("link");

        if(invalidFields.isEmpty() == false) {
            String message = String.format("The following properties have been constrained out on the Patient resource - %s", String.join(", ", invalidFields));
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(message),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.CONTENT_NOT_SUPPORTED);
        }
    }

    private void valiateNames(Patient patient) {
        List<HumanNameDt> names = patient.getName();
        validateNameCount(names, "name");

        HumanNameDt name = names.iterator().next();
        validateNameCount(name.getFamily(), "family");
        validateNameCount(name.getGiven(), "given");
    }

    private void validateNameCount(List<?> names, String nameType) {
        if(names.size() != 1) {
            String message = String.format("The patient can only have one %s name property. Found %s", nameType, names.size());
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(message),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }

    private PatientDetails registerPatientResourceConverterToPatientDetail(Patient patientResource) {
        PatientDetails patientDetails = new PatientDetails();
        HumanNameDt name = patientResource.getNameFirstRep();
        patientDetails.setForename(name.getGivenAsSingleString());
        patientDetails.setSurname(name.getFamilyAsSingleString());
        patientDetails.setDateOfBirth(patientResource.getBirthDate());
        patientDetails.setGender(patientResource.getGender());
        patientDetails.setNhsNumber(patientResource.getIdentifierFirstRep().getValue());

        BooleanDt multipleBirth = (BooleanDt) patientResource.getMultipleBirth();
		if (multipleBirth != null) {
			try {
				patientDetails.setMultipleBirth(multipleBirth.getValue());
			} catch (ClassCastException cce) {
				throw OperationOutcomeFactory.buildOperationOutcomeException(
						new UnprocessableEntityException("The multiple birth property is expected to be a boolean"),
						SystemCode.INVALID_RESOURCE, IssueTypeEnum.INVALID_CONTENT);
			}
		}

		DateTimeDt deceased = (DateTimeDt) patientResource.getDeceased();
		if(deceased != null) {
			try {
	        	patientDetails.setDeceased(deceased.getValue());
	        }
	        catch(ClassCastException cce) {
	            throw OperationOutcomeFactory.buildOperationOutcomeException(
	                    new UnprocessableEntityException("The multiple deceased property is expected to be a datetime"),
	                    SystemCode.INVALID_RESOURCE, IssueTypeEnum.INVALID_CONTENT);
	        }
		}

        patientDetails.setRegistrationStartDateTime(getRegistrationStartDate(patientResource));
        patientDetails.setRegistrationEndDateTime(getRegistrationEndDate(patientResource));
        patientDetails.setRegistrationStatus(getRegistrationStatus(patientResource));
        patientDetails.setRegistrationType(getRegistrationType(patientResource));

        return patientDetails;
    }

    private Date getRegistrationEndDate(Patient patient) {
        Date endDate = null;
        
        PeriodDt registrationPeriod = getFirstExtensionCode(SystemURL.SD_EXTENSION_REGISTRATION_PERIOD, patient);
       
        if(registrationPeriod != null) {
            endDate = registrationPeriod.getEnd();
        }
        
        return endDate;
    }
    
    private Date getRegistrationStartDate(Patient patient) {
        Date startDate = null;
        
        PeriodDt registrationPeriod = getFirstExtensionCode(SystemURL.SD_EXTENSION_REGISTRATION_PERIOD, patient);
       
        if(registrationPeriod != null) {
            startDate = registrationPeriod.getStart();
        }
        
        return startDate;
    }

    private String getRegistrationType(Patient patient) {
        String registrationType = null;
        
        CodeableConceptDt typeCode =  getFirstExtensionCode(SystemURL.SD_EXTENSION_REGISTRATION_TYPE, patient);
        
        if(typeCode != null) {
            registrationType = typeCode.getCodingFirstRep().getCode();
         }
        
        return registrationType;
    }

    private String getRegistrationStatus(Patient patient) {
        String registrationStatus = null;
        
        CodeableConceptDt statusCode = getFirstExtensionCode(SystemURL.SD_EXTENSION_REGISTRATION_STATUS, patient);
        if(statusCode != null) {
           registrationStatus = statusCode.getCodingFirstRep().getCode();
        }
        
        return registrationStatus;
    }

    @SuppressWarnings("unchecked")
    private <V> V getFirstExtensionCode(String extensionUrl, Patient patient) {
        V value = null;
        
        List<ExtensionDt> extensions = patient.getUndeclaredExtensionsByUrl(extensionUrl);
        if(extensions.isEmpty() == false) {
        
            if(extensions.size() == 1) {
                ExtensionDt extension = extensions.get(0);
                value = (V) extension.getValue();
            }
            else {
                // we do not allow duplicates
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(String.format("Duplicate undeclared extensions (url scheme - %s) found on patient resource", extensionUrl)),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
            }
        }
        
        return value;
    }

    // a cut-down Patient
    private Patient patientDetailsToRegisterPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = patientDetailsToMinimalPatient(patientDetails);

        patient.addName()
                    .addFamily(patientDetails.getSurname())
                    .addGiven(patientDetails.getForename())
                    .setUse(NameUseEnum.USUAL);

        return patient;
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

            patient.setId(new IdDt(patient.getResourceName(), patientDetails.getId(), versionId));
            patient.getMeta().setLastUpdated(lastUpdated);
        }

        patient.getMeta().addProfile(SystemURL.SD_GPC_PATIENT);
        patient.getMeta().setVersionId(versionId);

        patient.addIdentifier(new IdentifierDt(SystemURL.ID_NHS_NUMBER, patientDetails.getNhsNumber()));
        patient.setBirthDate(new DateDt(patientDetails.getDateOfBirth()));

        String gender = patientDetails.getGender();
        if (gender != null) {
            patient.setGender(AdministrativeGenderEnum.forCode(gender.toLowerCase(Locale.UK)));
        }

        Date registrationEndDateTime = patientDetails.getRegistrationEndDateTime();
        Date registrationStartDateTime = patientDetails.getRegistrationStartDateTime();
                
        PeriodDt registrationPeriod = new PeriodDt()
                                            .setStartWithSecondsPrecision(registrationStartDateTime)
                                            .setEndWithSecondsPrecision(registrationEndDateTime);

        patient.addUndeclaredExtension(false, SystemURL.SD_EXTENSION_REGISTRATION_PERIOD, registrationPeriod);
        
        String registrationStatusValue = patientDetails.getRegistrationStatus();
        if (registrationStatusValue != null) {
            patient.addUndeclaredExtension(false, SystemURL.SD_EXTENSION_REGISTRATION_STATUS, new CodeableConceptDt(
                    SystemURL.VS_REGISTRATION_STATUS, registrationStatusValue));
        }

        String registrationTypeValue = patientDetails.getRegistrationType();
        if (registrationTypeValue != null) {
            patient.addUndeclaredExtension(false, SystemURL.SD_EXTENSION_REGISTRATION_TYPE, new CodeableConceptDt(
                    SystemURL.VS_REGISTRATION_TYPE, registrationTypeValue));
        }

        return patient;
    }

    private Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = patientDetailsToMinimalPatient(patientDetails);

        patient.addName()
                .setText(patientDetails.getName())
                .addFamily(patientDetails.getSurname())
                .addGiven(patientDetails.getForename())
                .addPrefix(patientDetails.getTitle())
                .setUse(NameUseEnum.USUAL);

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
        }

        String telephoneNumber = patientDetails.getTelephone();
        if (telephoneNumber != null) {
            ContactPointDt telephone = new ContactPointDt()
                    .setSystem(ContactPointSystemEnum.PHONE)
                    .setValue(telephoneNumber)
                    .setUse(ContactPointUseEnum.HOME);

            patient.setTelecom(Collections.singletonList(telephone));
        }
        
        String maritalStatus = patientDetails.getMaritalStatus();
        if (maritalStatus != null) {
            BoundCodeableConceptDt<MaritalStatusCodesEnum> marital = new BoundCodeableConceptDt<>();
            CodingDt maritalCoding = new CodingDt();
            maritalCoding.setSystem("http://hl7.org/fhir/v3/MaritalStatus");
            maritalCoding.setCode(patientDetails.getMaritalStatus());
            maritalCoding.setDisplay("Married");
            marital.addCoding(maritalCoding);
            
            marital.setText(patientDetails.getMaritalStatus());
          
            patient.setMaritalStatus(marital);
        }
        
       String managingOrganization = patientDetails.getManagingOrganization();
       
        if (managingOrganization != null)
        {
            patient.setManagingOrganization(new ResourceReferenceDt("Organization/"+managingOrganization));
            
        }
        
        patient.setMultipleBirth(new BooleanDt(patientDetails.isMultipleBirth()));

        if(patientDetails.isDeceased()) {
        	patient.setDeceased(new DateTimeDt(patientDetails.getDeceased()));
        }

        return patient;
    }

    private class NhsNumber {

        private NhsNumber() {
            super();
        }

        private String getNhsNumber(Object source) {
            String nhsNumber = fromIdDt(source);

            if (nhsNumber == null) {
                nhsNumber = fromToken(source);

                if(nhsNumber == null) {
                    nhsNumber = fromParameters(source);

                    if(nhsNumber == null) {
                        nhsNumber = fromIdentifierDt(source);
                    }
                }
            }

            if (nhsNumber != null && !NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid NHS number submitted: " + nhsNumber),
                        SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
            }

            return nhsNumber;
        }

        private String fromIdentifierDt(Object source) {
            String nhsNumber = null;

            if(source instanceof IdentifierDt) {
                IdentifierDt identifierDt = (IdentifierDt) source;

                String identifierSystem = identifierDt.getSystem();
                if(identifierSystem != null && SystemURL.ID_NHS_NUMBER.equals(identifierSystem)) {
                    nhsNumber = identifierDt.getValue();
                }
                else {
                    String message = String.format("The given identifier system code (%s) does not match the expected code - %s", identifierSystem, SystemURL.ID_NHS_NUMBER);

                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException(message),
                            SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueTypeEnum.INVALID_CONTENT);
                }
            }

            return nhsNumber;
        }

        private String fromIdDt(Object source) {
            String nhsNumber = null;

            if(source instanceof IdDt) {
                IdDt idDt = (IdDt) source;

                PatientDetails patientDetails = patientSearch.findPatientByInternalID(idDt.getIdPart());
                if(patientDetails != null) {
                    nhsNumber = patientDetails.getNhsNumber();
                }
            }

            return nhsNumber;
        }

        private String fromToken(Object source) {
            String nhsNumber = null;

            if(source instanceof TokenParam) {
                TokenParam tokenParam = (TokenParam) source;

                if (!SystemURL.ID_NHS_NUMBER.equals(tokenParam.getSystem())) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid system code"),
                        SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
                }

                nhsNumber = tokenParam.getValue();
            }

            return nhsNumber;
        }

        private String fromParameters(Object source) {
            String nhsNumber = null;

            if(source instanceof Parameters) {
                Parameters parameters = (Parameters) source;

                Parameter parameter = getParameterByName(parameters.getParameter(), "patientNHSNumber");
                if(parameter != null) {
                    nhsNumber = fromIdentifierDt(parameter.getValue());
                }
                else {
                    parameter = getParameterByName(parameters.getParameter(), "registerPatient");
                    if(parameter != null) {
                        nhsNumber = fromPatientResource(parameter.getResource());
                    }
                    else {
                        throw OperationOutcomeFactory.buildOperationOutcomeException(
                                new InvalidRequestException("Unable to read parameters. Expecting one of patientNHSNumber or registerPatient both of which are case-sensitive"),
                                SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
                    }
                }
            }

            return nhsNumber;
        }

        private String fromPatientResource(Object source) {
            String nhsNumber = null;

            if(source instanceof Patient) {
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

            if(filteredParameters != null) {
                if(filteredParameters.size() == 1) {
                    parameter = filteredParameters.iterator().next();
                }
                else if(filteredParameters.size() > 1) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("The parameter " + parameterName + " cannot be set more than once"),
                            SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
                }
            }

            return parameter;
        }
    }
}
