package uk.gov.hscic.patient;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Composition;
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
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.activation.UnsupportedDataTypeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.common.util.NhsCodeValidator;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.details.search.PatientSearch;
import uk.gov.hscic.patient.html.FhirSectionBuilder;
import uk.gov.hscic.patient.html.Page;
import uk.gov.hscic.patient.html.PageSection;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.model.PatientSummary;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

@Component
public class PatientResourceProvider implements IResourceProvider {

    private static final String REGISTRATION_TYPE_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1";
    private static final String REGISTRATION_STATUS_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1";
    private static final String REGISTRATION_PERIOD_EXTENSION_URL = "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1";

    private static final List<String> MANDATORY_PARAM_NAMES = Arrays.asList("patientNHSNumber", "recordSection");
    private static final List<String> PERMITTED_PARAM_NAMES = new ArrayList<String>(MANDATORY_PARAM_NAMES) {
        {
            add("timePeriod");
        }
    };
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    @Autowired
    private PractitionerResourceProvider practitionerResourceProvider;

    @Autowired
    private OrganizationResourceProvider organizationResourceProvider;

    @Autowired
    private PatientSearch patientSearch;

    @Autowired
    private PageSectionFactory pageSectionFactory;

    @Value("${datasource.patient.intransit}")
    private String patientInTransit;

