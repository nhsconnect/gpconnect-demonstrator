package uk.gov.hscic.patient;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationHtmlRepository;
import uk.gov.hscic.medications.search.MedicationSearch;
import uk.gov.hscic.patient.adminitems.model.AdminItemData;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.repo.AllergyRepository;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemData;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearch;
import uk.gov.hscic.patient.encounters.model.EncounterData;
import uk.gov.hscic.patient.encounters.search.EncounterSearch;
import uk.gov.hscic.patient.html.PageSection;
import uk.gov.hscic.patient.html.Table;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;
import uk.gov.hscic.patient.immunisations.repo.ImmunisationRepository;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.repo.ObservationRepository;
import uk.gov.hscic.patient.observations.search.ObservationSearch;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;
import uk.gov.hscic.patient.referrals.search.ReferralSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class PageSectionFactory {

    @Autowired
    private ProblemSearch problemSearch;

    @Autowired
    private EncounterSearch encounterSearch;

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private ClinicalItemSearch clinicalItemsSearch;

    @Autowired
    private MedicationHtmlRepository medicationHtmlRepository;

    @Autowired
    private ReferralSearch referralSearch;

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private ObservationSearch observationSearch;

    @Autowired
    private MedicationSearch medicationSearch;

    @Autowired
    private InvestigationRepository investigationRepository;

    @Autowired
    private ImmunisationRepository immunisationRepository;

    @Autowired
    private AdminItemSearch adminItemSearch;

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
    private final static SimpleDateFormat DATE_FORMAT_SQL = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * overload default to dont check for a date range
     *
     * @param pageSection
     */
    private void addDateRangeBanners(PageSection pageSection) {
        addDateRangeBanners(pageSection, false);
    }

    /**
     * TODO the start only and end only dates are not surrounded by quotes
     * unlike the range
     *
     * @param pageSection
     */
    private void addDateRangeBanners(PageSection pageSection, boolean isDateRange) {
        StringBuilder sb = new StringBuilder();
        if (isDateRange) {
            sb.append("Date filter not applied"); // #251 for subsections not applying a date range
        } else {
            // Date Range Banner
            if (pageSection.getFromDate() != null && pageSection.getToDate() != null) {
                sb.append("For the period '").append(DATE_FORMAT.format(pageSection.getFromDate())).append("' to '").append(DATE_FORMAT.format(pageSection.getToDate())).append("'");
            } else if (pageSection.getFromDate() != null && pageSection.getToDate() == null) {
                // #224, #255 add quotes
                sb.append("All data items from '").append(DATE_FORMAT.format(pageSection.getFromDate())).append("'");
            } else if (pageSection.getFromDate() == null && pageSection.getToDate() != null) {
                // #224, #255 add quotes
                sb.append("All data items until '").append(DATE_FORMAT.format(pageSection.getToDate())).append("'");
            } else {
                // #225 change text on banner
                sb.append("All relevant items");
            }
        }
        pageSection.addBanner(sb.toString());
    }

    /**
     * Active Problems date range does not apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getPRBActivePageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> problemActiveRows = new ArrayList<>();
        for (ProblemEntity problem : problemSearch.findProblems(nhsNumber)) {
            if ("Active".equals(problem.getActiveOrInactive())) {
                problemActiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
            }
        }
        PageSection ps = new PageSection("Active Problems and Issues",
                "prb-tab-act",
                new Table(Arrays.asList("Start Date", "Entry", "Significance", "Details"), problemActiveRows));
        addDateRangeBanners(ps, requestedFromDate != null || requestedToDate != null);  // #251 Date filter not applied to subsection
        return ps;
    }

    /**
     * 0.7 only Major Inactive Problems Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getPRBMajorInactivePageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> problemInactiveRows = new ArrayList<>();

        List<ProblemEntity> findProblems = problemSearch.findProblems(nhsNumber, fromDate, toDate);
        for (ProblemEntity problem : findProblems) {
            if (!"Active".equals(problem.getActiveOrInactive()) && problem.isMajor()) {
                problemInactiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEndDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
            }
        }

        PageSection ps = new PageSection("Major Inactive Problems and Issues",
                "prb-tab-majinact",
                new Table(Arrays.asList("Start Date", "End Date", "Entry", "Significance", "Details"), problemInactiveRows),
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * 0.7 only Minor Inactive Problems Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getPRBOtherInactivePageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> problemInactiveRows = new ArrayList<>();

        List<ProblemEntity> findProblems = problemSearch.findProblems(nhsNumber, fromDate, toDate);
        for (ProblemEntity problem : findProblems) {
            if (!"Active".equals(problem.getActiveOrInactive()) && !problem.isMajor()) {
                problemInactiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEndDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
            }
        }

        PageSection ps = new PageSection("Other Inactive Problems and Issues",
                "prb-tab-othinact",
                new Table(Arrays.asList("Start Date", "End Date", "Entry", "Significance", "Details"), problemInactiveRows),
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * overload default all rows Encounters
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getENCPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        return getENCPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate, -1);
    }

    /**
     * Encounters Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @param maxRows to return -1 for all
     * @return PageSection
     */
    public PageSection getENCPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate, int maxRows) {
        List<EncounterData> encounterList = encounterSearch.findAllEncounterHTMLTables(nhsNumber, fromDate, toDate);
        List<List<Object>> encounterRows = new ArrayList<>();

        if (encounterList != null) {
            int i = 0;
            for (EncounterData encounter : encounterList) {
                encounterRows.add(Arrays.asList((Object) encounter.getEncounterDate(), encounter.getTitle(), encounter.getDetails()));
                i++;
                if (maxRows != -1 && i >= maxRows) {
                    break;
                }
            }
        }

        PageSection ps = new PageSection(maxRows != -1 ? "Last " + maxRows + " Encounters" : "Encounters",
                "enc-tab",
                new Table(Arrays.asList("Date", "Title", "Details"), encounterRows),
                requestedFromDate, requestedToDate, maxRows == -1);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Current Allergies Date range does not apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getALLCurrentPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        if (requestedFromDate != null || requestedToDate != null) {
            throw new InvalidRequestException("Date Ranges not allowed to be set",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                            OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                            IssueTypeEnum.BUSINESS_RULE_VIOLATION));
        }

        List<List<Object>> currentAllergyRows = new ArrayList<>();

        for (AllergyEntity allergyEntity : allergyRepository.findByNhsNumberOrderByStartDateDesc(nhsNumber)) {
            if ("Current".equals(allergyEntity.getCurrentOrHistoric())) {
                currentAllergyRows.add(Arrays.asList(allergyEntity.getStartDate(), allergyEntity.getDetails()));
            }
        }

        PageSection ps = new PageSection("Current Allergies and Adverse Reactions",
                "all-tab-curr",
                new Table(Arrays.asList("Start Date", "Details"), currentAllergyRows),
                requestedFromDate, requestedToDate);

        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Historical Allergies Date range does not apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getALLHistoricalPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        if (requestedToDate != null || requestedFromDate != null) {
            throw new InvalidRequestException("Date Ranges not allowed to be set",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                            OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                            IssueTypeEnum.BUSINESS_RULE_VIOLATION));
        }

        List<List<Object>> historicalAllergyRows = new ArrayList<>();

        for (AllergyEntity allergyEntity : allergyRepository.findByNhsNumberOrderByStartDateDesc(nhsNumber)) {
            if (!"Current".equals(allergyEntity.getCurrentOrHistoric())) {
                historicalAllergyRows.add(Arrays.asList(allergyEntity.getStartDate(), allergyEntity.getEndDate(), allergyEntity.getDetails()));
            }
        }

        PageSection ps = new PageSection("Historical Allergies and Adverse Reactions",
                "all-tab-hist",
                new Table(Arrays.asList("Start Date", "End Date", "Details"), historicalAllergyRows),
                requestedFromDate, requestedToDate);

        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Clinical Items Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getCLIPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<ClinicalItemData> clinicalItemList = clinicalItemsSearch.findAllClinicalItemHTMLTables(nhsNumber, fromDate, toDate);
        List<List<Object>> clinicalItemsRows = new ArrayList<>();

        if (clinicalItemList != null) {
            for (ClinicalItemData clinicalItemData : clinicalItemList) {
                clinicalItemsRows.add(Arrays.asList(clinicalItemData.getDate(), clinicalItemData.getEntry(), clinicalItemData.getDetails()));
            }
        }

        PageSection ps = new PageSection("Clinical Items",
                "cli-tab",
                new Table(Arrays.asList("Date", "Entry", "Details"), clinicalItemsRows),
                requestedFromDate, requestedToDate, true);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * 0.7 only Acute Medications order by start date desc Date range does not
     * apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getMEDAcuteMedicationSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> currentMedRows = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberOrderByStartDateDesc(nhsNumber)) {
            if ("Current".equals(patientMedicationHtmlEntity.getCurrentRepeatPast())) {
                currentMedRows.add(Arrays.asList(
                        patientMedicationHtmlEntity.getTypeMed(),
                        patientMedicationHtmlEntity.getStartDate(),
                        patientMedicationHtmlEntity.getMedicationItem(),
                        patientMedicationHtmlEntity.getDosageInstruction(),
                        patientMedicationHtmlEntity.getQuantity(),
                        patientMedicationHtmlEntity.getScheduledEnd(),
                        patientMedicationHtmlEntity.getDaysDuration(),
                        patientMedicationHtmlEntity.getDetails()));
            }
        }

        PageSection ps = new PageSection("Acute Medication (Last 12 Months)",
                "med-tab-acu-med",
                new Table(Arrays.asList(
                        "Type",
                        "Start Date",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Scheduled End Date",
                        "Days Duration",
                        "Additional Information"), currentMedRows));
        addDateRangeBanners(ps, requestedFromDate != null || requestedToDate != null);  // #251 Date filter not applied to subsection
        return ps;
    }

    /**
     * Current Repeat Medications order by start date desc Date range does not
     * apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getMEDRepeatPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> repeatMedRows = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberOrderByStartDateDesc(nhsNumber)) {
            if ("Repeat".equals(patientMedicationHtmlEntity.getCurrentRepeatPast())) {

                repeatMedRows.add(Arrays.asList(
                        patientMedicationHtmlEntity.getTypeMed(),
                        patientMedicationHtmlEntity.getStartDate(),
                        patientMedicationHtmlEntity.getMedicationItem(),
                        patientMedicationHtmlEntity.getDosageInstruction(),
                        patientMedicationHtmlEntity.getQuantity(),
                        patientMedicationHtmlEntity.getLastIssued(),
                        patientMedicationHtmlEntity.getNumberIssued(),
                        patientMedicationHtmlEntity.getMaxIssues(),
                        patientMedicationHtmlEntity.getReviewDate(),
                        patientMedicationHtmlEntity.getDetails()));
            } // if repeat
        }  // for entity

        // NB Now the singular
        PageSection ps = new PageSection("Current Repeat Medication",
                "med-tab-curr-rep",
                new Table(Arrays.asList(
                        "Type",
                        "Start Date",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Last Issued Date",
                        "Number of Prescriptions Issued",
                        "Max Issues",
                        "Review Date",
                        "Additional Information"), repeatMedRows));
        addDateRangeBanners(ps, requestedFromDate != null || requestedToDate != null);  // #251 Date filter not applied to subsection
        return ps;
    }

    /**
     * Discontinued Repeat Medications order by Last Issued Date desc Date range
     * does not apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getMEDDiscontinuedRepeatPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> pastMedRows = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationSearch.findMedications(nhsNumber, requestedFromDate, requestedToDate, medicationSearch.PAST)) {
            pastMedRows.add(Arrays.asList(
                    patientMedicationHtmlEntity.getTypeMed(),
                    patientMedicationHtmlEntity.getLastIssued(),
                    patientMedicationHtmlEntity.getMedicationItem(),
                    patientMedicationHtmlEntity.getDosageInstruction(),
                    patientMedicationHtmlEntity.getQuantity(),
                    patientMedicationHtmlEntity.getStartDate(), // now discontinued date
                    patientMedicationHtmlEntity.getDiscontinuationReason(),
                    patientMedicationHtmlEntity.getDetails()));
        }

        PageSection ps = new PageSection("Discontinued Repeat Medication",
                "med-tab-dis-rep",
                new Table(Arrays.asList(
                        "Type",
                        "Last Issued Date",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Discontinued Date",
                        "Discontinuation Reason",
                        "Additional Information"), pastMedRows),
                null, null);
        addDateRangeBanners(ps, requestedFromDate != null || requestedToDate != null);
        return ps;
    }

    /**
     * 0.7 only All Medications (Summary) This is a *grouped* summarised list of
     * the items in the All Medication Issues Table grouped by Type, Start Date
     * as min(Issue Date), Medication Item, Dosage Instruction,
     * Quantity,count(*) as Number of Prescriptions, Last Issued Date as
     * max(Issue Date) sorted by medication item asc start date desc
     *
     * Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getMEDAllMedicationPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> currentMedRows = new ArrayList<>();

        String fromDateStr = fromDate == null ? null : DATE_FORMAT_SQL.format(fromDate);
        String toDateStr = toDate == null ? null : DATE_FORMAT_SQL.format(toDate);

        String currentMedicationItem = null;
        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberGrouped(nhsNumber, fromDateStr, toDateStr)) {

            // for a changed MedicationItemName add a row with one cell spanning the table as a header
            if (!patientMedicationHtmlEntity.getMedicationItem().equals(currentMedicationItem)) {
                currentMedicationItem = patientMedicationHtmlEntity.getMedicationItem();
                currentMedRows.add(Arrays.asList(currentMedicationItem));
            }
            currentMedRows.add(Arrays.asList(
                    patientMedicationHtmlEntity.getTypeMed(),
                    patientMedicationHtmlEntity.getStartDate(),
                    patientMedicationHtmlEntity.getMedicationItem(),
                    patientMedicationHtmlEntity.getDosageInstruction(),
                    patientMedicationHtmlEntity.getQuantity(),
                    patientMedicationHtmlEntity.getLastIssued(),
                    patientMedicationHtmlEntity.getNumberIssued(),
                    patientMedicationHtmlEntity.getDiscontinuationReason(),
                    patientMedicationHtmlEntity.getDetails()));
        }

        PageSection ps = new PageSection("All Medication",
                "med-tab-all-sum",
                new Table(Arrays.asList(
                        "Type",
                        "Start Date",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Last Issued Date",
                        "Number of Prescriptions Issued",
                        "Discontinuation Details",
                        "Additional Information"), currentMedRows), requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * 0.7 only All medication Issues itemised list sorted by medication item
     * asc issue date desc
     *
     * date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getMEDAllMedicationIssuesPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> currentMedRows = new ArrayList<>();

        String fromDateStr = fromDate == null ? null : DATE_FORMAT_SQL.format(fromDate);
        String toDateStr = toDate == null ? null : DATE_FORMAT_SQL.format(toDate);

        String currentMedicationItem = null;
        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberOrderByMedicationItemAscLastIssuedDesc(nhsNumber, fromDateStr, toDateStr)) {
            // for a changed MedicationItemName add a row with one cell spanning the table as a header
            if (!patientMedicationHtmlEntity.getMedicationItem().equals(currentMedicationItem)) {
                currentMedicationItem = patientMedicationHtmlEntity.getMedicationItem();
                currentMedRows.add(Arrays.asList(currentMedicationItem));
            }
            currentMedRows.add(Arrays.asList(
                    patientMedicationHtmlEntity.getTypeMed(),
                    patientMedicationHtmlEntity.getStartDate(),
                    patientMedicationHtmlEntity.getMedicationItem(),
                    patientMedicationHtmlEntity.getDosageInstruction(),
                    patientMedicationHtmlEntity.getQuantity(),
                    patientMedicationHtmlEntity.getDaysDuration(),
                    patientMedicationHtmlEntity.getDetails()));
        }

        PageSection ps = new PageSection("All Medication Issues",
                "med-tab-all-iss",
                new Table(Arrays.asList(
                        "Type",
                        "Issue Date",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Days Duration",
                        "Additional Information"), currentMedRows), requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Referrals Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getREFPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> referralRows = new ArrayList<>();

        for (ReferralEntity referralEntity : referralSearch.findReferrals(nhsNumber, fromDate, toDate)) {
            referralRows.add(Arrays.asList(
                    referralEntity.getSectionDate(),
                    referralEntity.getFrom(),
                    referralEntity.getTo(),
                    referralEntity.getPriority(),
                    referralEntity.getDetails()));
        }

        PageSection ps = new PageSection("Referrals",
                "ref-tab",
                new Table(Arrays.asList("Date", "From", "To", "Priority", "Details"), referralRows),
                requestedFromDate, requestedToDate, true);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Observations Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getOBSPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> observationRows = new ArrayList<>();

        for (ObservationEntity observationEntity : observationSearch.findObservations(nhsNumber, fromDate, toDate)) {
            observationRows.add(Arrays.asList(
                    observationEntity.getObservationDate(),
                    observationEntity.getEntry(),
                    observationEntity.getValue(),
                    observationEntity.getRange(),
                    // NB at 0.5 this was a repeat of getValue cut and paste error?
                    observationEntity.getDetails()));
        }

        PageSection ps = new PageSection("Observations",
                "obs-tab",
                new Table(Arrays.asList("Date", "Entry", "Value", "Range", "Details"), observationRows),
                requestedFromDate, requestedToDate, true);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Immunisations Date range does not apply
     *
     * @param nhsNumber
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getIMMPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        if (requestedToDate != null || requestedFromDate != null) {
            throw new InvalidRequestException("Date Ranges not allowed to be set",
                    OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                            OperationConstants.COD_CONCEPT_RECORD_INVALID_PARAMETER, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME,
                            IssueTypeEnum.BUSINESS_RULE_VIOLATION));
        }

        List<List<Object>> immunisationRows = new ArrayList<>();

        for (ImmunisationEntity immunisationEntity : immunisationRepository.findByNhsNumberOrderByDateOfVacDesc(nhsNumber)) {
            immunisationRows.add(Arrays.asList(
                    immunisationEntity.getDateOfVac(),
                    immunisationEntity.getVaccination(),
                    immunisationEntity.getPart(),
                    immunisationEntity.getContents(),
                    immunisationEntity.getDetails()));
        }

        PageSection ps = new PageSection("Immunisations",
                "imm-tab",
                new Table(Arrays.asList("Date", "Vaccination", "Part", "Contents", "Details"), immunisationRows),
                requestedFromDate, requestedToDate, true);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Administrative Items Date range applies
     *
     * @param nhsNumber
     * @param fromDate after possible amendment
     * @param toDate after possible amendment
     * @param requestedFromDate as in the original request
     * @param requestedToDate as in the original request
     * @return PageSection
     */
    public PageSection getADMPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> adminItemsRows = new ArrayList<>();
        List<AdminItemData> adminItemList = adminItemSearch.findAllAdminItemHTMLTables(nhsNumber, fromDate, toDate);

        if (adminItemList != null) {
            for (AdminItemData adminItemData : adminItemList) {
                adminItemsRows.add(Arrays.asList(adminItemData.getAdminDate(), adminItemData.getEntry(), adminItemData.getDetails()));
            }
        }

        PageSection ps = new PageSection("Administrative Items",
                "adm-tab",
                new Table(Arrays.asList("Date", "Entry", "Details"), adminItemsRows),
                requestedFromDate, requestedToDate, true);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Not currently used but valuable for development
     * algorithm fed back into sql
     * see
     * https://developer.nhs.uk/apis/gpconnect-0-7-2/accessrecord_view_medications.html
     * for filtering rules
     *
     * @param fromDate start search date
     * @param toDate end search date
     * @param medication
     * @return whether its in range
     */
    private boolean isMedicationInRange(Date fromDate, Date toDate, PatientMedicationHtmlEntity medication) {
        boolean result = false;
        String clause = null;
        if (fromDate == null && toDate == null) {
            result = true;
        } else if (medication.getStartDate() != null) {
            if (medication.getScheduledEnd() != null) {
                clause = "1"; // medication has start and end date
                result = !(fromDate != null && medication.getScheduledEnd().before(fromDate) || toDate != null && medication.getStartDate().after(toDate));
            } else {
                if ("repeat".equals(medication.getTypeMed().toLowerCase())) {
                    clause = "2"; // medication only has start date and is repeat
                    // 2 a. 
                    result = toDate != null && medication.getStartDate().before(toDate) || fromDate != null && medication.getStartDate().before(fromDate);
                    // 2 b. prescribed elsewhere?
                } else {
                    clause = "3"; // medication only has start date and is acute or other. By specifying not repeat this also includes "NHS Medication"
                    // 3. 
                    result = (fromDate != null && medication.getStartDate().after(fromDate)) && (toDate != null && medication.getStartDate().before(toDate));
                }
            }
        } else if (medication.getStartDate() == null) {
            clause = "4"; // no start date
            // 4. use date recorded but we don't store that so just return false
        } else {
            clause = "5"; // catch all if not caught by other criteria eg no med end and current/repeat = Past or Current
        }
        System.out.println(String.format("%s type [%s] current/repeat [%s] from %s to %s med start %s med end %s clause %s result %s",
                medication.getMedicationItem(), medication.getTypeMed(), medication.getCurrentRepeatPast(), md(fromDate), md(toDate), md(medication.getStartDate()), md(medication.getScheduledEnd()), clause, result));
        return result;
    }

    /**
     * munge date objects tooutput format string
     * @param date
     * @return 
     */
    private String md(Date date) {
        return date != null ? DATE_FORMAT.format(date) : "Null";
    }
}
