package uk.gov.hscic.patient;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
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
import static uk.gov.hscic.metadata.GpConnectServerConformanceProvider.VERSION;

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

    /**
     * Active Problems
     *
     * @param nhsNumber
     * @return PageSection
     */
    public PageSection getPRBActivePageSection(String nhsNumber) {
        List<List<Object>> problemActiveRows = new ArrayList<>();
        for (ProblemEntity problem : problemSearch.findProblems(nhsNumber)) {
            if ("Active".equals(problem.getActiveOrInactive())) {
                problemActiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
            }
        }
        return new PageSection("Active Problems and Issues",
                "prb-tab-act",
                new Table(Arrays.asList("Start Date", "Entry", "Significance", "Details"), problemActiveRows));
    }

    /**
     * 0.5 only Inactive Problems
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getPRBInactivePageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> problemInactiveRows = new ArrayList<>();

        List<ProblemEntity> findProblems = problemSearch.findProblems(nhsNumber, requestedFromDate, requestedToDate);
        for (ProblemEntity problem : findProblems) {
            if (!"Active".equals(problem.getActiveOrInactive())) {
                problemInactiveRows.add(Arrays.asList(problem.getStartDate(), problem.getEndDate(), problem.getEntry(), problem.getSignificance(), problem.getDetails()));
            }
        }

        return new PageSection("Inactive Problems and Issues",
                "",  // Not required at 0.5
                new Table(Arrays.asList("Start Date", "End Date", "Entry", "Significance", "Details"), problemInactiveRows),
                requestedFromDate, requestedToDate);
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

        return new PageSection("Major Inactive Problems and Issues",
                "prb-tab-majinact",
                new Table(Arrays.asList("Start Date", "End Date", "Entry", "Significance", "Details"), problemInactiveRows),
                requestedFromDate, requestedToDate);
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

        return new PageSection("Other Inactive Problems and Issues",
                "prb-tab-othinact",
                new Table(Arrays.asList("Start Date", "End Date", "Entry", "Significance", "Details"), problemInactiveRows),
                requestedFromDate, requestedToDate);
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

        return new PageSection(maxRows != -1 ? "Last "+ maxRows +" Encounters" : "Encounters",
                "enc-tab",
                new Table(Arrays.asList("Date", "Title", "Details"), encounterRows),
                requestedFromDate, requestedToDate);
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
        if (toDate != null && fromDate != null) {
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

        return new PageSection("Current Allergies and Adverse Reactions",
                "all-tab-curr",
                new Table(Arrays.asList("Start Date", "Details"), currentAllergyRows),
                requestedFromDate, requestedToDate);
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
        if (toDate != null && fromDate != null) {
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

        return new PageSection("Historical Allergies and Adverse Reactions",
                "all-tab-hist",
                new Table(Arrays.asList("Start Date", "End Date", "Details"), historicalAllergyRows),
                requestedFromDate, requestedToDate);
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

        return new PageSection("Clinical Items",
                "cli-tab",
                new Table(Arrays.asList("Date", "Entry", "Details"), clinicalItemsRows),
                requestedFromDate, requestedToDate);
    }

    /**
     * 0.5 only Current Medications
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getMEDCurrentPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> currentMedRows = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberOrderByStartDateDesc(nhsNumber)) {
            if ("Current".equals(patientMedicationHtmlEntity.getCurrentRepeatPast())) {
                currentMedRows.add(Arrays.asList(
                        patientMedicationHtmlEntity.getStartDate(),
                        patientMedicationHtmlEntity.getMedicationItem(),
                        patientMedicationHtmlEntity.getTypeMed(),
                        patientMedicationHtmlEntity.getScheduledEnd(),
                        patientMedicationHtmlEntity.getDaysDuration(),
                        patientMedicationHtmlEntity.getDetails()));
            }
        }

        return new PageSection("Current Medication Issues",
                "", // Not required at 0.5
                new Table(Arrays.asList("Start Date", "Medication Item", "Type", "Scheduled End Date", "Days Duration", "Details"), currentMedRows));
    }

    /**
     * 0.5 only Past Medications
     *
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getMEDPastPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> pastMedRows = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationSearch.findMedications(nhsNumber, requestedFromDate, requestedToDate, medicationSearch.PAST)) {
            pastMedRows.add(Arrays.asList(
                    patientMedicationHtmlEntity.getStartDate(),
                    patientMedicationHtmlEntity.getMedicationItem(),
                    patientMedicationHtmlEntity.getTypeMed(),
                    patientMedicationHtmlEntity.getLastIssued(),
                    patientMedicationHtmlEntity.getReviewDate(),
                    patientMedicationHtmlEntity.getNumberIssued(),
                    patientMedicationHtmlEntity.getMaxIssues(),
                    patientMedicationHtmlEntity.getDetails()));
        }

        return new PageSection("Past Medications",
                "", // Not required at 0.5
                new Table(Arrays.asList("Start Date", "Medication Item", "Type", "Last Issued", "Review Date", "Number Issued", "Max Issues", "Details"), pastMedRows),
                requestedFromDate, requestedToDate);
    }

    /**
     * 0.7 only Acute Medications
     * order by 
     *  start date desc
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

        return new PageSection("Acute Medication (Last 12 Months)",
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
    }

    /**
     * Current Repeat Medications
     * order by start date desc
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getMEDRepeatPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> repeatMedRows = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumberOrderByStartDateDesc(nhsNumber)) {
            if ("Repeat".equals(patientMedicationHtmlEntity.getCurrentRepeatPast())) {

                if (VERSION.getMinor() <= 5) {
                    repeatMedRows.add(Arrays.asList(
                            patientMedicationHtmlEntity.getLastIssued(),
                            patientMedicationHtmlEntity.getMedicationItem(),
                            patientMedicationHtmlEntity.getStartDate(),
                            patientMedicationHtmlEntity.getReviewDate(),
                            patientMedicationHtmlEntity.getNumberIssued(),
                            patientMedicationHtmlEntity.getMaxIssues(),
                            patientMedicationHtmlEntity.getDetails()));
                } else {
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
                }
            } // if repeat
        }  // for entity

        if (VERSION.getMinor() <= 5) {
            return new PageSection("Current Repeat Medications",
                    "", // Not required at 0.5
                    new Table(Arrays.asList(
                            "Last Issued",
                            "Medication Item",
                            "Start Date",
                            "Review Date",
                            "Number Issued",
                            "Max Issues",
                            "Details"), repeatMedRows));

        } else {  // 0.7
            // NB Now the singular
            return new PageSection("Current Repeat Medication",
                    "med-tab-curr-rep",
                    new Table(Arrays.asList(
                            "Type",
                            "Start Date",
                            "Medication Item",
                            "Dosage Instruction",
                            "Quantity",
                            "Last Issued",
                            "Number Issued",
                            "Max Issues",
                            "Review Date",
                            "Additional Information"), repeatMedRows));
        }
    }

    /**
     * Discontinued Repeat Medications
     * order by Last Issued Date desc
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

        return new PageSection("Discontinued Repeat Medication",
                "med-tab-dis-rep",
                new Table(Arrays.asList(
                        "Type",
                        "Last Issued",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Discontinued Date",
                        "Discontinuation Reason",
                        "Additional Information"), pastMedRows),
                requestedFromDate, requestedToDate);
    }

    /**
     * 0.7 only All Medications (Summary)
     * This is a grouped summarised list of the items in the All Medication Issues Table
     *  grouped by Type, Start Date as min(Issue Date), Medication Item, Dosage Instruction, Quantity,count(*) as Number of Prescriptions, Last Issued Date as max(Issue Date)
     *  sorted by 
     *  medication item asc
     *  start date desc
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

        return new PageSection("All Medication",
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
    }

    /**
     * 0.7 only All medication Issues 
     * itemised list 
     * sorted by 
     *  medication item asc
     *  issue date desc
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

        return new PageSection("All Medication Issues",
                "med-tab-all-iss",
                new Table(Arrays.asList(
                        "Type",
                        "Issue Date",
                        "Medication Item",
                        "Dosage Instruction",
                        "Quantity",
                        "Days Duration",
                        "Additional Information"), currentMedRows));
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

        return new PageSection("Referrals",
                "ref-tab",
                new Table(Arrays.asList("Date", "From", "To", "Priority", "Details"), referralRows),
                requestedFromDate, requestedToDate);
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

        return new PageSection("Observations",
                "obs-tab",
                new Table(Arrays.asList("Date", "Entry", "Value", "Range", "Details"), observationRows),
                requestedFromDate, requestedToDate);
    }

    /**
     * Investigations
     * Removed at 0.5.1
     * @param nhsNumber
     * @param requestedFromDate
     * @param requestedToDate
     * @return PageSection
     */
    public PageSection getINVPageSection(String nhsNumber, Date requestedFromDate, Date requestedToDate) {
        List<List<Object>> investigationRows = new ArrayList<>();

        for (InvestigationEntity investigationEntity : investigationRepository.findByNhsNumberOrderByDateDesc(nhsNumber)) {
            investigationRows.add(Arrays.asList(
                    investigationEntity.getDate(),
                    investigationEntity.getTitle(),
                    investigationEntity.getDetails()));
        }

        return new PageSection("Investigations",
                "", 
                new Table(Arrays.asList("Date", "Title", "Details"), investigationRows),
                requestedFromDate, requestedToDate);
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
        if (toDate != null && fromDate != null) {
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

        return new PageSection("Immunisations",
                "imm-tab",
                new Table(Arrays.asList("Date", "Vaccination", "Part", "Contents", "Details"), immunisationRows),
                requestedFromDate, requestedToDate);
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

        return new PageSection("Administrative Items",
                "adm-tab",
                new Table(Arrays.asList("Date", "Entry", "Details"), adminItemsRows),
                requestedFromDate, requestedToDate);
    }
}
