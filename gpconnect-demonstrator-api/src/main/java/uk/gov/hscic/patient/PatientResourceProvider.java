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
import ca.uhn.fhir.model.dstu2.resource.Composition.Section;
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
import java.util.stream.Collectors;
import javax.activation.UnsupportedDataTypeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.medications.MedicationAdministrationResourceProvider;
import uk.gov.hscic.medications.MedicationDispenseResourceProvider;
import uk.gov.hscic.medications.MedicationOrderResourceProvider;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationHtmlRepository;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.adminitems.model.AdminItemData;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;
import uk.gov.hscic.patient.allergies.model.AllergyData;
import uk.gov.hscic.patient.allergies.search.AllergySearch;
import uk.gov.hscic.patient.careRecordHtml.*;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemData;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearch;
import uk.gov.hscic.patient.details.search.PatientSearch;
import uk.gov.hscic.patient.details.store.PatientStore;
import uk.gov.hscic.patient.encounters.model.EncounterData;
import uk.gov.hscic.patient.encounters.search.EncounterSearch;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;
import uk.gov.hscic.patient.immunisations.repo.ImmunisationRepository;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.repo.ObservationRepository;
import uk.gov.hscic.patient.patientsummary.search.PatientSummarySearch;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.repo.ProblemRepository;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;
import uk.gov.hscic.patient.referrals.search.ReferralSearch;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

@Component
public class PatientResourceProvider implements IResourceProvider {
    private static final String REGISTRATION_TYPE_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1";
    private static final String REGISTRATION_STATUS_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1";
    private static final String REGISTRATION_PERIOD_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1";
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
    private PatientSummarySearch patientSummarySearch;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private EncounterSearch encounterSearch;
    @Autowired
    private AllergySearch allergySearch;
    @Autowired
    private ClinicalItemSearch clinicalItemsSearch;
    @Autowired
    private MedicationHtmlRepository medicationHtmlRepository;
    @Autowired
    private ReferralSearch referralSearch;
    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    private InvestigationRepository investigationRepository;
    @Autowired
    private ImmunisationRepository immunisationRepository;
    @Autowired
    private AdminItemSearch adminItemSearch;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient getPatientById(@IdParam IdDt internalId) {
        PatientDetails patientDetails = patientSearch.findPatientByInternalID(internalId.getIdPart());

        if (patientDetails == null) {
            throw new ResourceNotFoundException("No patient details found for patient ID: " + internalId.getIdPart(),
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_PATIENT_NOT_FOUND, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                            OperationConstants.META_GP_CONNECT_PATIENT, IssueTypeEnum.NOT_FOUND));
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
            throw new ResourceNotFoundException("No patient details found for patient ID: ",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_PATIENT_NOT_FOUND, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                            OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
        }

        return patientDetailsToPatientResourceConverter(patientDetails);
    }

    @SuppressWarnings("deprecation")
    @Operation(name = "$gpc.getcarerecord")
    public Bundle getPatientCareRecord(@ResourceParam Parameters params) throws UnsupportedDataTypeException {
        String nhsNumber = null;
        String sectionName = null;
        Date fromDate = null;
        Date toDate = null;
        Date requestedFromDate = null;
        Date requestedToDate = null;

        List<String> parameters = params.getParameter()
                .stream()
                .map(Parameter::getName)
                .collect(Collectors.toList());

        if (!PERMITTED_PARAM_NAMES.containsAll(parameters) || !parameters.containsAll(MANDATORY_PARAM_NAMES)) {
            throw new UnprocessableEntityException("Parameters are incorrect",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                            OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                            IssueTypeEnum.INVALID_CONTENT));
        }

