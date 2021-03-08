package uk.gov.hscic.patient;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.io.IOException;
import static java.lang.Integer.min;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.common.helpers.StaticElementsHelper;
import uk.gov.hscic.common.validators.IdentifierValidator;
import uk.gov.hscic.medications.PopulateMedicationBundle;
import uk.gov.hscic.model.organization.OrganizationDetails;
import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.organization.OrganizationSearch;
import uk.gov.hscic.patient.details.PatientSearch;
import uk.gov.hscic.patient.details.PatientStore;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;
import uk.gov.hscic.practitioner.PractitionerRoleResourceProvider;
import uk.gov.hscic.util.NhsCodeValidator;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import static org.hl7.fhir.dstu3.model.Address.AddressUse.OLD;
import static org.hl7.fhir.dstu3.model.Address.AddressUse.WORK;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hscic.SystemConstants;
import static uk.gov.hscic.SystemConstants.*;
import static uk.gov.hscic.SystemHeader.SSP_TRACEID;
import static uk.gov.hscic.SystemURL.SD_CC_EXT_NHS_COMMUNICATION;
import static uk.gov.hscic.SystemURL.VS_GPC_ERROR_WARNING_CODE;
import uk.gov.hscic.model.telecom.TelecomDetails;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwInvalidRequest400_BadRequestException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntity422_InvalidResourceException;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyListNote;
import static uk.gov.hscic.patient.StructuredAllergyIntoleranceBuilder.addEmptyReasonCode;
import uk.gov.hscic.patient.details.PatientEntity;
import static uk.gov.hscic.patient.StructuredBuilder.createCondition;

@Component
public class PatientResourceProvider implements IResourceProvider {

    public static final String REGISTER_PATIENT_OPERATION_NAME = "$gpc.registerpatient";
    public static final String GET_CARE_RECORD_OPERATION_NAME = "$gpc.getcarerecord";
    public static final String GET_STRUCTURED_RECORD_OPERATION_NAME = "$gpc.getstructuredrecord";

    private static final String TEMPORARY_RESIDENT_REGISTRATION_TYPE = "T";
    private static final String ACTIVE_REGISTRATION_STATUS = "A";

    private static final int ADDRESS_CITY_INDEX = 3;
    private static final int ADDRESS_DISTRICT_INDEX = 4;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private PractitionerResourceProvider practitionerResourceProvider;

    @Autowired
    private PractitionerRoleResourceProvider practitionerRoleResourceProvider;

    @Autowired
    private AppointmentResourceProvider appointmentResourceProvider;

    @Autowired
    private OrganizationResourceProvider organizationResourceProvider;

    @Autowired
    private PatientStore patientStore;

    @Autowired
    private PatientSearch patientSearch;

    @Autowired
    private OrganizationSearch organizationSearch;

    @Autowired
    private StaticElementsHelper staticElHelper;

    @Autowired
    private StructuredAllergyIntoleranceBuilder structuredAllergyIntoleranceBuilder;

    @Autowired
    private PopulateMedicationBundle populateMedicationBundle;

    @Value("${datasource.patients:#{null}}")
    private String[] patients;

    @Value("${config.path}")
    private String configPath;

    @Value("#{${responses}}")
    private Map<String, String> hmCannedResponse;

    private NhsNumber nhsNumber;

    private Map<String, Boolean> registerPatientParams;

    private OperationOutcome operationOutcome;

    public static Set<String> getCustomReadOperations() {
        Set<String> customReadOperations = new HashSet<>();
        customReadOperations.add(GET_CARE_RECORD_OPERATION_NAME);

        return customReadOperations;
    }

    public static Set<String> getCustomWriteOperations() {
        Set<String> customWriteOperations = new HashSet<>();
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

        registerPatientParams = new HashMap<>();
        registerPatientParams.put("registerPatient", true);

        // Spring does not strip trailing blanks from property values
        for (int i = 0; i < patients.length; i++) {
            patients[i] = patients[i].trim();
        }
    }

    @Read(version = true)
    public Patient getPatientById(@IdParam IdType internalId) throws FHIRException {
        PatientDetails patientDetails = patientSearch.findPatientByInternalID(internalId.getIdPart());

        if (patientDetails == null || patientDetails.isSensitive() || patientDetails.isDeceased() || !patientDetails.isActive()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No patient details found for patient ID: " + internalId.getIdPart()),
                    SystemCode.PATIENT_NOT_FOUND, IssueType.NOTFOUND);
        }

