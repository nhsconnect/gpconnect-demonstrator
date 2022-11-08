package uk.gov.hscic;

import java.util.Arrays;
import java.util.List;

public final class SystemConstants {

    //Structured Allergies
    public static final String MEDICATION = "medication";
    public static final String ACTIVE = "active";

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

    public static final String INCLUDE_MEDICATION_PARM = "includeMedication";
    public static final String INCLUDE_ALLERGIES_PARM = "includeAllergies";

    // 1.3
    public static final String INCLUDE_IMMUNIZATIONS_PARM = "includeImmunisations";
    public static final String INCLUDE_UNCATEGORISED_DATA_PARM = "includeUncategorisedData"; // Changed at 1.3.1
    public static final String INCLUDE_CONSULTATIONS_PARM = "includeConsultations";
    public static final String INCLUDE_PROBLEMS_PARM = "includeProblems";

    // 1.4
    public static final String INCLUDE_INVESTIGATIONS_PARM = "includeInvestigations";
    public static final String INCLUDE_REFERRALS_PARM = "includeReferrals";

    public static final List VALID_PARAMETER_NAMES = Arrays.asList(SystemConstants.PATIENT_NHS_NUMBER_PARM,
            SystemConstants.INCLUDE_ALLERGIES_PARM,
            SystemConstants.INCLUDE_MEDICATION_PARM,
            // 1.3
            SystemConstants.INCLUDE_IMMUNIZATIONS_PARM,
            SystemConstants.INCLUDE_UNCATEGORISED_DATA_PARM,
            SystemConstants.INCLUDE_CONSULTATIONS_PARM,
            SystemConstants.INCLUDE_PROBLEMS_PARM,
            // 1.4
            SystemConstants.INCLUDE_INVESTIGATIONS_PARM,
            SystemConstants.INCLUDE_REFERRALS_PARM
    );

    // parameter part names
    public static final String INCLUDE_RESOLVED_ALLERGIES_PARM_PART = "includeResolvedAllergies";
    public static final String INCLUDE_PRESCRIPTION_ISSUES_PARAM_PART = "includePrescriptionIssues";
    // 1.2.1
    public static final String MEDICATION_DATE_PERIOD_PARAM_PART = "medicationDatePeriod";
    // 1.2.2
    public static final String MEDICATION_SEARCH_FROM_DATE_PARAM_PART = "medicationSearchFromDate";

    // SNOMED ListResource Data
    // https://developer.nhs.uk/apis/gpconnect-1-3-1/accessrecord_structured_development_list.html
    public static final String SNOMED_ACTIVE_ALLERGIES_DISPLAY = "Allergies and adverse reactions";
    public static final String SNOMED_ACTIVE_ALLERGIES_CODE = "886921000000105";
    
    // #175 NB This is not typical normally the list title is the same as the display
    // #365 active allergies display and title should be the same this supersedes #175
    //public static final String ACTIVE_ALLERGIES_TITLE = "Allergies and adverse reactions";

    public static final String SNOMED_RESOLVED_ALLERGIES_DISPLAY = "Ended allergies";
    public static final String SNOMED_RESOLVED_ALLERGIES_CODE = "1103671000000101";

    public static final String SNOMED_MEDICATION_LIST_DISPLAY = "Medications and medical devices";
    public static final String SNOMED_MEDICATION_LIST_CODE = "933361000000108";
    
    public static final String SNOMED_IMMUNIZATIONS_LIST_DISPLAY = "Immunisations"; // NB the s not z
    public static final String SNOMED_IMMUNIZATIONS_LIST_CODE = "1102181000000102";
    
    public static final String SNOMED_UNCATEGORISED_DATA_LIST_DISPLAY = "Miscellaneous record";
    public static final String SNOMED_UNCATEGORISED_DATA_LIST_CODE = "826501000000100";
  
    // #289 new snomed code and display for list of consultations
    public static final String SNOMED_CONSULTATION_LIST_DISPLAY = "List of consultations";
    public static final String SNOMED_CONSULTATION_LIST_CODE = "1149501000000101";
    
