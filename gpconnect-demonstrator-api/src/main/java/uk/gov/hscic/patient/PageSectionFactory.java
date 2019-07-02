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
                // # 224
                sb.append("All data items from ").append(DATE_FORMAT.format(pageSection.getFromDate()));
            } else if (pageSection.getFromDate() == null && pageSection.getToDate() != null) {
                // #224
                sb.append("All data items until ").append(DATE_FORMAT.format(pageSection.getToDate()));
            } else {
                // #225 change text on banner
                sb.append("All relevant items");
            }
        }
        pageSection.addBanner(sb.toString());
    }

    /**
     * Active Problems
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
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
     * 0.7 only Major Inactive Problems
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getPRBMajorInactivePageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> problemInactiveRows = new ArrayList<>();

        List<ProblemEntity> findProblems = problemSearch.findProblems(nhsNumber, requestedFromDate, requestedToDate);
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
     * 0.7 only Minor Inactive Problems
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getPRBOtherInactivePageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> problemInactiveRows = new ArrayList<>();

        List<ProblemEntity> findProblems = problemSearch.findProblems(nhsNumber, requestedFromDate, requestedToDate);
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
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getENCPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        return getENCPageSection(nhsNumber, fromDate, toDate, requestedFromDate, requestedToDate, -1);
    }

    /**
     * Encounters
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
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
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Current Allergies
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getALLCurrentPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        if (toDate != null || fromDate != null) {
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
     * Historical Allergies
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getALLHistoricalPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        if (toDate != null || fromDate != null) {
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
     * Clinical Items
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
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
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * 0.7 only Acute Medications order by start date desc
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
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
     * Current Repeat Medications order by start date desc
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
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
     * Discontinued Repeat Medications order by Last Issued Date desc
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
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
     * 0.7 only All Medications (Summary) This is a grouped summarised list of
     * the items in the All Medication Issues Table grouped by Type, Start Date
     * as min(Issue Date), Medication Item, Dosage Instruction,
     * Quantity,count(*) as Number of Prescriptions, Last Issued Date as
     * max(Issue Date) sorted by medication item asc start date desc
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getMEDAllMedicationPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> currentMedRows = new ArrayList<>();

        String currentMedicationItem = null;
        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberGrouped(nhsNumber)) {
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
                        "Additional Information"), currentMedRows));
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * 0.7 only All medication Issues itemised list sorted by medication item
     * asc issue date desc
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getMEDAllMedicationIssuesPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> currentMedRows = new ArrayList<>();

        String currentMedicationItem = null;
        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberOrderByMedicationItemAscLastIssuedDesc(nhsNumber)) {
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
                        "Additional Information"), currentMedRows));
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Referrals
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
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
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Observations
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getOBSPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> observationRows = new ArrayList<>();

        for (ObservationEntity observationEntity : observationSearch.findObservations(nhsNumber, requestedFromDate, requestedToDate)) {
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
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Immunisations
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getIMMPageSection(String nhsNumber, Date fromDate, Date toDate, Date requestedFromDate, Date requestedToDate) {
        if (toDate != null || fromDate != null) {
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
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }

    /**
     * Administrative Items
     *
     * @param nhsNumber
     * @param fromDate
     * @param toDate
     * @param requestedFromDate
     * @param requestedToDate
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
                requestedFromDate, requestedToDate);
        addDateRangeBanners(ps);
        return ps;
    }
}