        for (Parameter param : params.getParameter()) {
            IDatatype value = param.getValue();

            if (value instanceof IdentifierDt) {
                if (null != nhsNumber) {
                    throw new InvalidRequestException("Bad Request Exception",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_IDENTIFIER_SYSTEM,
                                    OperationConstants.COD_CONCEPT_RECORD_NHS_NUMBER_INVALID,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                                    IssueTypeEnum.INVALID_CONTENT));
                }

                nhsNumber = ((IdentifierDt) value).getValue();

                if (StringUtils.isBlank(nhsNumber)) {
                    throw new InvalidRequestException(OperationConstants.SYSTEM_INVALID,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_NHS_NUMBER, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                    throw new InvalidRequestException("NHS number Invalid " + OperationOutcomeFactory.buildOperationOutcome(
                            OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_NHS_NUMBER,
                            OperationConstants.COD_CONCEPT_RECORD_NHS_NUMBER_INVALID,
                            OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }
            } else if (value instanceof CodeableConceptDt) {
                if (null != sectionName) {
                    throw new InvalidRequestException("Bad Request Exception",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_IDENTIFIER_SYSTEM,
                                    OperationConstants.COD_CONCEPT_RECORD_MULTIPLE_SECTIONS_ADDED,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                                    IssueTypeEnum.INVALID_CONTENT));
                }

                CodingDt coding = ((CodeableConceptDt) value).getCodingFirstRep();

                String system = coding.getSystem();
                sectionName = coding.getCode();

                if (system == null || sectionName == null) {
                    throw new UnprocessableEntityException(OperationConstants.SYSTEM_INVALID,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!sectionName.equals(sectionName.toUpperCase())) {
                    throw new UnprocessableEntityException("Section Case Invalid: ",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!system.equals(OperationConstants.SYSTEM_RECORD_SECTION)) {
                    throw new InvalidRequestException(OperationConstants.SYSTEM_INVALID);
                }
            } else if (value instanceof PeriodDt) {
                PeriodDt period = (PeriodDt) value;

                fromDate = period.getStart();
                toDate = period.getEnd();
                requestedFromDate = period.getStart();
                requestedToDate = period.getEnd();

                if (fromDate != null && toDate != null && fromDate.after(toDate)) {
                    throw new UnprocessableEntityException("Dates are invalid: ",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (toDate != null) {
                    toDate = period.getEndElement().getPrecision().add(toDate, 1);

                    requestedToDate = period.getEndElement().getPrecision().add(requestedToDate, 1);
                    Calendar toDateCalendar = Calendar.getInstance();
                    toDateCalendar.setTime(requestedToDate);
                    toDateCalendar.add(Calendar.DATE, -1);
                    requestedToDate = toDateCalendar.getTime();
                }
            }
        }

        // Build requested section
        String html;
        HtmlPage htmlPage;
        List<Section> sectionsList = new ArrayList<>();

        switch (sectionName) {
            case "SUM":
                String patientSummaryHtml = patientSummarySearch.findPatientSummaryHtml(nhsNumber);

                if (patientSummaryHtml != null) {
                    if (patientSummaryHtml.contains("This is confidential")) {
                        throw new ForbiddenOperationException("This Data Is Confidential",
                                OperationOutcomeFactory.buildOperationOutcome(
                                        OperationConstants.SYSTEM_WARNING_CODE,
                                        OperationConstants.CODE_NO_PATIENT_CONSENT,
                                        OperationConstants.COD_CONCEPT_RECORD_PATIENT_DATA_CONFIDENTIAL,
                                        OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                                        IssueTypeEnum.NOT_FOUND));
                    } else {
                        html = patientSummaryHtml;
                    }
                } else {
                    html = BuildHtmlTable.buildEmptyHtml(OperationConstants.SUMMARY);
                }

                sectionsList.add(SectionsCreationClass.buildSection(
                        OperationConstants.SYSTEM_RECORD_SECTION, sectionName, html,
                        OperationConstants.SUMMARY, OperationConstants.SUMMARY));

                break;

            case "PRB":
                if (toDate != null && fromDate != null) {
                    throw new InvalidRequestException(OperationConstants.DATE_RANGES_NOT_ALLOWED);
                }

                List<List<Object>> problemActiveRows = new ArrayList<>();
                List<List<Object>> problemInactiveRows = new ArrayList<>();

                for (ProblemEntity problem : problemRepository.findBynhsNumber(nhsNumber)) {
                    if ("Active".equals(problem.getActiveOrInactive())) {
                        problemActiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
                    } else {
                        problemInactiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEndDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
                    }
                }

                PageSection activeProblems = new PageSection("Active Problems and Issues");
                activeProblems.setTable(new PageSectionHtmlTable(Arrays.asList("Start Date", "Entry", "Significance", "Details"), problemActiveRows));

                PageSection inactiveProblems = new PageSection("Inactive Problems and Issues");
                inactiveProblems.setTable(new PageSectionHtmlTable(Arrays.asList("Start Date", "End Date", "Entry", "Significance", "Details"), problemInactiveRows));

                htmlPage = new HtmlPage("Problems", sectionName);
                htmlPage.addPageSection(activeProblems);
                htmlPage.addPageSection(inactiveProblems);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "ENC":
                List<EncounterData> encounterList = encounterSearch.findAllEncounterHTMLTables(nhsNumber, fromDate, toDate);
                List<List<Object>> encounterRows = new ArrayList<>();

                if (encounterList != null) {
                    for (EncounterData encounter : encounterList) {
                        encounterRows.add(Arrays.asList(encounter.getEncounterDate(), encounter.getTitle(), encounter.getDetails()));
                    }
                }

                PageSection encountersSection = new PageSection("Encounters");
                encountersSection.setDateRange(requestedFromDate, requestedToDate);
                encountersSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "Title", "Details"), encounterRows));

                htmlPage = new HtmlPage("Encounters", sectionName);
                htmlPage.addPageSection(encountersSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "ALL":
                if (toDate != null && fromDate != null) {
                    throw new InvalidRequestException(OperationConstants.DATE_RANGES_NOT_ALLOWED);
                }

                List<List<Object>> currentAllergyRows = new ArrayList<>();
                List<List<Object>> historicalAllergyRows = new ArrayList<>();
                List<AllergyData> allergyList = allergySearch.findAllAllergyHTMLTables(nhsNumber);

                if (allergyList != null) {
                    for (AllergyData allergyData : allergyList) {
                        if ("Current".equals(allergyData.getCurrentOrHistoric())) {
                            currentAllergyRows.add(Arrays.asList(allergyData.getStartDate(), allergyData.getDetails()));
                        } else {
                            historicalAllergyRows.add(Arrays.asList(allergyData.getStartDate(), allergyData.getEndDate(), allergyData.getDetails()));
                        }
                    }
                }

                PageSection currentAllergiesSection = new PageSection("Current Allergies and Adverse Reactions");
                currentAllergiesSection.setTable(new PageSectionHtmlTable(Arrays.asList("Start Date", "Details"), currentAllergyRows));

                PageSection historicalAllergiesSection = new PageSection("Historical Allergies and Adverse Reactions");
                historicalAllergiesSection.setTable(new PageSectionHtmlTable(Arrays.asList("Start Date", "End Date", "Details"), historicalAllergyRows));

                htmlPage = new HtmlPage("Allergies and Adverse Reactions", sectionName);
                htmlPage.addPageSection(currentAllergiesSection);
                htmlPage.addPageSection(historicalAllergiesSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "CLI":
                List<ClinicalItemData> clinicalItemList = clinicalItemsSearch.findAllClinicalItemHTMLTables(nhsNumber, fromDate, toDate);
                List<List<Object>> clinicalItemsRows = new ArrayList<>();

                if (clinicalItemList != null) {
                    for (ClinicalItemData clinicalItemData : clinicalItemList) {
                        clinicalItemsRows.add(Arrays.asList(clinicalItemData.getDate(), clinicalItemData.getEntry(), clinicalItemData.getDetails()));
                    }
                }

                PageSection clinicalItemsSection = new PageSection("Clinical Items");
                clinicalItemsSection.setDateRange(requestedFromDate, requestedToDate);
                clinicalItemsSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "Entry", "Details"), clinicalItemsRows));

                htmlPage = new HtmlPage("Clinical Items", sectionName);
                htmlPage.addPageSection(clinicalItemsSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "MED":
                if (toDate != null && fromDate != null) {
                    throw new InvalidRequestException(OperationConstants.DATE_RANGES_NOT_ALLOWED);
                }

                List<List<Object>> currentMedRows = new ArrayList<>();
                List<List<Object>> repeatMedRows = new ArrayList<>();
                List<List<Object>> pastMedRows = new ArrayList<>();

                for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumber(nhsNumber)) {
                    switch (patientMedicationHtmlEntity.getCurrentRepeatPast()) {
                        case "Current":
                            currentMedRows
                                    .add(Arrays.asList(patientMedicationHtmlEntity.getStartDate(),
                                            patientMedicationHtmlEntity.getMedicationItem(),
                                            patientMedicationHtmlEntity.getTypeMed(),
                                            patientMedicationHtmlEntity.getScheduledEnd(),
                                            patientMedicationHtmlEntity.getDaysDuration(),
                                            patientMedicationHtmlEntity.getDetails()));
                            break;

                        case "Repeat":
                            repeatMedRows
                                    .add(Arrays.asList(patientMedicationHtmlEntity.getLastIssued(),
                                            patientMedicationHtmlEntity.getMedicationItem(),
                                            patientMedicationHtmlEntity.getStartDate(),
                                            patientMedicationHtmlEntity.getReviewDate(),
                                            patientMedicationHtmlEntity.getNumberIssued(),
                                            patientMedicationHtmlEntity.getMaxIssues(),
                                            patientMedicationHtmlEntity.getDetails()));
                            break;

                        case "Past":
                            pastMedRows
                                    .add(Arrays.asList(patientMedicationHtmlEntity.getStartDate(),
                                            patientMedicationHtmlEntity.getMedicationItem(),
                                            patientMedicationHtmlEntity.getTypeMed(),
                                            patientMedicationHtmlEntity.getLastIssued(),
                                            patientMedicationHtmlEntity.getReviewDate(),
                                            patientMedicationHtmlEntity.getNumberIssued(),
                                            patientMedicationHtmlEntity.getMaxIssues(),
                                            patientMedicationHtmlEntity.getDetails()));
                            break;
                    }
                }

                PageSection currentMedSection = new PageSection("Current Medication Issues");
                currentMedSection.setTable(new PageSectionHtmlTable(Arrays.asList("Start Date", "Medication Item", "Type", "Scheduled End Date", "Days Duration", "Details"), currentMedRows));

                PageSection repeatMedSection = new PageSection("Current Repeat Medications");
                repeatMedSection.setTable(new PageSectionHtmlTable(Arrays.asList("Last Issued", "Medication Item", "Start Date", "Review Date", "Number Issued", "Max Issues", "Details"), repeatMedRows));

                PageSection pastMedSection = new PageSection("Past Medications");
                pastMedSection.setTable(new PageSectionHtmlTable(Arrays.asList("Start Date", "Medication Item", "Type", "Last Issued", "Review Date", "Number Issued", "Max Issues", "Details"), pastMedRows));

                htmlPage = new HtmlPage("Medications", sectionName);
                htmlPage.addPageSection(currentMedSection);
                htmlPage.addPageSection(repeatMedSection);
                htmlPage.addPageSection(pastMedSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "REF":
                List<List<Object>> referralRows = new ArrayList<>();

                for (ReferralEntity referralEntity : referralSearch.findReferrals(nhsNumber, fromDate, toDate)) {
                    referralRows.add(Arrays.asList(
                            referralEntity.getSectionDate(),
                            referralEntity.getFrom(),
                            referralEntity.getTo(),
                            referralEntity.getPriority(),
                            referralEntity.getDetails()));
                }

                PageSection referralSection = new PageSection("Referrals");
                referralSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "From", "To", "Priority", "Details"), referralRows));