    public static final String SNOMED_PROBLEMS_LIST_DISPLAY = "Problems";
    public static final String SNOMED_PROBLEMS_LIST_CODE = "717711000000103";
    // 1.4
    public static final String SNOMED_REFERRALS_LIST_DISPLAY = "Outbound referral";
    public static final String SNOMED_REFERRALS_LIST_CODE = "792931000000107";
    
    public static final String SNOMED_INVESTIGATIONS_LIST_DISPLAY = "Investigations and results";
    public static final String SNOMED_INVESTIGATIONS_LIST_CODE = "887191000000108";

    // 1.5 consultation secondary lists
    public static final String SECONDARY_LIST_URL = "https://fhir.hl7.org.uk/STU3/CodeSystem/GPConnect-SecondaryListValues-1";

    public static final String CONSULTATION_MEDS_SECONDARY_LIST_TITLE = "Consultations - medications contained in consultations";
    public static final String CONSULTATION_UNCATEGORISED_SECONDARY_LIST_TITLE = "Consultations - uncategorised data contained in consultations";
    // removed for #359
    //public static final String CONSULTATION_PROBLEMS_SECONDARY_LIST_TITLE = "Consultations - problems contained in consultations";

    // 1.5 problems secondary lists
    public static final String PROBLEMS_MEDS_SECONDARY_LIST_TITLE = "Problems - medications related to problems";
    public static final String PROBLEMS_UNCATEGORISED_SECONDARY_LIST_TITLE = "Problems - uncategorised data related to problems";
    
    // #359 new secondary list NB These are not snomed codes. They have their own secondary list url
    public static final String PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_TITLE = "Problems - linked problems not relating to the primary query";
    public static final String PROBLEMS_LINKED_NOT_RELATING_TO_PRIMARY_QUERY_LIST_CODE = "problems-linked-problems-not-relating-to-the-primary-query";
    
    // extension point

    // Parameter Parts
    public static final String UNCATEGORISED_DATA_SEARCH_PERIOD_PARAM_PART = "uncategorisedDataSearchPeriod";
    public static final String CONSULTATION_SEARCH_PERIOD_PARAM_PART = "consultationSearchPeriod";
    public static final String INVESTIGATION_SEARCH_PERIOD_PARAM_PART = "investigationSearchPeriod";
    public static final String REFERRAL_SEARCH_PERIOD_PARAM_PART = "referralSearchPeriod";
    public static final String NUMBER_OF_MOST_RECENT_PARAM_PART = "includeNumberOfMostRecent";
    public static final String FILTER_STATUS_PARAM_PART = "filterStatus"; // renamed at 1.3.1
    public static final String FILTER_SIGNIFICANCE_PARAM_PART = "filterSignificance"; // renamed at 1.3.1
    
    // 1.5
    public static final String IMMUNIZATIONS_INCLUDE_NOT_GIVEN_PARAM_PART = "includeNotGiven";
    public static final String IMMUNIZATIONS_INCLUDE_STATUS_PARAM_PART = "includeStatus";
    
    // offsets into the patients property array. See the provider prerequsites spreadsheet for more details
    // https://github.com/nhsconnect/gpconnect-provider-testing/blob/master/Data/Test%20Suite%20Prerequisites.xlsx
    public static final int PATIENT_2 = 2; // patient 2 contains majority of structured test data
    public static final int PATIENT_3 = 3; // patient 3 contains majority of structured test data without returning problems and hence problems list
    public static final int PATIENT_SUPERSEDED = 11; // patient 11 Superseded NHS Number
    public static final int PATIENT_NOTONSPINE = 14; // patient 14 Not on PDS
    public static final int PATIENT_NOCONSENT = 15; // patient 15 No consent to share
    public static final int PATIENT_DECEASED_14_DAYS = 18; // patient 18 Deceased
    
    // TODO remove hard coding pick up from providerRouting.json ?
    public static final String OUR_ODS_CODE = "B82617";

    // #266 Notes associated with warnings
    public static final String CONFIDENTIAL_ITEMS_NOTE = "Items excluded due to confidentiality and/or patient preferences.";
    public static final String DATA_IN_TRANSIT_NOTE = "Patient record transfer from previous GP practice not yet complete; information recorded before %s may be missing.";
    public static final String DATA_AWAITING_FILING_NOTE = "Patient data may be incomplete as there is data supplied by a third party awaiting review before becoming available.";
}