    @Value("${datasource.patient.noconsent}")
    private String patientNoConsent;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    private Patient getPatientByPatientId(TokenParam patientId) {
        PatientDetails patientDetails = patientSearch.findPatient(patientId.getValue());

        if (null == patientDetails) {
            throw new ResourceNotFoundException("No patient details found for patient ID: " + patientId,
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_PATIENT_NOT_FOUND, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                            OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
        }

        return patientDetailsToPatientResourceConverter(patientDetails);
    }

    @Operation(name = "$gpc.getcarerecord")
    public Bundle getPatientCareRecord(@ResourceParam Parameters params) throws UnsupportedDataTypeException {
        String nhsNumber = null;
        String sectionName = null;
        Date fromDate = null; // used for actual search rounded down because search start is exclusive 
        Date toDate = null; // user for actual search  end time is rounded up eg 206 means end 2016, May 2016 means end of May 2016 aso search end is exlcusive
        Date requestedFromDate = null; // used for banner
        Date requestedToDate = null; // user for banner end time is rounded up eg 206 means end 2016, May 2016 means end of May 2016

        List<String> parameters = params.getParameter()
                .stream()
                .map(Parameter::getName)
                .collect(Collectors.toList());

        if (!PERMITTED_PARAM_NAMES.containsAll(parameters)) {
            throw new UnprocessableEntityException("Invalid parameters",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                            OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                            IssueTypeEnum.INVALID_CONTENT));
        }

        if (!parameters.containsAll(MANDATORY_PARAM_NAMES)) {
            throw new InvalidRequestException("Missing parameters",
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
                    throw new InvalidRequestException("NHS Number is blank",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_NHS_NUMBER, OperationConstants.CODE_INVALID_NHS_NUMBER,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!NhsCodeValidator.nhsNumberValid(nhsNumber)) {
                    throw new InvalidRequestException("NHS number Invalid", OperationOutcomeFactory.buildOperationOutcome(
                            OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_NHS_NUMBER,
                            OperationConstants.COD_CONCEPT_RECORD_NHS_NUMBER_INVALID,
                            OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }

                PatientSummary patientSummary = patientSearch.findPatientSummary(nhsNumber);

                // https://developer.nhs.uk/apis/gpconnect-0-7-1/overview_release_notes_0_5_1.html
                // #223 S flag patients return patient not found
                if (null == patientSummary || patientSummary.isSensitive()) {
                    throw new ResourceNotFoundException("No patient details found for patient ID: " + nhsNumber,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_PATIENT_NOT_FOUND, OperationConstants.COD_CONCEPT_RECORD_NOT_FOUND,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                // #245 patient 15 no consent return forbidden 403
                if (nhsNumber != null && nhsNumber.equals(patientNoConsent)) {
                    throw new ForbiddenOperationException("No patient consent to share for patient ID: " + nhsNumber,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_NO_PATIENT_CONSENT, OperationConstants.COD_CONCEPT_RECORD_NO_PATIENT_CONSENT,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.FORBIDDEN));
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
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!sectionName.equals(sectionName.toUpperCase())) {
                    throw new UnprocessableEntityException("Section Case Invalid: " + sectionName,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_SECTION,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (!system.equals(OperationConstants.SYSTEM_RECORD_SECTION)) {
                    throw new InvalidRequestException(OperationConstants.SYSTEM_INVALID,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_SYSTEM,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }
            } else if (value instanceof PeriodDt) {
                PeriodDt period = (PeriodDt) value;

                validateDateTimeFormats(period);

                fromDate = period.getStart();
                toDate = period.getEnd();
                requestedFromDate = period.getStart();
                requestedToDate = period.getEnd();

                // #302 if both are empty this is an invalid parameter
                if (fromDate == null && toDate == null) {
                    // TODO These messages are not propagated to the diagnostics field
                    throw new UnprocessableEntityException("From date and to date are both empty",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                if (fromDate != null && toDate != null && fromDate.after(toDate)) {
                    throw new UnprocessableEntityException("Dates are invalid: " + fromDate + ", " + toDate,
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                                    OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_DATES,
                                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }

                // its not clear what is going on here we end up with toDate being a day later than requestedToDate and requestedToDate being rounded?
                // both fromDate and  requestedFromDate appear to always be identical..
                // #358 update this is complicated. The from and to date are transferred in a period structure comprised of two datetimes
                // we are storing the start date and time as requested but also copying and modifying the actual date/times as submitted to the query
                // the demonstrator validation checks against both a date regexp and timestamp regexp
                // hence the precision element can be either DAY or SECOND
                // jpa pseudo english queries where the query is derived from the method name only support the words Before or After
                // see eg EncounterSearch.findAllEncounterHTMLTables for examples of the queries
                // see https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference section 6.3.2 Table 2 for supported keywords
                // what we actually require for these queries is OnOrAfter and OnOrBefore
                // these adjustments compensate so we get the same effect.
                // if the start/end is a time subtract/add a second, if its a date subtract/add a day
                if (toDate != null) {
                    // add a day/second to toDate depending on whether its a date or a timestamp
                    switch (period.getEndElement().getPrecision()) {
                        case DAY:
                            toDate = TemporalPrecisionEnum.DAY.add(toDate, 1);
                            break;
                        case MONTH:
                            // add a month subtract a day
                            requestedToDate = TemporalPrecisionEnum.MONTH.add(requestedToDate, 1);
                            requestedToDate = TemporalPrecisionEnum.DAY.add(requestedToDate, -1);
                            toDate = TemporalPrecisionEnum.MONTH.add(toDate, 1);
                            break;
                        case YEAR:
                            // add a year subtract a day
                            requestedToDate = TemporalPrecisionEnum.YEAR.add(requestedToDate, 1);
                            requestedToDate = TemporalPrecisionEnum.DAY.add(requestedToDate, -1);
                            toDate = TemporalPrecisionEnum.YEAR.add(toDate, 1);
                            break;
                        default:
                    }
                    

                    // add a day/second to the requestedToDate
                    //requestedToDate = period.getEndElement().getPrecision().add(requestedToDate, 1);
                    // subtract a day from requestedToDate
                    //Calendar toDateCalendar = Calendar.getInstance();
                    //toDateCalendar.setTime(requestedToDate);
                    //toDateCalendar.add(Calendar.DATE, -1);
                    //requestedToDate = toDateCalendar.getTime();
                }
                
                if ( fromDate != null ) {
                    fromDate = TemporalPrecisionEnum.SECOND.add(fromDate, -1);
                }


//                System.err.println("fromDate Precision " + period.getStartElement().getPrecision() + " toDate Precision " + period.getEndElement().getPrecision());
//                System.err.println("requestedFromDate " + requestedFromDate + " requestedToDate " + requestedToDate);
//                System.err.println("fromDate (after)" + fromDate + " toDate (before)" + toDate);
            } else {
                throw new InvalidRequestException("Invalid datatype",
                        OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                                OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                                IssueTypeEnum.INVALID_CONTENT));
            }
        }

        // Build requested section
        Page page;

        final boolean IN_SUMMARY = true;

        switch (sectionName) {
            case "SUM":
                // #287 related changes to banners and filtering
                if (requestedFromDate != null || requestedToDate != null) {
                    throw new InvalidRequestException("Date Ranges not allowed to be set",
                            OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                                    OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                                    IssueTypeEnum.BUSINESS_RULE_VIOLATION));
                }

                page = new Page("Summary", sectionName);
                handlePageBanners(nhsNumber, page);

                page.addPageSection(pageSectionFactory.getENCPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate, 3));
                page.addPageSection(pageSectionFactory.getPRBActivePageSection(nhsNumber, null, null, IN_SUMMARY));
                // default for this Consumer supplied filter is assumed to be all
                page.addPageSection(pageSectionFactory.getPRBMajorInactivePageSection(nhsNumber, null, null, null, null, IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getALLCurrentPageSection(nhsNumber, null, null, IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getMEDAcuteMedicationSection(nhsNumber, null, null, IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getMEDRepeatPageSection(nhsNumber, null, null, IN_SUMMARY));
                break;

            case "PRB":
                page = new Page("Problems and Issues", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getPRBActivePageSection(nhsNumber, requestedFromDate, requestedToDate, !IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getPRBMajorInactivePageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate, !IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getPRBOtherInactivePageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "ENC":
                page = new Page("Encounters", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getENCPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "ALL":
                page = new Page("Allergies and Adverse Reactions", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getALLCurrentPageSection(nhsNumber, requestedFromDate, requestedToDate, !IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getALLHistoricalPageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "CLI":
                page = new Page("Clinical Items", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getCLIPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "MED":
                page = new Page("Medications", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getMEDAcuteMedicationSection(nhsNumber, requestedFromDate, requestedToDate, !IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getMEDRepeatPageSection(nhsNumber, requestedFromDate, requestedToDate, !IN_SUMMARY));
                page.addPageSection(pageSectionFactory.getMEDDiscontinuedRepeatPageSection(nhsNumber));
                page.addPageSection(pageSectionFactory.getMEDAllMedicationPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));
                page.addPageSection(pageSectionFactory.getMEDAllMedicationIssuesPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "REF":
                page = new Page("Referrals", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getREFPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "OBS":
                page = new Page("Observations", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getOBSPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            case "IMM":
                page = new Page("Immunisations", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getIMMPageSection(nhsNumber, requestedFromDate, requestedToDate));

                break;

            case "ADM":
                page = new Page("Administrative Items", sectionName);
                handlePageBanners(nhsNumber, page);
                page.addPageSection(pageSectionFactory.getADMPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate));

                break;

            default:
                throw new UnprocessableEntityException("Invalid section code: " + sectionName,
                        OperationOutcomeFactory.buildOperationOutcome(
                                OperationConstants.SYSTEM_WARNING_CODE,
                                OperationConstants.CODE_INVALID_PARAMETER,
                                OperationConstants.COD_CONCEPT_RECORD_INVALID_SECTION_CODE,
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

        careRecordComposition.setSection(Collections.singletonList(FhirSectionBuilder.buildFhirSection(page)));

        // Build the Care Record Composition
        Bundle bundle = new Bundle()
                // #288 change bundle type to searchset
                .setType(BundleTypeEnum.SEARCH_RESULTS)
                .addEntry(new Bundle.Entry().setResource(careRecordComposition));

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
                                OperationConstants.META_GP_CONNECT_ORGANIZATION,
                                IssueTypeEnum.NOT_FOUND));
            }

            bundle.addEntry(organizationEntry);
        }

        return bundle.addEntry(patientEntry);
    }

    /**
     * #261 add in transfer banner should be registration date at new GP but
     * default to a week ago spec says may not be populated not will not be
     * populated
     *
     * @param nhsNumber
     * @param page
     */
    private void handlePageBanners(String nhsNumber, Page page) {
        if (nhsNumber.equals(patientInTransit)) {
            Calendar cal = new GregorianCalendar();
            Date now = new Date();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            page.addGPTransferBanner(DATE_FORMAT.format(cal.getTime()));
        }
    }

    private void validateDateTimeFormats(PeriodDt period) {
        DateTimeDt startDate = period.getStartElement();
        DateTimeDt endDate = period.getEndElement();

        Pattern dateTimePattern = Pattern.compile("-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?");
        Pattern dateOnlyPattern = Pattern.compile("-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1]))?)?");

        //If the time is included then match against the date/time regex
        if ((startDate.getValueAsString() != null && startDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(startDate.getValueAsString()).matches())
                || (endDate.getValueAsString() != null && endDate.getPrecision().getCalendarConstant() > TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateTimePattern.matcher(endDate.getValueAsString()).matches())) {
            throw new UnprocessableEntityException(OperationConstants.DATE_INVALID,
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER,
                            OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        } //if only a date then match against the date regex
        else if ((startDate.getValueAsString() != null && startDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(startDate.getValueAsString()).matches())
                || (endDate.getValueAsString() != null && endDate.getPrecision().getCalendarConstant() <= TemporalPrecisionEnum.DAY.getCalendarConstant() && !dateOnlyPattern.matcher(endDate.getValueAsString()).matches())) {
            throw new UnprocessableEntityException(OperationConstants.DATE_INVALID,
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                            OperationConstants.CODE_INVALID_PARAMETER, OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER,
                            OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }

    private Patient patientDetailsToPatientResourceConverter(PatientDetails patientDetails) {
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
            throw new ResourceNotFoundException("No GP record exists",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
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
