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
    public static final String INCLUDE_MEDICATION_PARM = "includeMedication";
    public static final List VALID_PARAMETER_NAMES = Arrays.asList(SystemConstants.PATIENT_NHS_NUMBER_PARM,
            SystemConstants.INCLUDE_ALLERGIES_PARM,
            SystemConstants.INCLUDE_MEDICATION_PARM,
            SystemConstants.INCLUDE_TEST_RESULTS_PARM
    );

    public static final String INCLUDE_PRESCRIPTION_ISSUES = "includePrescriptionIssues";
    // 1.2.1
    public static final String MEDICATION_DATE_PERIOD = "medicationDatePeriod";
    // 1.2.2
    public static final String MEDICATION_SEARCH_FROM_DATE = "medicationSearchFromDate";

    public static final String MEDICATION_LIST = "Medications and medical devices";

}
