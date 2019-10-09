package uk.gov.hscic;

import java.util.Arrays;
import java.util.List;

public final class SystemConstants {

    //Structured Allergies
    public static final String MEDICATION = "medication";
    public static final String ACTIVE = "active";

    public static final String ACTIVE_ALLERGIES_DISPLAY = "Active Allergies";
    // #175
    public static final String ACTIVE_ALLERGIES_TITLE = "Allergies and adverse reactions";

    public static final String RESOLVED_ALLERGIES_DISPLAY = "Ended allergies";
    // #175
    public static final String RESOLVED_ALLERGIES_TIILE = "Ended allergies";

    public static final String RESOLVED = "resolved";
    public static final String NO_KNOWN = "no known";
    public static final String NO_CONTENT_RECORDED_DISPLAY = "No Content Recorded";

    public static final String SNOMED_URL = "http://snomed.info/sct";

    public static final String NO_CONTENT = "noContent";
    public static final String NO_CONTENT_RECORDED = "no-content-recorded";
    public static final String INFORMATION_NOT_AVAILABLE = "Information not available";
    public static final String NO_INFORMATION_AVAILABLE = "No information available"; // required structured
    public static final String PATIENT_REFERENCE_URL = "Patient/";

    // Parameter Names
    public static final String PATIENT_NHS_NUMBER_PARM = "patientNHSNumber";
    public static final String INCLUDE_RESOLVED_ALLERGIES_PARM = "includeResolvedAllergies";
    public static final String INCLUDE_ALLERGIES_PARM = "includeAllergies";
    public static final String INCLUDE_TEST_RESULTS_PARM = "includeTestResults";
    public static final String INCLUDE_IMMUNIZATIONS_PARM = "includeImmunisations";
    public static final String INCLUDE_UNCATEGORISED_DATA_PARM = "includeUncategorisedData"; // Changed at 1.3.1
    public static final String INCLUDE_CONSULTATIONS_PARM = "includeConsultations";
    public static final String INCLUDE_PROBLEMS_PARM = "includeProblems";

    public static final String INCLUDE_MEDICATION_PARM = "includeMedication";
    public static final List VALID_PARAMETER_NAMES = Arrays.asList(SystemConstants.PATIENT_NHS_NUMBER_PARM,
            SystemConstants.INCLUDE_ALLERGIES_PARM,
            SystemConstants.INCLUDE_MEDICATION_PARM,
            SystemConstants.INCLUDE_IMMUNIZATIONS_PARM,
            SystemConstants.INCLUDE_UNCATEGORISED_DATA_PARM,
            SystemConstants.INCLUDE_CONSULTATIONS_PARM,
            SystemConstants.INCLUDE_PROBLEMS_PARM/*,
            SystemConstants.INCLUDE_TEST_RESULTS_PARM */
    );

    public static final String INCLUDE_PRESCRIPTION_ISSUES = "includePrescriptionIssues";
    // 1.2.1
    public static final String MEDICATION_DATE_PERIOD = "medicationDatePeriod";
    // 1.2.2
    public static final String MEDICATION_SEARCH_FROM_DATE = "medicationSearchFromDate";

    // SNOMED ListResource Data
    public static final String SNOMED_MEDICATION_LIST_DISPLAY = "Medications and medical devices";
    public static final String SNOMED_MEDICATION_LIST_CODE = "933361000000108";
    public static final String SNOMED_IMMUNIZATIONS_LIST_DISPLAY = "Immunisations"; // NB the s not z
    public static final String SNOMED_IMMUNIZATIONS_LIST_CODE = "1102181000000102";
    public static final String SNOMED_UNCATEGORISED_LIST_DISPLAY = "Miscellaneous record";
    public static final String SNOMED_UNCATEGORISED_LIST_CODE = "826501000000100";
    public static final String SNOMED_CONSULTATION_LIST_DISPLAY = "Consultation";
    // TODO Need the new SNOMED Code for the List of Consultations
    public static final String SNOMED_CONSULTATION_LIST_CODE = "999999999999999";
    public static final String SNOMED_PROBLEMS_LIST_DISPLAY = "Problems";
    public static final String SNOMED_PROBLEMS_LIST_CODE = "717711000000103";

    // Parameter Parts
    public static final String UNCATEGORISED_DATA_SEARCH_PERIOD = "uncategorisedDataSearchPeriod";
    public static final String CONSULTATION_SEARCH_PERIOD = "consultationSearchPeriod";
    public static final String NUMBER_OF_MOST_RECENT = "numberOfMostRecent";
    public static final String FILTER_STATUS = "filterStatus"; // renamed at 1.3.1
    public static final String FILTER_SIGNIFICANCE = "filterSignificance"; // renamed at 1.3.1
    
    // offsets into the patients property array. See the provider prerequsites spreadsheet for more details
    // https://github.com/nhsconnect/gpconnect-provider-testing/blob/master/Data/Test%20Suite%20Prerequisites.xlsx
    public static final int PATIENT_2 = 2; // patient 2 contains majority of structured test data
    public static final int PATIENT_SUPERSEDED = 11; // patient 11 Superseded NHS Number
    public static final int PATIENT_NOTONSPINE = 14; // patient 14 Not on PDS
    public static final int PATIENT_NOCONSENT = 15; // patient 15 No consent to share
    
    // TODO remove hard coding pick up from providerRouting.json ?
    public static final String OUR_ODS_CODE = "A20047";

    // #266 Notes associated with warnings
    public static final String CONFIDENTIAL_ITEMS_NOTE = "Items excluded due to confidentiality and/or patient preferences.";
    public static final String DATA_IN_TRANSIT_NOTE = "Patient record transfer from previous GP practice not yet complete; information recorded before %s may be missing.";
    public static final String DATA_AWAITING_FILING_NOTE = "Patient data may be incomplete as there is data supplied by a third party awaiting review before becoming available.";
}