                htmlPage = new HtmlPage("Referrals", sectionName);
                htmlPage.addPageSection(referralSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "OBS":
                if (toDate != null && fromDate != null) {
                    throw new InvalidRequestException(OperationConstants.DATE_RANGES_NOT_ALLOWED);
                }

                List<List<Object>> observationRows = new ArrayList<>();

                for (ObservationEntity observationEntity : observationRepository.findBynhsNumber(nhsNumber)) {
                    observationRows.add(Arrays.asList(
                            observationEntity.getObservationDate(),
                            observationEntity.getEntry(),
                            observationEntity.getValue(),
                            observationEntity.getValue()));
                }

                PageSection observationSection = new PageSection("Observations");
                observationSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "Entry", "Value", "Details"), observationRows));

                htmlPage = new HtmlPage("Observations", sectionName);
                htmlPage.addPageSection(observationSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "INV":
                List<List<Object>> investigationRows = new ArrayList<>();

                for (InvestigationEntity investigationEntity : investigationRepository.findByNhsNumber(nhsNumber)) {
                    investigationRows.add(Arrays.asList(
                            investigationEntity.getDate(),
                            investigationEntity.getTitle(),
                            investigationEntity.getDetails()));
                }

                PageSection investigationSection = new PageSection("Investigations");
                investigationSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "Title", "Details"), investigationRows));

                htmlPage = new HtmlPage("Investigations", sectionName);
                htmlPage.addPageSection(investigationSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "IMM":
                if (toDate != null && fromDate != null) {
                    throw new InvalidRequestException(OperationConstants.DATE_RANGES_NOT_ALLOWED);
                }

                List<List<Object>> immunisationRows = new ArrayList<>();

                for (ImmunisationEntity immunisationEntity : immunisationRepository.findByNhsNumber(nhsNumber)) {
                    immunisationRows.add(Arrays.asList(
                            immunisationEntity.getDateOfVac(),
                            immunisationEntity.getVaccination(),
                            immunisationEntity.getPart(),
                            immunisationEntity.getContents(),
                            immunisationEntity.getDetails()));
                }

                PageSection immunisationSection = new PageSection("Immunisations");
                immunisationSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "Vaccination", "Part", "Contents", "Details"), immunisationRows));

                htmlPage = new HtmlPage("Immunisations", sectionName);
                htmlPage.addPageSection(immunisationSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            case "ADM":
                List<List<Object>> adminItemsRows = new ArrayList<>();
                List<AdminItemData> adminItemList = adminItemSearch.findAllAdminItemHTMLTables(nhsNumber, fromDate, toDate);

                if (adminItemList != null) {
                    for (AdminItemData adminItemData : adminItemList) {
                        adminItemsRows.add(Arrays.asList(adminItemData.getAdminDate(), adminItemData.getEntry(), adminItemData.getDetails()));
                    }
                }

                PageSection administativeItemsSection = new PageSection("Administrative Items");
                administativeItemsSection.setDateRange(requestedFromDate, requestedToDate);
                administativeItemsSection.setTable(new PageSectionHtmlTable(Arrays.asList("Date", "Entry", "Details"), adminItemsRows));

                htmlPage = new HtmlPage("Administrative Items", sectionName);
                htmlPage.addPageSection(administativeItemsSection);
                sectionsList.add(FhirSectionBuilder.build(htmlPage));

                break;

            default:
                throw new UnprocessableEntityException("Dates are invalid: ",
                        OperationOutcomeFactory.buildOperationOutcome(
                                OperationConstants.SYSTEM_WARNING_CODE,
                                OperationConstants.CODE_INVALID_PARAMETER,
                                OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                                OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                                IssueTypeEnum.NOT_FOUND));
        }

        // Build the Patient Resource and add it to the bundle
        Patient patient = getPatientByPatientId(new TokenParam("", nhsNumber));
        String patientId = patient.getId().getIdPart();

        Bundle.Entry patientEntry = new Bundle.Entry()
                .setResource(patient)
                .setFullUrl("Patient/" + patientId);

        CodingDt coding = new CodingDt()
                .setSystem("http://snomed.info/sct")
                .setCode("425173008")
                .setDisplay("record extract (record artifact)");

        CodeableConceptDt codableConcept = new CodeableConceptDt()
                .addCoding(coding)
                .setText("record extract (record artifact)");

        CodingDt classCoding = new CodingDt()
                .setSystem("http://snomed.info/sct")
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

        careRecordComposition.getMeta().addProfile(OperationConstants.META_GP_CONNECT_CARERECORD_COMPOSITION);

        careRecordComposition.setSection(sectionsList);

        // Build the Care Record Composition
        Bundle bundle = new Bundle().setType(BundleTypeEnum.DOCUMENT);

        bundle.addEntry(new Bundle.Entry().setResource(careRecordComposition));

        List<ResourceReferenceDt> careProviderPractitionerList = ((Patient) patientEntry.getResource()).getCareProvider();

        if (!careProviderPractitionerList.isEmpty()) {
            String id = careProviderPractitionerList.get(0).getReference().getValue();
            careRecordComposition.setAuthor(Collections.singletonList(new ResourceReferenceDt(id)));

            Practitioner practitioner = practitionerResourceProvider.getPractitionerById(new IdDt(id));

            if (practitioner == null) {
                throw new ResourceNotFoundException("Practitioner Reference returning null",
                        OperationOutcomeFactory.buildOperationOutcome(
                                OperationConstants.SYSTEM_WARNING_CODE,
                                OperationConstants.CODE_REFERENCE_NOT_FOUND,
                                OperationConstants.COD_CONCEPT_RECORD_INVALID_REFERENCE,
                                OperationConstants.META_GP_CONNECT_PRACTITIONER,
                                IssueTypeEnum.NOT_FOUND));
            }

            practitioner.getMeta().addProfile(OperationConstants.META_GP_CONNECT_PRACTITIONER);

            bundle.addEntry(new Bundle.Entry().setResource(practitioner).setFullUrl(id));

            IdDt organizationId = practitioner.getPractitionerRoleFirstRep().getManagingOrganization().getReference();

            Bundle.Entry organizationEntry = new Bundle.Entry()
                    .setResource(organizationResourceProvider.getOrganizationById(organizationId))
                    .setFullUrl(organizationId);

            if (organizationEntry.getResource() == null || organizationEntry.getFullUrl() == null) {
                throw new ResourceNotFoundException("organizationResource returning null",
                        OperationOutcomeFactory.buildOperationOutcome(
                                OperationConstants.SYSTEM_WARNING_CODE,
                                OperationConstants.CODE_REFERENCE_NOT_FOUND,
                                OperationConstants.COD_CONCEPT_RECORD_INVALID_REFERENCE,
                                OperationConstants.META_GP_CONNECT_PRACTITIONER,
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
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                            OperationConstants.META_GP_CONNECT_PRACTITIONER, IssueTypeEnum.NOT_FOUND));
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
                .getUndeclaredExtensionsByUrl(REGISTRATION_PERIOD_EXTENSION_URL);
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
        Patient patient = new Patient()
                .addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", patientDetails.getNhsNumber()))
                .setBirthDate(new DateDt(patientDetails.getDateOfBirth()))
                .setGender(AdministrativeGenderEnum.forCode(patientDetails.getGender().toLowerCase()));

        patient.setId(patientDetails.getId());
        patient.addName().addFamily(patientDetails.getSurname()).addGiven(patientDetails.getForename()).setUse(NameUseEnum.USUAL);

        PeriodDt registrationPeriod = new PeriodDt()
                .setStartWithSecondsPrecision(patientDetails.getRegistrationStartDateTime())
                .setEndWithSecondsPrecision(patientDetails.getRegistrationEndDateTime());
        patient.addUndeclaredExtension(true, REGISTRATION_PERIOD_EXTENSION_URL, registrationPeriod);

        patient.addUndeclaredExtension(true, REGISTRATION_STATUS_EXTENSION_URL, new CodeableConceptDt(
                "http://fhir.nhs.net/ValueSet/registration-status-1", patientDetails.getRegistrationStatus()));

        patient.addUndeclaredExtension(true, REGISTRATION_TYPE_EXTENSION_URL, new CodeableConceptDt(
                "http://fhir.nhs.net/ValueSet/registration-type-1", patientDetails.getRegistrationType()));

        return patient;
    }

    public Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) {
        Patient patient = new Patient();
        patient.setId(patientDetails.getId());
        patient.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/nhs-number", patientDetails.getNhsNumber()));

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
        patient.getMeta().addProfile(OperationConstants.META_GP_CONNECT_PATIENT);

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
            throw new ResourceNotFoundException("No GP record exists "
                    + OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_PATIENT_NOT_FOUND, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                            OperationConstants.META_GP_CONNECT_PRACTITIONER, IssueTypeEnum.NOT_FOUND));
        }

        String gender = patientDetails.getGender();
        if (gender != null) {
            patient.setGender(AdministrativeGenderEnum.forCode(gender.toLowerCase()));
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

            patient.addUndeclaredExtension(true, REGISTRATION_PERIOD_EXTENSION_URL, registrationPeriod);
        }

        String registrationStatusValue = patientDetails.getRegistrationStatus();
        if (registrationStatusValue != null) {
            patient.addUndeclaredExtension(true, REGISTRATION_STATUS_EXTENSION_URL, new CodeableConceptDt(
                    "http://fhir.nhs.net/ValueSet/registration-status-1", registrationStatusValue));
        }

        String registrationTypeValue = patientDetails.getRegistrationType();
        if (registrationTypeValue != null) {
            patient.addUndeclaredExtension(true, REGISTRATION_TYPE_EXTENSION_URL, new CodeableConceptDt(
                    "http://fhir.nhs.net/ValueSet/registration-type-1", registrationTypeValue));
        }

        return patient;
    }
}