        Patient patient = IdentifierValidator.versionComparison(internalId,
                patientDetailsToPatientResourceConverter(patientDetails));
        if (null != patient) {
            addPreferredBranchSurgeryExtension(patient);
        }
        return patient;
    }

    @Search
    public List<Patient> getPatientsByPatientId(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam tokenParam)
            throws FHIRException {

        Patient patient = getPatientByPatientId(nhsNumber.fromToken(tokenParam));
        if (null != patient) {
            addPreferredBranchSurgeryExtension(patient);
        }

        PatientDetails patientDetails = patientSearch.findPatient(nhsNumber.fromToken(tokenParam));

        // ie does not return a deceased, inactive or sensitive patient in the list 
        return null == patient || patient.getDeceased() != null || !patientDetails.isActive() || patientDetails.isSensitive() ? Collections.emptyList()
                : Collections.singletonList(patient);
    }

    private void addPreferredBranchSurgeryExtension(Patient patient) {
        List<Extension> regDetailsEx = patient.getExtensionsByUrl(SystemURL.SD_EXTENSION_CC_REG_DETAILS);
        Extension branchSurgeryEx = regDetailsEx.get(0).addExtension();
        branchSurgeryEx.setUrl("preferredBranchSurgery");
        branchSurgeryEx.setValue(new Reference("Location/1"));
    }

    private Patient getPatientByPatientId(String patientId) throws FHIRException {
        PatientDetails patientDetails = patientSearch.findPatient(patientId);

        return null == patientDetails ? null : patientDetailsToPatientResourceConverter(patientDetails);
    }

    private void validateParameterNames(Parameters parameters, Map<String, Boolean> parameterDefinitions) {
        List<String> parameterNames = parameters.getParameter().stream().map(ParametersParameterComponent::getName)
                .collect(Collectors.toList());

        Set<String> parameterDefinitionNames = parameterDefinitions.keySet();

        if (!parameterNames.isEmpty()) {
            for (String parameterDefinition : parameterDefinitionNames) {
                boolean mandatory = parameterDefinitions.get(parameterDefinition);

                if (mandatory) {
                    if (!parameterNames.contains(parameterDefinition)) {
                        throwInvalidParameterInvalidOperationalOutcome("Not all mandatory parameters have been provided - " + parameterDefinition);
                    }
                }
            }

            if (!parameterDefinitionNames.containsAll(parameterNames)) {
                parameterNames.removeAll(parameterDefinitionNames);
                throwInvalidRequest400_BadRequestException("Unrecognised parameters have been provided - " + parameterNames.toString());
            }
        } else {
            throwInvalidParameterInvalidOperationalOutcome("No mandatory parameters have been provided");
        }
    }

    @Search(compartmentName = "Appointment")
    public List<Appointment> getPatientAppointments(@IdParam IdType patientLocalId, @Sort SortSpec sort,
            @Count Integer count, @OptionalParam(name = "start") DateAndListParam startDate) {
        return appointmentResourceProvider.getAppointmentsForPatientIdAndDates(patientLocalId, sort, count, startDate);
    }

    @Operation(name = GET_STRUCTURED_RECORD_OPERATION_NAME)
    public Bundle StructuredRecordOperation(@ResourceParam Parameters params, HttpServletRequest theRequest) throws FHIRException, IOException {

        String NHS = getNhsNumber(params);

        PatientDetails patientDetails = patientSearch.findPatient(NHS);

        // see https://nhsconnect.github.io/gpconnect/accessrecord_structured_development_retrieve_patient_record.html#error-handling
        if (patientDetails == null || patientDetails.isSensitive() || patientDetails.isDeceased() || !patientDetails.isActive()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No patient details found for patient ID: " + NHS),
                    SystemCode.PATIENT_NOT_FOUND, IssueType.NOTFOUND);
        }

        if (NHS.equals(patients[PATIENT_NOCONSENT])) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ForbiddenOperationException("No patient consent to share for patient ID: " + NHS),
                    SystemCode.NO_PATIENT_CONSENT, IssueType.FORBIDDEN);
        }

        operationOutcome = null;

        // this is just for allergies and meds
        HashMap<String, ParametersParameterComponent> uncannedClinicalAreaParameters = new HashMap<>();

        // for all the rest ie canned parameters
        HashMap<String, ArrayList<ParametersParameterComponent>> cannedCLinicalAreaParameters = new HashMap<>();

        validateParameters(params, uncannedClinicalAreaParameters, cannedCLinicalAreaParameters);

        Bundle structuredBundle = new Bundle();
        
        // #366 add the trace id as the Bundle id
        String traceId = theRequest.getHeader(SSP_TRACEID);
        if (traceId != null) {
            structuredBundle.setId(traceId);
        }

        // Add Patient
        Patient patient = patientDetailsToPatientResourceConverter(patientDetails);

        if (patient.getIdentifierFirstRep()
                .getValue().equals(NHS)) {
            structuredBundle.addEntry().setResource(patient);
        }

        //Organization from patient
        Set<String> orgIds = new HashSet<>();

        orgIds.add(patientDetails.getManagingOrganization());

        //Practitioner from patient
        Set<String> practitionerIds = new HashSet<>();
        List<Reference> practitionerReferenceList = patient.getGeneralPractitioner();

        practitionerReferenceList.forEach(practitionerReference
                -> {
            String[] pracRef = practitionerReference.getReference().split("/");
            if (pracRef.length > 1) {
                practitionerIds.add(pracRef[1]);
            }
        }
        );

        for (String uncannedClinicalArea : uncannedClinicalAreaParameters.keySet()) {
            ParametersParameterComponent param = uncannedClinicalAreaParameters.get(uncannedClinicalArea);
            switch (uncannedClinicalArea) {
                case INCLUDE_ALLERGIES_PARM:
                    structuredAllergyIntoleranceBuilder.buildStructuredAllergyIntolerance(structuredBundle, param, NHS, practitionerIds);
                    break;

                case INCLUDE_MEDICATION_PARM:
                    populateMedicationBundle.addMedicationBundleEntries(structuredBundle, param, patientDetails, practitionerIds, orgIds);
                    break;
            }
            addProblemEntryForUncanned(NHS, structuredBundle, patient, uncannedClinicalArea);
        }

        // append the required canned responses for patient 2 only
        StructuredBuilder structureBuilder = new StructuredBuilder();
        for (String cannedClinicalArea
                : cannedCLinicalAreaParameters.keySet()) {
            if (Arrays.asList(new String[]{patients[PATIENT_2], patients[PATIENT_3]}).contains(NHS)) {
                structureBuilder.appendCannedResponse(configPath + "/" + hmCannedResponse.get(cannedClinicalArea), structuredBundle, patient, cannedCLinicalAreaParameters.get(cannedClinicalArea));
            } else {
                // only patients 2 and 3 have canned repsonses all the rest not.
                addEmptyList(cannedClinicalArea, NHS, structuredBundle);
            }
        }

        //Add all practitioners and practitioner roles
        for (String practitionerId : practitionerIds) {
            Practitioner pracResource = practitionerResourceProvider.getPractitionerById(new IdType(practitionerId));
            structuredBundle.addEntry().setResource(pracResource);

            List<PractitionerRole> practitionerRoleList = practitionerRoleResourceProvider.getPractitionerRoleByPracticionerId(new IdType(practitionerId));
            for (PractitionerRole role : practitionerRoleList) {
                String[] split = role.getOrganization().getReference().split("/");
                orgIds.add(split[1]);
                structuredBundle.addEntry().setResource(role);
            }
        }

        //Add all organizations
        for (String orgId : orgIds) {
            OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(new Long(orgId));
            Organization organization = organizationResourceProvider.convertOrganizationDetailsToOrganization(organizationDetails);
            structuredBundle.addEntry().setResource(organization);
        }

        structuredBundle.setType(BundleType.COLLECTION);

        structuredBundle.getMeta()
                .addProfile(SystemURL.SD_GPC_STRUCTURED_BUNDLE);

        if (operationOutcome != null) {
            structuredBundle.addEntry().setResource(operationOutcome);
        } else {
            removeDuplicateResources(structuredBundle);
        }
        return structuredBundle;
    }

    /**
     *
     * adds an entry to the problem list pointing at a problem/Condition This
     * handles the case where an uncanned clinical area is requested and a
     * problem needs adding for patient 2 for testing purposes.
     *
     * @param NHS
     * @param structuredBundle
     * @param patient
     * @param uncannedClinicalArea
     */
    private void addProblemEntryForUncanned(String NHS, Bundle structuredBundle, Patient patient, String uncannedClinicalArea) {
        // if patient 2 add a dummy allergy problem and list (if required)
        if (NHS.equals(patients[PATIENT_2])) {

            // #359 new secondary list
            ListResource problemsLinkedNotRelatedToPrimaryQueryList = null;
            Resource resource = null;
            Resource medicationsRequest = null;
            for (BundleEntryComponent entry : structuredBundle.getEntry()) {
                if (entry.getResource() instanceof ListResource) {
                    ListResource listResource = (ListResource) entry.getResource();
                    if (listResource.getId() != null) {
                        switch (listResource.getId()) {
                            case PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE:
                                problemsLinkedNotRelatedToPrimaryQueryList = listResource;
                                break;
                        }
                    }
                } else if ((uncannedClinicalArea.equals(INCLUDE_ALLERGIES_PARM) && entry.getResource() instanceof AllergyIntolerance)
                        || (uncannedClinicalArea.equals(INCLUDE_MEDICATION_PARM) && entry.getResource() instanceof MedicationStatement)) {
                    if (resource == null) {
                        resource = entry.getResource();
                    }
                } else if (uncannedClinicalArea.equals(INCLUDE_MEDICATION_PARM) && entry.getResource() instanceof MedicationRequest) {
                    if (medicationsRequest == null) {
                        medicationsRequest = entry.getResource();
                    }
                }

                if (problemsLinkedNotRelatedToPrimaryQueryList != null && resource != null
                        && (uncannedClinicalArea.equals(INCLUDE_ALLERGIES_PARM) || uncannedClinicalArea.equals(INCLUDE_MEDICATION_PARM) && medicationsRequest != null)) {
                    break;
                }
            }
            if (problemsLinkedNotRelatedToPrimaryQueryList == null) {
                problemsLinkedNotRelatedToPrimaryQueryList = StructuredBuilder.createList(PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_CODE, PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE, SECONDARY_LIST_URL, patient);
                structuredBundle.addEntry(new BundleEntryComponent().setResource(problemsLinkedNotRelatedToPrimaryQueryList));
            }

            // create the named problem and add that to the response
            String problemId = uncannedClinicalArea.replaceFirst("^include(.*)$", "$1Problem");

            if (resource != null) {
                Condition condition = null;
                switch (uncannedClinicalArea) {
                    case INCLUDE_ALLERGIES_PARM:
                        condition = createCondition(problemId, patient, resource.getResourceType() + "/" + resource.getId());
                        break;
                    case INCLUDE_MEDICATION_PARM:

                        // ensure the problem references both a medication statment and a medication request.
                        condition = createCondition(problemId, patient,
                                new String[]{resource.getResourceType() + "/" + resource.getId(),
                                    medicationsRequest.getResourceType() + "/" + medicationsRequest.getId()});
                        break;
                }

                if (condition != null) {
                    structuredBundle.addEntry(new BundleEntryComponent().setResource(condition));

                    // add the reference to the problem (Condition) to the problem list named for the parameter eg includeMedications
                    // TODO are these still in order? get these to be generated automatically?
                    problemsLinkedNotRelatedToPrimaryQueryList.addEntry(new ListEntryComponent().setItem(new Reference("Condition/" + problemId)));
                }
            } else {
                System.err.println("No resource found to insert for uncanned clinical area " + uncannedClinicalArea);
            }
        }
    }

    /**
     *
     * @param params
     * @param uncannedClinicalAreaParameters
     * @param cannedClinicalAreaParameters
     * @throws BaseServerResponseException
     */
    private void validateParameters(Parameters params, HashMap<String, ParametersParameterComponent> uncannedClinicalAreaParameters, HashMap<String, ArrayList<ParametersParameterComponent>> cannedClinicalAreaParameters) throws BaseServerResponseException {
        for (ParametersParameterComponent param : params.getParameter()) {
            String paramName = param.getName();
            if (validateParametersName(paramName)) {

                AbstractClinicalArea clinicalArea = null;

                // list of parameter instances for each parameter, required for clinical areas which have cardinality 0..*
                // all but problems will have only one array entry in the parameter list
                // hence includes may already have an allocated array of parameters
                switch (paramName) {
                    case PATIENT_NHS_NUMBER_PARM:
                        break;
                    case INCLUDE_ALLERGIES_PARM:
                    case INCLUDE_MEDICATION_PARM:
                        if (uncannedClinicalAreaParameters.get(paramName) == null) {
                            uncannedClinicalAreaParameters.put(paramName, param);
                        } else {
                            throwInvalidParameterInvalidOperationalOutcome("Repeated Parameter : " + paramName);
                        }
                        clinicalArea = instantiateClinicalArea(paramName);
                        break;
                    default:
                        // this is different from other parameters in that it can appear more than once
                        if (cannedClinicalAreaParameters.get(paramName) == null) {
                            addNewParameterListToIncludes(param, cannedClinicalAreaParameters);
                        } else {
                            if (paramName.equals(INCLUDE_PROBLEMS_PARM)) {
                                cannedClinicalAreaParameters.get(paramName).add(param);
                            } else {
                                throwInvalidParameterInvalidOperationalOutcome("Repeated Parameter : " + paramName);
                            }
                        }
                        clinicalArea = instantiateClinicalArea(paramName);
                        break;
                }

                if (clinicalArea != null) {
                    clinicalArea.validateParameterParts(param);
                }
            } else {
                // invalid parameter
                addWarningIssue(param, IssueType.NOTSUPPORTED);
            }
        } // for parameter
    }

    /**
     * There is some magic required here to invoke an inner class constructor
     * see
     * https://stackoverflow.com/questions/17485297/how-to-instantiate-an-inner-class-with-reflection-in-java
     *
     * @param paramName
     * @return parameterValidator
     */
    private AbstractClinicalArea instantiateClinicalArea(String paramName) {
        AbstractClinicalArea clinicalArea = null;
        try {
            // map from name of clinical area parameter to appropriate class name
            Class<?> toRun = Class.forName(getClass().getName() + "$" + paramName.replaceFirst("include", "") + "ClinicalArea");
            Constructor<?> ctor = toRun.getDeclaredConstructor(PatientResourceProvider.class);
            clinicalArea = (AbstractClinicalArea) ctor.newInstance(this);
        } catch (Exception ex) {
            System.err.println("Failed to instantiate ClinicalArea " + paramName + "" + ex.getMessage());
        }
        return clinicalArea;
    }

    private void addNewParameterListToIncludes(ParametersParameterComponent param, HashMap<String, ArrayList<ParametersParameterComponent>> includes) {
        ArrayList<ParametersParameterComponent> parameters = new ArrayList<>();
        parameters.add(param);
        includes.put(param.getName(), parameters);
    }

    /**
     * add a list item with empty flags set. No data returned for this clinical
     * area.
     *
     * @param clinicalAreaName
     * @param NHS
     * @param structuredBundle
     */
    private void addEmptyList(String clinicalAreaName, String NHS, Bundle structuredBundle) {
        // see https://gpc-structured-futures.netlify.com/accessrecord_structured_development_list.html#emptyreason
        // if its not patient 2 add an empty list for the requested include
        ListResource list = new ListResource();
        list.setMeta(new Meta().addProfile(SystemURL.SD_GPC_LIST));
        list.setStatus(ListResource.ListStatus.CURRENT);
        list.setMode(ListResource.ListMode.SNAPSHOT);

        AbstractClinicalArea clinicalArea = instantiateClinicalArea(clinicalAreaName);
        if (clinicalArea != null) {
            clinicalArea.setListMetaData(list);
        } else {
            System.err.println("Unrecognised Clinical Area " + clinicalAreaName);
        }

        PatientEntity patient = patientStore.findByNhsNumber(NHS);

        // was hardcoded to patient 1
        list.setSubject(new Reference(new IdType("Patient", patient.getId())).setIdentifier(new Identifier().setValue(NHS).setSystem(SystemURL.ID_NHS_NUMBER)));
        list.setDate(new Date());
        list.setOrderedBy(createCodeableConcept("event-date", "Sorted by Event Date", SystemURL.CS_LIST_ORDER));

        // #284 align behaviour with meds and allergies
        addEmptyListNote(list);
        addEmptyReasonCode(list);
        // #283 change of emptyReason.text
        //list.setEmptyReason(new CodeableConcept().setText(SystemConstants.NO_CONTENT_RECORDED));
        //list.setNote(Arrays.asList(new Annotation(new StringType(SystemConstants.INFORMATION_NOT_AVAILABLE))));

        // TODO call the warnings helper here?
        BundleEntryComponent entry = new BundleEntryComponent();
        entry.setResource(list);
        structuredBundle.addEntry(entry);
    }

    /**
     *
     * @return new Object
     */
    private void createOperationOutcome() {
        operationOutcome = new OperationOutcome();
        // TODO Check this it doesn't look consistent but its as per the example
        operationOutcome.setId(java.util.UUID.randomUUID().toString());
        operationOutcome.getMeta().addProfile(SystemURL.SD_GPC_OPERATIONOUTCOME);
    }

    /**
     * Overload
     *
     * @param param
     * @param paramPart
     * @param issueType
     */
    private void addWarningIssue(ParametersParameterComponent param, ParametersParameterComponent paramPart, IssueType issueType) {
        addWarningIssue(param, paramPart, issueType, null);
    }

    /**
     * Overload
     *
     * @param param
     * @param issueType
     */
    private void addWarningIssue(ParametersParameterComponent param, IssueType issueType) {
        addWarningIssue(param, null, issueType, null);
    }

    /**
     * Overload
     *
     * @param param
     * @param issueType
     * @param details
     */
    private void addWarningIssue(ParametersParameterComponent param, IssueType issueType, String details) {
        addWarningIssue(param, null, issueType, details);
    }

    /**
     * see
     * https://gpconnect-1-2-4.netlify.com/accessrecord_structured_development_version_compatibility.html
     * add an issue to the OperationOutcome to be returned in a successful
     * response bundle this is for forward compatibility as specified in 1.2.4
     *
     * @param param
     * @param paramPart
     * @param issueType
     * @param details lower level details to be added to the text element
     */
    private void addWarningIssue(ParametersParameterComponent param, ParametersParameterComponent paramPart, IssueType issueType, String details) {
        if (operationOutcome == null) {
            createOperationOutcome();
        }
        OperationOutcomeIssueComponent issue = new OperationOutcomeIssueComponent();
        issue.setSeverity(OperationOutcome.IssueSeverity.WARNING);

        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding();
        coding.setSystem(VS_GPC_ERROR_WARNING_CODE);
        switch (issueType) {
            case NOTSUPPORTED:
                issue.setCode(issueType);
                coding.setCode(SystemCode.NOT_IMPLEMENTED);
                coding.setDisplay("Not implemented");
                break;
            case REQUIRED:
                issue.setCode(issueType);
                coding.setCode(SystemCode.PARAMETER_NOT_FOUND);
                coding.setDisplay("Parameter not found");
                break;
            case INVALID:
                issue.setCode(issueType);
                coding.setCode(SystemCode.INVALID_PARAMETER);
                coding.setDisplay("Invalid Parameter");
                break;
        }

        codeableConcept.addCoding(coding);
        issue.setDetails(codeableConcept);

        String locus = paramPart != null ? "." + paramPart.getName() : "";
        issue.setDiagnostics(param.getName() + locus);
        if (details == null) {
            // mod to remove more informative text which was off spec
            codeableConcept.setText(param.getName() + locus + " is an unrecognised parameter" /*+ (paramPart != null ? " part" : "")*/);
        } else {
            codeableConcept.setText(details);
        }
        operationOutcome.addIssue(issue);
    }

    private boolean validateParametersName(String name) {
        return VALID_PARAMETER_NAMES.contains(name);
    }

    @Operation(name = REGISTER_PATIENT_OPERATION_NAME)
    public Bundle registerPatient(@ResourceParam Parameters params) {
        Patient registeredPatient = null;

        validateParameterNames(params, registerPatientParams);

        Patient unregisteredPatient = params.getParameter().stream()
                .filter(param -> "registerPatient".equalsIgnoreCase(param.getName()))
                .map(ParametersParameterComponent::getResource).map(Patient.class::cast).findFirst().orElse(null);

        String nnn = nhsNumber.fromPatientResource(unregisteredPatient);

        // see https://nhsconnect.github.io/gpconnect/foundations_use_case_register_a_patient.html#error-handling
        // if its patient 14 spoof not on PDS and return the required error 
        if (nnn.equals(patients[PATIENT_NOTONSPINE])) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(String.format("Patient (NHS number - %s) not present on PDS", nnn)),
                    SystemCode.INVALID_PATIENT_DEMOGRAPHICS, IssueType.BUSINESSRULE);
        } else if (nnn.equals(patients[PATIENT_SUPERSEDED])) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(String.format("Patient (NHS number - %s) is superseded", nnn)),
                    SystemCode.INVALID_NHS_NUMBER, IssueType.VALUE);
        }

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
                    // reactivate inactive non temporary patient
                    patientDetails.setRegistrationStatus(ACTIVE_REGISTRATION_STATUS);
                    updateAddressAndTelecom(unregisteredPatient, patientDetails);

                    patientStore.update(patientDetails);
                }
                try {
                    registeredPatient = patientDetailsToRegisterPatientResourceConverter(
                            patientSearch.findPatient(unregisteredPatient.getIdentifierFirstRep().getValue()));

                    addPreferredBranchSurgeryExtension(registeredPatient);
                } catch (FHIRException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (patientDetails.isDeceased() || patientDetails.isSensitive()) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException(String.format("Patient (NHS number - %s) has invalid demographics", nnn)),
                        SystemCode.INVALID_PATIENT_DEMOGRAPHICS, IssueType.BUSINESSRULE);

            } else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(409, String.format("Patient (NHS number - %s) already exists", nnn)),
                        SystemCode.DUPLICATE_REJECTED, IssueType.DUPLICATE);
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

    private void updateAddressAndTelecom(Patient unregisteredPatient, PatientDetails patientDetails) {
        ArrayList<TelecomDetails> al = new ArrayList<>();
        if (unregisteredPatient.getTelecom().size() > 0) {
            for (ContactPoint contactPoint : unregisteredPatient.getTelecom()) {
                TelecomDetails telecomDetails = new TelecomDetails();
                telecomDetails.setSystem(contactPoint.getSystem().toString());
                telecomDetails.setUseType(contactPoint.getUse().toString());
                telecomDetails.setValue(contactPoint.getValue());
                al.add(telecomDetails);
            }
        }
        patientDetails.setTelecoms(al);

        // actually a list of addresses not a single one
        if (unregisteredPatient.getAddress().size() > 0) {
            // get the first one off the block
            Address address = unregisteredPatient.getAddress().get(0);
            String[] addressLines = new String[ADDRESS_DISTRICT_INDEX + 1];
            List<StringType> addressLineList = address.getLine();
            for (int i = 0; i < ADDRESS_CITY_INDEX; i++) {
                if (i < addressLineList.size()) {
                    addressLines[i] = addressLineList.get(i).asStringValue();
                } else {
                    addressLines[i] = null;
                }
            }
            addressLines[ADDRESS_CITY_INDEX] = address.getCity();
            addressLines[ADDRESS_DISTRICT_INDEX] = address.getDistrict();
            patientDetails.setAddress(addressLines);
            patientDetails.setPostcode(address.getPostalCode());
        }
    }

    /**
     * Returns true if registration type is temporary AND the record is marked
     * inactive
     *
     * @param patientDetails assumed non null
     * @return Boolean object
     */
    private Boolean IsInactiveTemporaryPatient(PatientDetails patientDetails) {

        return patientDetails.getRegistrationType() != null
                && TEMPORARY_RESIDENT_REGISTRATION_TYPE.equals(patientDetails.getRegistrationType())
                && patientDetails.getRegistrationStatus() != null
                && !ACTIVE_REGISTRATION_STATUS.equals(patientDetails.getRegistrationStatus());
    }

    public String getNhsNumber(Object source) {
        return nhsNumber.getNhsNumber(source);
    }

    private void validatePatient(Patient patient) {
        validateIdentifiers(patient);
        validateTelecomAndAddress(patient);
        validateConstrainedOutProperties(patient);
        checkValidExtensions(patient.getExtension());
        validateNames(patient);
        validateDateOfBirth(patient);
        validateGender(patient);
    }

    private void validateTelecomAndAddress(Patient patient) {
        // 0..1 of phone - (not nec. temp),  0..1 of email
        HashSet<ContactPointUse> phoneUse = new HashSet<>();
        int emailCount = 0;
        for (ContactPoint telecom : patient.getTelecom()) {
            if (telecom.hasSystem()) {
                if (telecom.getSystem() != null) {
                    switch (telecom.getSystem()) {
                        case PHONE:
                            if (telecom.hasUse()) {
                                switch (telecom.getUse()) {
                                    case HOME:
                                    case WORK:
                                    case MOBILE:
                                    case TEMP:
                                        if (!phoneUse.contains(telecom.getUse())) {
                                            phoneUse.add(telecom.getUse());
                                        } else {
                                            throwInvalidRequest400_BadRequestException("Only one Telecom of type phone with use type "
                                                    + telecom.getUse().toString().toLowerCase() + " is allowed in a register patient request.");
                                        }
                                        break;
                                    default:
                                        throwInvalidRequest400_BadRequestException(
                                                "Invalid Telecom of type phone use type " + telecom.getUse().toString().toLowerCase()
                                                + " in a register patient request.");
                                }
                            } else {
                                throwInvalidRequest400_BadRequestException("Invalid Telecom - no Use type provided in a register patient request.");
                            }
                            break;
                        case EMAIL:
                            if (++emailCount > 1) {
                                throwInvalidRequest400_BadRequestException("Only one Telecom of type " + "email" + " is allowed in a register patient request.");
                            }
                            break;
                        default:
                            throwInvalidRequest400_BadRequestException("Telecom system is missing in a register patient request.");
                    }
                }
            } else {
                throwInvalidRequest400_BadRequestException("Telecom system is missing in a register patient request.");
            }
        } // iterate telcom 

        // count by useType - Only the first address is persisted at present
        HashSet<AddressUse> useTypeCount = new HashSet<>();
        for (Address address : patient.getAddress()) {
            AddressUse useType = address.getUse();
            // #189 address use types work and old are not allowed
            if (useType == WORK || useType == OLD) {
                throwUnprocessableEntity422_InvalidResourceException("Address use type " + useType + " cannot be sent in a register patient request.");
            }
            if (!useTypeCount.contains(useType)) {
                useTypeCount.add(useType);
            } else {
                // #174 Only a single address of each usetype may be sent
                throwUnprocessableEntity422_InvalidResourceException("Only a single address of each use type can be sent in a register patient request.");
            }
        } // for address
    } //  validateTelecomAndAddress

    private void validateGender(Patient patient) {
        AdministrativeGender gender = patient.getGender();

        if (gender != null) {

            EnumSet<AdministrativeGender> genderList = EnumSet.allOf(AdministrativeGender.class
            );
            Boolean valid = false;
            for (AdministrativeGender genderItem : genderList) {

                if (genderItem.toCode().equalsIgnoreCase(gender.toString())) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                throwInvalidRequest400_BadRequestException(String.format("The supplied Patient gender %s is an unrecognised type.", gender));
            }
        }
    }

    private void validateDateOfBirth(Patient patient) {
        Date birthDate = patient.getBirthDate();

        if (birthDate == null) {
            throwInvalidRequest400_BadRequestException("The Patient date of birth must be supplied");
        }
    }

    private void validateIdentifiers(Patient patient) {
        List<Identifier> identifiers = patient.getIdentifier();
        if (!identifiers.isEmpty()) {
            boolean identifiersValid = identifiers.stream()
                    .allMatch(identifier -> identifier.getSystem() != null && identifier.getValue() != null);

            if (!identifiersValid) {
                throwInvalidRequest400_BadRequestException("One or both of the system and/or value on some of the provided identifiers is null");
            }
        } else {
            throwInvalidRequest400_BadRequestException("At least one identifier must be supplied on a Patient resource");
        }
    }

    private void checkValidExtensions(List<Extension> undeclaredExtensions) {

        // This list must be empty for the request to be valid
        List<String> extensionURLs = undeclaredExtensions.stream().map(Extension::getUrl).collect(Collectors.toList());

        // see https://nhsconnect.github.io/gpconnect/foundations_use_case_register_a_patient.html
        extensionURLs.remove(SystemURL.SD_EXTENSION_CC_REG_DETAILS);

        // these commented out entries are not allowed at 1.2.2 so don't get removed from the list
        //extensionURLs.remove(SystemURL.SD_CC_EXT_ETHNIC_CATEGORY);
        //extensionURLs.remove(SystemURL.SD_CC_EXT_RELIGIOUS_AFFILI);
        //extensionURLs.remove(SystemURL.SD_PATIENT_CADAVERIC_DON);
        //extensionURLs.remove(SystemURL.SD_CC_EXT_RESIDENTIAL_STATUS);
        //extensionURLs.remove(SystemURL.SD_CC_EXT_TREATMENT_CAT);
        //extensionURLs.remove(SystemURL.SD_CC_EXT_NHS_COMMUNICATION);
        // 1.2.2 allows 0..1 SD_CC_EXT_NHS_COMMUNICATION
        long communicationCount = extensionURLs.stream().filter(extension -> extension.equals(SD_CC_EXT_NHS_COMMUNICATION)).count();
        if (communicationCount == 1) {
            extensionURLs.remove(SystemURL.SD_CC_EXT_NHS_COMMUNICATION);
            // also remove any associated "interpreterRequired" url, other urls from eg language etc dont appear in this list.
            // so don't need to be removed
            extensionURLs.remove(SystemURL.SD_CC_INTERPRETER_REQUIRED);
        }

        if (!extensionURLs.isEmpty()) {
            throwUnprocessableEntity422_InvalidResourceException("Invalid/multiple patient extensions found. The following are in excess or invalid: "
                    + extensionURLs.stream().collect(Collectors.joining(", ")));
        }
    }

    private void validateConstrainedOutProperties(Patient patient) {

        Set<String> invalidFields = new HashSet<>();

        // ## The above can exist in the patient resource but can be ignored. If
        // they are saved by the provider then they should be returned in the
        // response!
        if (!patient.getPhoto().isEmpty()) {
            invalidFields.add("photo");
        }
        if (!patient.getAnimal().isEmpty()) {
            invalidFields.add("animal");
        }
        if (!patient.getCommunication().isEmpty()) {
            invalidFields.add("communication");
        }
        if (!patient.getLink().isEmpty()) {
            invalidFields.add("link");
        }
        if (patient.getDeceased() != null) {
            invalidFields.add("deceased");
        }
        // 6 extra fields added at 1.2.2 
        if (patient.hasActive()) {
            invalidFields.add("active");
        }
        if (patient.hasMaritalStatus()) {
            invalidFields.add("maritalStatus");
        }
        if (patient.hasMultipleBirth()) {
            invalidFields.add("multipleBirths");
        }
        if (patient.hasContact()) {
            invalidFields.add("contact");
        }
        if (patient.hasManagingOrganization()) {
            invalidFields.add("mangingOrganization");
        }
        if (patient.hasGeneralPractitioner()) {
            invalidFields.add("generalPractitioner");
        }

        if (!invalidFields.isEmpty()) {
            String message = String.format(
                    "The following properties have been constrained out on the Patient resource - %s",
                    String.join(", ", invalidFields));
            // #250 422 INVALID_RESOURCE not 400 BAD_REQUEST
            throw OperationOutcomeFactory.buildOperationOutcomeException(new UnprocessableEntityException(message),
                    SystemCode.INVALID_RESOURCE, IssueType.INVALID);
        }
    }

    private void validateNames(Patient patient) {
        List<HumanName> names = patient.getName();

        if (names.size() < 1) {
            throwInvalidRequest400_BadRequestException("The patient must have at least one Name.");
        }

        List<HumanName> activeOfficialNames = names
                .stream()
                .filter(nm -> IsActiveName(nm))
                .filter(nm -> NameUse.OFFICIAL.equals(nm.getUse()))
                .collect(Collectors.toList());

        if (activeOfficialNames.size() != 1) {
            throwInvalidRequest400_BadRequestException("The patient must have one Active Name with a Use of OFFICIAL");
        }

        List<String> officialFamilyNames = new ArrayList<>();

        for (HumanName humanName : activeOfficialNames) {
            if (humanName.getFamily() != null) {
                officialFamilyNames.add(humanName.getFamily());
            }
        }

        validateNameCount(officialFamilyNames, "family");
    }

    private void validateNameCount(List<String> names, String nameType) {
        if (names.size() != 1) {
            throwInvalidRequest400_BadRequestException(
                    String.format("The patient must have one and only one %s name property. Found %s",
                            nameType, names.size()));
        }
    }

    private Boolean IsActiveName(HumanName name) {

        Period period = name.getPeriod();

        if (null == period) {
            return true;
        }

        Date start = period.getStart();
        Date end = period.getEnd();

        return (null == end || end.after(new Date()))
                && (null == start || start.equals(new Date()) || start.before(new Date()));
    }

    private PatientDetails registerPatientResourceConverterToPatientDetail(Patient patientResource) {
        PatientDetails patientDetails = new PatientDetails();
        HumanName name = patientResource.getNameFirstRep();

        String givenNames = name.getGiven().stream().map(n -> n.getValue()).collect(Collectors.joining(","));

        patientDetails.setForename(givenNames);

        patientDetails.setSurname(name.getFamily());
        patientDetails.setDateOfBirth(patientResource.getBirthDate());
        if (patientResource.getGender() != null) {
            patientDetails.setGender(patientResource.getGender().toString());
        }
        patientDetails.setNhsNumber(patientResource.getIdentifierFirstRep().getValue());

        DateTimeType deceased = (DateTimeType) patientResource.getDeceased();
        if (deceased != null) {
            try {
                patientDetails.setDeceased((deceased.getValue()));
            } catch (ClassCastException cce) {
                throwUnprocessableEntity422_InvalidResourceException("The multiple deceased property is expected to be a datetime");
            }
        }

        // activate patient as temporary
        patientDetails.setRegistrationStartDateTime(new Date());
        // patientDetails.setRegistrationEndDateTime(getRegistrationEndDate(patientResource));
        patientDetails.setRegistrationStatus(ACTIVE_REGISTRATION_STATUS);
        patientDetails.setRegistrationType(TEMPORARY_RESIDENT_REGISTRATION_TYPE);
        updateAddressAndTelecom(patientResource, patientDetails);

        // set some standard values for defaults, ensure managing org is always returned
        // added at 1.2.2 7 is A20047 the default GP Practice
        List<OrganizationDetails> ourOrg = organizationSearch.findOrganizationDetailsByOrgODSCode(OUR_ODS_CODE);
        patientDetails.setManagingOrganization("" + ourOrg.get(0).getId());

        return patientDetails;
    }

    /**
     * only used on register patient call
     *
     * @param patient Patient object
     * @return patient object adorned with "static" data
     */
    private Patient setStaticPatientData(Patient patient) {

        patient.setLanguage(("en-GB"));

        setStaticCommunicationData(patient);

        Identifier localIdentifier = new Identifier();
        localIdentifier.setUse(IdentifierUse.USUAL);
        localIdentifier.setSystem(SystemURL.ID_LOCAL_PATIENT_IDENTIFIER);
        localIdentifier.setValue("123456");

        localIdentifier.setType(createCodeableConcept("EN", "Employer number", SystemURL.VS_IDENTIFIER_TYPE));

        localIdentifier.setAssigner(new Reference("Organization/1"));
        patient.addIdentifier(localIdentifier);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1);
        calendar.set(2016, 1, 1);
        Period pastPeriod = new Period().setStart(calendar.getTime()).setEnd(calendar.getTime());

        patient.addName()
                .setFamily("AnotherOfficialFamilyName")
                .addGiven("AnotherOfficialGivenName")
                .setUse(NameUse.OFFICIAL)
                .setPeriod(pastPeriod);

        patient.addName()
                .setFamily("AdditionalFamily")
                .addGiven("AdditionalGiven")
                .setUse(NameUse.TEMP);

        //patient.addTelecom(staticElHelper.getValidTelecom());
        // TODO This appears to return a useless address element, only populated with use and type
        patient.addAddress(staticElHelper.getValidAddress());

        return patient;
    }

    private void setStaticCommunicationData(Patient patient) {
        // inhibited at 1.2.2
        //        patient.addExtension(createCodingExtension("CG", "Greek Cypriot", SystemURL.CS_CC_ETHNIC_CATEGORY_STU3,
        //                SystemURL.SD_CC_EXT_ETHNIC_CATEGORY));
        //        patient.addExtension(createCodingExtension("SomeSnomedCode", "Some Snomed Code",
        //                SystemURL.CS_CC_RELIGIOUS_AFFILI, SystemURL.SD_CC_EXT_RELIGIOUS_AFFILI));
        //        patient.addExtension(createCodingExtension("H", "UK Resident", SystemURL.CS_CC_RESIDENTIAL_STATUS_STU3,
        //                SystemURL.SD_CC_EXT_RESIDENTIAL_STATUS));
        //        patient.addExtension(createCodingExtension("3", "To pay hotel fees only", SystemURL.CS_CC_TREATMENT_CAT_STU3,
        //                SystemURL.SD_CC_EXT_TREATMENT_CAT));
        Extension nhsCommExtension = new Extension();
        nhsCommExtension.setUrl(SystemURL.SD_CC_EXT_NHS_COMMUNICATION);
        nhsCommExtension.addExtension(
                createCodingExtension("en", "English", SystemURL.CS_CC_HUMAN_LANG_STU3, SystemURL.SD_CC_EXT_COMM_LANGUAGE));
        nhsCommExtension.addExtension(new Extension(SystemURL.SD_CC_COMM_PREFERRED, new BooleanType(false)));
        nhsCommExtension.addExtension(createCodingExtension("RWR", "Received written",
                SystemURL.CS_CC_LANG_ABILITY_MODE_STU3, SystemURL.SD_CC_MODE_OF_COMM));
        nhsCommExtension.addExtension(createCodingExtension("E", "Excellent", SystemURL.CS_CC_LANG_ABILITY_PROFI_STU3,
                SystemURL.SD_CC_COMM_PROFICIENCY));
        nhsCommExtension.addExtension(new Extension(SystemURL.SD_CC_INTERPRETER_REQUIRED, new BooleanType(false)));

        patient.addExtension(nhsCommExtension);
    }

    private Extension createCodingExtension(String code, String display, String vsSystem, String extSystem) {

        Extension ext = new Extension(extSystem, createCodeableConcept(code, display, vsSystem));

        return ext;
    }

    /**
     *
     * @param code
     * @param display
     * @param vsSystem
     * @return CodeableConcept
     */
    public static CodeableConcept createCodeableConcept(String code, String display, String vsSystem) {

        Coding coding = new Coding();
        coding.setCode(code);
        if (display != null) {
            coding.setDisplay(display);
        }
        coding.setSystem(vsSystem);
        CodeableConcept concept = new CodeableConcept();
        concept.addCoding(coding);

        return concept;
    }

    // a cut-down Patient
    private Patient patientDetailsToRegisterPatientResourceConverter(PatientDetails patientDetails)
            throws FHIRException {
        Patient patient = patientDetailsToMinimalPatient(patientDetails);

        HumanName name = getPatientNameFromPatientDetails(patientDetails);

        patient.addName(name);

        addTelecoms(patientDetails, patient);

        patient = setStaticPatientData(patient);

        return patient;
    }

    /**
     * from details to patient
     *
     * @param patientDetails
     * @param patient fhir resource
     */
    private void addTelecoms(PatientDetails patientDetails, Patient patient) {
        for (TelecomDetails telecomDetails : patientDetails.getTelecoms()) {
            patient.addTelecom(populateTelecom(telecomDetails));
        }
    }

    private Patient patientDetailsToMinimalPatient(PatientDetails patientDetails) throws FHIRException {
        Patient patient = new Patient();

        Date lastUpdated = patientDetails.getLastUpdated() == null ? new Date() : patientDetails.getLastUpdated();

        String resourceId = String.valueOf(patientDetails.getId());
        String versionId = String.valueOf(lastUpdated.getTime());
        String resourceType = patient.getResourceType().toString();

        IdType id = new IdType(resourceType, resourceId, versionId);

        patient.setId(id);
        patient.getMeta().setVersionId(versionId);
        patient.getMeta().addProfile(SystemURL.SD_GPC_PATIENT);

        Identifier patientNhsNumber = new Identifier().setSystem(SystemURL.ID_NHS_NUMBER)
                .setValue(patientDetails.getNhsNumber());

        Extension extension = createCodingExtension("01", "Number present and verified",
                SystemURL.CS_CC_NHS_NUMBER_VERIF_STU3, SystemURL.SD_CC_EXT_NHS_NUMBER_VERIF);

        patientNhsNumber.addExtension(extension);

        patient.addIdentifier(patientNhsNumber);

        patient.setBirthDate(patientDetails.getDateOfBirth());

        String gender = patientDetails.getGender();
        if (gender != null) {
            patient.setGender(AdministrativeGender.fromCode(gender.toLowerCase(Locale.UK)));
        }

        Date registrationEndDateTime = patientDetails.getRegistrationEndDateTime();
        Date registrationStartDateTime = patientDetails.getRegistrationStartDateTime();

        Extension regDetailsExtension = new Extension(SystemURL.SD_EXTENSION_CC_REG_DETAILS);

        Period registrationPeriod = new Period().setStart(registrationStartDateTime);
        if (registrationEndDateTime != null) {
            registrationPeriod.setEnd(registrationEndDateTime);
        }

        Extension regPeriodExt = new Extension(SystemURL.SD_CC_EXT_REGISTRATION_PERIOD, registrationPeriod);
        regDetailsExtension.addExtension(regPeriodExt);

        String registrationStatusValue = patientDetails.getRegistrationStatus();
        patient.setActive(
                ACTIVE_REGISTRATION_STATUS.equals(registrationStatusValue) || null == registrationStatusValue);

        String registrationTypeValue = patientDetails.getRegistrationType();
        if (registrationTypeValue != null) {

            CodeableConcept regTypeConcept = createCodeableConcept(registrationTypeValue, "Temporary", SystemURL.CS_REGISTRATION_TYPE);
            Extension regTypeExt = new Extension(SystemURL.SD_CC_EXT_REGISTRATION_TYPE, regTypeConcept);
            regDetailsExtension.addExtension(regTypeExt);
        }

        patient.addExtension(regDetailsExtension);

        if (patientDetails.isDeceased()) {
            DateTimeType decesed = new DateTimeType(patientDetails.getDeceased());
            patient.setDeceased(decesed);
        }

        String managingOrganization = patientDetails.getManagingOrganization();
        if (managingOrganization != null) {
            patient.setManagingOrganization(new Reference("Organization/" + managingOrganization));
        }

        // for patient 2 add some contact details
        if (patientDetails.getNhsNumber().equals(patients[PATIENT_2])) {
            createContact(patient);
        }

        return patient;
    } // patientDetailsToMinimalPatient

    /**
     * add a set of contact details into the patient record NB these are
     * Contacts (related people etc) not contactpoints (telecoms)
     *
     * @param patient fhirResource object
     */
    private void createContact(Patient patient) {

        // relationships
        Patient.ContactComponent contact = new ContactComponent();
        for (String relationship : new String[]{"Emergency contact", "Next of kin", "Daughter"}) {
            CodeableConcept crelationship = new CodeableConcept();
            crelationship.setText(relationship);
            contact.addRelationship(crelationship);
        }

        // contact address
        Address address = new Address();
        address.addLine("Trevelyan Square");
        address.addLine("Boar Ln");
        address.setPostalCode("LS1 6AE");
        address.setType(AddressType.PHYSICAL);
        address.setUse(AddressUse.HOME);
        contact.setAddress(address);

        // gender
        contact.setGender(AdministrativeGender.FEMALE);

        // telecom
        ContactPoint telecom = new ContactPoint();
        telecom.setSystem(ContactPointSystem.PHONE);
        telecom.setUse(ContactPointUse.MOBILE);
        telecom.setValue("07777123123");
        contact.addTelecom(telecom);

        // Name
        HumanName name = new HumanName();
        name.addGiven("Jane");
        name.setFamily("Jackson");
        List<StringType> prefixList = new ArrayList<>();
        prefixList.add(new StringType("Miss"));
        name.setPrefix(prefixList);
        name.setText("JACKSON Jane (Miss)");
        name.setUse(NameUse.OFFICIAL);
        contact.setName(name);

        patient.addContact(contact);
    }

    private Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) throws FHIRException {
        Patient patient = patientDetailsToMinimalPatient(patientDetails);

        HumanName name = getPatientNameFromPatientDetails(patientDetails);

        patient.addName(name);

        // now returns structured address (not using text element) at 1.2.2
        ArrayList<StringType> addressLines = new ArrayList<>();
        for (int i = 0; i < min(ADDRESS_CITY_INDEX, patientDetails.getAddress().length); i++) {
            addressLines.add(new StringType(patientDetails.getAddress()[i]));
        }
        patient.addAddress().
                setUse(AddressUse.HOME).
                setType(AddressType.PHYSICAL).
                setLine(addressLines).
                setCity(patientDetails.getAddress().length > ADDRESS_CITY_INDEX ? patientDetails.getAddress()[ADDRESS_CITY_INDEX] : "").
                setDistrict(patientDetails.getAddress().length > ADDRESS_DISTRICT_INDEX ? patientDetails.getAddress()[ADDRESS_DISTRICT_INDEX] : "").
                setPostalCode(patientDetails.getPostcode());

        Long gpId = patientDetails.getGpId();
        if (gpId != null) {
            Practitioner prac = practitionerResourceProvider.getPractitionerById(new IdType(gpId));
//          HumanName practitionerName = prac.getNameFirstRep();

            Reference practitionerReference = new Reference("Practitioner/" + gpId);
            // #243 remove display from reference elements
//                    .setDisplay(practitionerName.getPrefix().get(0) + " " + practitionerName.getGivenAsSingleString() + " "
//                            + practitionerName.getFamily());
            List<Reference> ref = new ArrayList<>();
            ref.add(practitionerReference);
            patient.setGeneralPractitioner(ref);
        }

        String telephoneNumber = patientDetails.getTelephone();
        ArrayList<ContactPoint> al = new ArrayList<>();
        // defaults to home, this is from the slot in the patients table
        if (telephoneNumber != null) {
            ContactPoint telephone = new ContactPoint().
                    setSystem(ContactPointSystem.PHONE).
                    setValue(telephoneNumber).
                    setUse(ContactPointUse.HOME);

            al.add(telephone);
        }

        // tack on any from the telecoms table 1.2.2 structured
        if (patientDetails.getTelecoms() != null) {
            for (TelecomDetails telecomDetails : patientDetails.getTelecoms()) {
                al.add(populateTelecom(telecomDetails));
            }
        }
        patient.setTelecom(al);

        String managingOrganization = patientDetails.getManagingOrganization();

        if (managingOrganization != null) {
            patient.setManagingOrganization(new Reference("Organization/" + managingOrganization));
        }

        // # 163 add patient language etc
        setStaticCommunicationData(patient);

        return patient;
    } // patientDetailsToPatientResourceConverter

    private ContactPoint populateTelecom(TelecomDetails telecomDetails) {
        return new ContactPoint().
                setSystem(ContactPoint.ContactPointSystem.valueOf(telecomDetails.getSystem())).
                setUse(ContactPoint.ContactPointUse.valueOf(telecomDetails.getUseType())).
                setValue(telecomDetails.getValue());
    }

    private HumanName getPatientNameFromPatientDetails(PatientDetails patientDetails) {
        HumanName name = new HumanName();

        name.setText(patientDetails.getName()).setFamily(patientDetails.getSurname())
                .addPrefix(patientDetails.getTitle()).setUse(NameUse.OFFICIAL);

        List<String> givenNames = patientDetails.getForenames();

        givenNames.forEach((givenName) -> {
            name.addGiven(givenName);
        });

        return name;

    }

    /**
     * This is a temporary sticking plaster to ensure no duplicates are returned
     * It was spotted when patient 12 was found to be returning two Medication/2
     * resources.
     *
     * @param structuredBundle
     */
    private void removeDuplicateResources(Bundle structuredBundle) {
        // take a copy into an array so we are not accused of modifying a collection while iterating through it.
        BundleEntryComponent[] entries = structuredBundle.getEntry().toArray(new BundleEntryComponent[0]);
        HashSet<String> hs = new HashSet<>();
        for (BundleEntryComponent entry : entries) {
            if ( entry.getResource() != null && entry.getResource().getId() != null) {
                String reference = entry.getResource().getResourceType().toString() + "/" + entry.getResource().getId();
                if (!hs.contains(reference)) {
                    hs.add(reference);
                } else {
                    System.out.println("Removing duplicate entry " + reference);
                    structuredBundle.getEntry().remove(entry);
                }
            }
        }
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
                        SystemCode.INVALID_NHS_NUMBER, IssueType.VALUE);
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
                            SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueType.VALUE);
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
                    throwInvalidParameterInvalidOperationalOutcome("Invalid system code");
                }

                nhsNumber = tokenParam.getValue();
            }

            return nhsNumber;
        }

        private String fromParameters(Object source) {
            String nhsNumber = null;

            if (source instanceof Parameters) {
                Parameters parameters = (Parameters) source;
                List<ParametersParameterComponent> params = new ArrayList<>();
                params.addAll(parameters.getParameter());

                ParametersParameterComponent parameter = getParameterByName(params, "patientNHSNumber");
                if (parameter != null) {
                    nhsNumber = fromIdentifier(parameter.getValue());
                } else {
                    parameter = getParameterByName(parameters.getParameter(), "registerPatient");
                    if (parameter != null) {
                        nhsNumber = fromPatientResource(parameter.getResource());
                    } else {
                        // 1.2.4 now Http 422 Unprocessable Entity not Http 400 Invalid Request
                        throwInvalidParameterInvalidOperationalOutcome("Unable to read parameters. Expecting one of patientNHSNumber or registerPatient both of which are case-sensitive");
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

        private ParametersParameterComponent getParameterByName(List<ParametersParameterComponent> parameters,
                String parameterName) {
            ParametersParameterComponent parameter = null;

            List<ParametersParameterComponent> filteredParameters = parameters.stream()
                    .filter(currentParameter -> parameterName.equals(currentParameter.getName()))
                    .collect(Collectors.toList());

            if (filteredParameters != null) {
                if (filteredParameters.size() == 1) {
                    parameter = filteredParameters.iterator().next();
                } else if (filteredParameters.size() > 1) {
                    throwInvalidRequest400_BadRequestException("The parameter " + parameterName + " cannot be set more than once");
                }
            }

            return parameter;
        }
    }

    /**
     * Checks that the dates are ok and that end is not earlier than start
     * throws an exception on failure
     *
     * @param startDate String
     * @param endDate String (may be null if not a period
     * @param sb StringBuilder for appending derails
     */
    private void validateStartDateParamAndEndDateParam(String startDate, String endDate, StringBuilder sb) {
        Pattern dateOnlyPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        boolean result = true;
        Date now = new Date();
        try {
            checkDate(startDate, "Start", dateOnlyPattern, sb, result, now);
            checkDate(endDate, "End", dateOnlyPattern, sb, result, now);
            if (startDate != null && endDate != null) {
                Date startDt = DATE_FORMAT.parse(startDate);
                Date endDt = DATE_FORMAT.parse(endDate);
                if (endDt.before(startDt)) {
                    sb.append(" End date ").append(endDate).append(" is earlier than Start date ").append(startDate);
                    result = false;
                }
            }
        } catch (ParseException ex) {
            result = false;
        }
        if (!result) {
            throwInvalidParameterInvalidOperationalOutcome("Invalid date used " + sb.toString());
        }
    }

    /**
     * checks that date is in the correct format and is not in the future
     *
     * @param date Date object
     * @param dateLabel "start" or "end"
     * @param dateOnlyPattern regex for valid date strings
     * @param sb StringBuilder to allow appending of additional info regarding
     * the nature of the failure
     * @param result boolean
     * @param now Date object
     * @return boolean true => ok
     * @throws ParseException
     */
    private void checkDate(String date, String dateLabel, Pattern dateOnlyPattern, StringBuilder sb, boolean result, Date now) throws ParseException {
        if (date != null) {
            if (!dateOnlyPattern.matcher(date).matches()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(" ").append(dateLabel).append(" date ").append(date).append(" does not match yyyy-mm-dd");
                result = false;
            } else {
                // extra check that date is not in the future
                Date d = DATE_FORMAT.parse(date);
                if (d.after(now)) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(" ").append(dateLabel).append(" date ").append(date).append(" is in the future");
                    result = false;
                }
            }
        }
        if (!result) {
            throwInvalidParameterInvalidOperationalOutcome("Invalid date used " + sb.toString());
        }
    }

    /**
     * 422 Invalid Parameter
     *
     * @param error String
     */
    private void throwInvalidParameterInvalidOperationalOutcome(String error) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new UnprocessableEntityException(
                        error),
                SystemCode.INVALID_PARAMETER, IssueType.INVALID);
    }

    /**
     * 422 Invalid Resource
     *
     * @param error String
     */
    private void throwInvalidResourceInvalidOperationalOutcome(String error) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new UnprocessableEntityException(
                        error),
                SystemCode.INVALID_RESOURCE, IssueType.INVALID);
    }

    /**
     * base class for Clinical Area classes Supports parameter part validation
     * Initialisation of associated List resource
     */
    public abstract class AbstractClinicalArea {

        protected HashSet<String> parameterPartsSupplied;
        protected ParametersParameterComponent param = null;
        protected ParametersParameterComponent paramPart = null;

        /**
         * public constructor
         */
        public AbstractClinicalArea() {
            parameterPartsSupplied = new HashSet<>();
        }

        /**
         * default no parameter parts expected add an unsupported issue for each
         * parameter part
         *
         * @param param
         */
        public void validateParameterParts(ParametersParameterComponent param) {
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                addWarningIssue(param, pParamPart, IssueType.NOTSUPPORTED);
            }
        }

        protected void notSupported() {
            addWarningIssue(param, paramPart, IssueType.NOTSUPPORTED);
        }

        /**
         * 422 Invalid Resource #321 return INVALID_RESOURCE on repeated
         * Parameter Part
         */
        protected void repeatedParameterPart() {
            throwInvalidResourceInvalidOperationalOutcome("Repeated Parameter Part : " + paramPart.getName());
        }

        /**
         * 422 Invalid Parameter
         */
        protected void invalidType() {
            throwInvalidParameterInvalidOperationalOutcome("Invalid parameter part type : " + paramPart.getName());
        }

        /**
         * This needs a parameter (java!) because we are explicitly naming a
         * ParameterPart thats not been passed
         *
         * @param paramPartName
         */
        protected void missParameterPart(String paramPartName) {
            throwInvalidParameterInvalidOperationalOutcome("Miss parameter : " + paramPartName);
        }

        protected void noValue() {
            throwInvalidParameterInvalidOperationalOutcome("Invalid parameter part : " + paramPart.getName() + " no value element");
        }

        protected void checkMissingMandatory(String paramPartName) {
            if (!parameterPartsSupplied.contains(paramPartName)) {
                missParameterPart(paramPartName);
            }
        }

        protected String getListDisplay() {
            return ("");
        }

        protected String getListCode() {
            return ("");
        }

        public void setListMetaData(ListResource list) {
            list.setTitle(getListDisplay());
            list.setCode(createCodeableConcept(getListCode(), getListDisplay(), SystemConstants.SNOMED_URL));
        }

        /**
         * @param cls expected parameter type
         */
        protected void checkParameterPart(Class cls) {
            if (paramPart.getValue() != null) {
                if (paramPart.getValue().getClass() == cls) {
                    if (!parameterPartsSupplied.contains(paramPart.getName())) {
                        // type specific checking
                        if (cls == DateType.class) {
                            DateType dateDt = (DateType) paramPart.getValue();
                            String startDate = dateDt.asStringValue();
                            StringBuilder sb = new StringBuilder();
                            validateStartDateParamAndEndDateParam(startDate, null, sb);
                        } else if (cls == Period.class) {
                            Period period = (Period) paramPart.getValue();
                            StringBuilder sb = new StringBuilder();
                            validateStartDateParamAndEndDateParam(period.getStartElement().asStringValue(), period.getEndElement().asStringValue(), sb);
                        } else if (cls == BooleanType.class) {
                            BooleanType bool = (BooleanType) paramPart.getValue();
                        }
                        parameterPartsSupplied.add(paramPart.getName());
                    } else {
                        repeatedParameterPart();
                    }
                } else {
                    invalidType();
                }
            } else {
                noValue();
            }
        }
    }

    /**
     * Allergies 1.2 Not canned
     */
    public class AllergiesClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent param) {
            this.param = param;
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case INCLUDE_RESOLVED_ALLERGIES_PARM_PART:
                        checkParameterPart(BooleanType.class);
                        break;
                    default:
                        notSupported();
                }
            }

            checkMissingMandatory(INCLUDE_RESOLVED_ALLERGIES_PARM_PART);
        }
    }

    /**
     * Medication 1.2 Not canned
     */
    public class MedicationClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent pParam) {
            this.param = pParam;
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case INCLUDE_PRESCRIPTION_ISSUES_PARAM_PART:
                        checkParameterPart(BooleanType.class);
                        break;

                    case MEDICATION_SEARCH_FROM_DATE_PARAM_PART:
                        checkParameterPart(DateType.class);
                        break;
                    default:
                        notSupported();
                }
            }

            // #272 fail for invalid as opposed to unrecognised
            // #310 this parameter is now optional defaulting to true
            //checkMissingMandatory(INCLUDE_PRESCRIPTION_ISSUES_PARAM_PART);
        }

    }

    /**
     * Immunisations 1.3 canned but not post processed
     */
    public class ImmunisationsClinicalArea extends AbstractClinicalArea {

        public ImmunisationsClinicalArea() {
        }

        @Override
        public void validateParameterParts(ParametersParameterComponent param) {
            this.param = param;
            // optional parts named includeNotGiven and includeDissentConsent with a boolean
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case IMMUNIZATIONS_INCLUDE_NOT_GIVEN_PARAM_PART:
                    case IMMUNIZATIONS_INCLUDE_DISSENT_CONSENT_PARAM_PART:
                        checkParameterPart(BooleanType.class);
                        break;
                    default:
                        notSupported();
                }
            }
        }

        @Override
        protected String getListDisplay() {
            return SNOMED_IMMUNIZATIONS_LIST_DISPLAY;
        }

        @Override
        protected String getListCode() {
            return SNOMED_IMMUNIZATIONS_LIST_CODE;
        }
    }

    /**
     * Uncategorised Data 1.3 canned but not post processed
     */
    public class UncategorisedDataClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent param) {
            this.param = param;
            // optional part named uncategorisedDataSearchPeriod with a valuePeriod
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case UNCATEGORISED_DATA_SEARCH_PERIOD_PARAM_PART:
                        checkParameterPart(Period.class);
                        break;
                    default:
                        notSupported();
                }
            }
        }

        @Override
        protected String getListDisplay() {
            return SNOMED_UNCATEGORISED_DATA_LIST_DISPLAY;
        }

        @Override
        protected String getListCode() {
            return SNOMED_UNCATEGORISED_DATA_LIST_CODE;
        }
    }

    /**
     * Consultations 1.3 canned and post processed
     */
    public class ConsultationsClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent pParam) {
            this.param = pParam;
            // optional part named consultationSearchPeriod with a valuePeriod
            // optional part named numberOfMostRecent with a positive integer count
            // Where both are supplied is an error
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case CONSULTATION_SEARCH_PERIOD_PARAM_PART:
                        checkParameterPart(Period.class);
                        break;

                    case NUMBER_OF_MOST_RECENT_PARAM_PART:
                        if (paramPart.getValue() != null) {
                            if (paramPart.getValue() instanceof IntegerType) {
                                if (!parameterPartsSupplied.contains(paramPart.getName())) {
                                    int numberOfMostRecent = ((IntegerType) paramPart.getValue()).getValue();
                                    // #312 value is a positive :integer
                                    if (numberOfMostRecent <= 0) {
                                        throwInvalidParameterInvalidOperationalOutcome("ParameterPart " + paramPart.getName() + " must be greater than 0 : " + numberOfMostRecent);
                                    }
                                    parameterPartsSupplied.add(paramPart.getName());
                                } else {
                                    repeatedParameterPart();
                                }
                            } else {
                                invalidType();
                            }
                        } else {
                            noValue();
                        }
                        break;
                    default:
                        notSupported();
                } // switch
            } // for

            if (parameterPartsSupplied.size() > 1) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException("Incorrect combination of parameter parts passed : both " + CONSULTATION_SEARCH_PERIOD_PARAM_PART + " and "
                                + NUMBER_OF_MOST_RECENT_PARAM_PART + " were supplied."),
                        SystemCode.INVALID_RESOURCE, IssueType.INVALID);
            }
        }

        @Override
        protected String getListDisplay() {
            return SNOMED_CONSULTATION_LIST_DISPLAY;
        }

        @Override
        protected String getListCode() {
            return SNOMED_CONSULTATION_LIST_CODE;
        }
    }

    /**
     * Problems 1.3 canned and post processed
     */
    public class ProblemsClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent pParam) {
            this.param = pParam;
            // optional part named filterStatus with a valueCode eg active inactive
            // optional part named filterSignificance with a valueCode eg major minor
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case FILTER_STATUS_PARAM_PART:
                        if (paramPart.getValue() != null) {
                            if ((paramPart.getValue() instanceof CodeType)) {
                                if (!parameterPartsSupplied.contains(paramPart.getName())) {
                                    try {
                                        Condition.ConditionClinicalStatus status = Condition.ConditionClinicalStatus.valueOf(((CodeType) paramPart.getValue()).getValue().toUpperCase());
                                        switch (status) {
                                            case ACTIVE:
                                            case INACTIVE:
                                                break;
                                            default:
                                                throwInvalidParameterInvalidOperationalOutcome("Invalid parameter part passed : " + paramPart.getName() + " invalid value " + paramPart.getValue());
                                        }
                                        parameterPartsSupplied.add(paramPart.getName());
                                    } catch (IllegalArgumentException ex) {
                                        throwInvalidParameterInvalidOperationalOutcome("Invalid parameter part passed : " + paramPart.getName() + " invalid value " + paramPart.getValue());
                                    }
                                } else {
                                    repeatedParameterPart();
                                }
                            } else {
                                invalidType();
                            }
                        } else {
                            noValue();
                        }
                        break;

                    case FILTER_SIGNIFICANCE_PARAM_PART:
                        if (paramPart.getValue() != null) {
                            if ((paramPart.getValue() instanceof CodeType)) {
                                if (!parameterPartsSupplied.contains(paramPart.getName())) {
                                    String significance = ((CodeType) paramPart.getValue()).getValue();
                                    switch (significance) {
                                        case "major":
                                        case "minor":
                                            break;
                                        default:
                                            throwInvalidParameterInvalidOperationalOutcome("Invalid parameter part passed : " + paramPart.getName() + " invalid value " + paramPart.getValue());
                                    }
                                    parameterPartsSupplied.add(paramPart.getName());
                                } else {
                                    repeatedParameterPart();
                                }
                            } else {
                                invalidType();
                            }
                        } else {
                            noValue();
                        }
                        break;

                    default:
                        notSupported();
                }
            }
        }

        @Override
        protected String getListDisplay() {
            return SNOMED_PROBLEMS_LIST_DISPLAY;
        }

        @Override
        protected String getListCode() {
            return SNOMED_PROBLEMS_LIST_CODE;
        }
    }

    /**
     * Investigations 1.4
     */
    public class InvestigationsClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent pParam) {
            this.param = pParam;
            // optional parameter investigationsSearchPeriod of type period
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case INVESTIGATION_SEARCH_PERIOD_PARAM_PART:
                        checkParameterPart(Period.class);
                        break;
                    default:
                        notSupported();
                }
            }
        }

        @Override
        protected String getListDisplay() {
            return SNOMED_INVESTIGATIONS_LIST_DISPLAY;
        }

        @Override
        protected String getListCode() {
            return SNOMED_INVESTIGATIONS_LIST_CODE;
        }
    }

    /**
     * Referrals 1.4
     */
    public class ReferralsClinicalArea extends AbstractClinicalArea {

        @Override
        public void validateParameterParts(ParametersParameterComponent pParam) {
            this.param = pParam;
            // optional parameter referralSearchPeriod of type period
            for (ParametersParameterComponent pParamPart : param.getPart()) {
                paramPart = pParamPart;
                switch (paramPart.getName()) {
                    case REFERRAL_SEARCH_PERIOD_PARAM_PART:
                        checkParameterPart(Period.class);
                        break;
                    default:
                        notSupported();
                }
            }
        }

        @Override
        protected String getListDisplay() {
            return SNOMED_REFERRALS_LIST_DISPLAY;
        }

        @Override
        protected String getListCode() {
            return SNOMED_REFERRALS_LIST_CODE;
        }
    }
}
