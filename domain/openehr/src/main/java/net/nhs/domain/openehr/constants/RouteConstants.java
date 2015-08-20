package net.nhs.domain.openehr.constants;

public final class RouteConstants {
    public static final String DIRECT = "direct:";
    public static final String LOG = "log:";
    public static final String MOCK = "mock:";
    public static final String SEDA = "seda:";

    public static final String RS_PATIENTS_ROUTE = "rsPatientsRoute";

    public static final String OPENEHR_GET_DIAGNOSES_FOR_PATIENT_ROUTE = "openEHRGetDiagnosesForPatientRoute";
    public static final String LEGACY_GET_DIAGNOSES_FOR_PATIENT_ROUTE = "legacyGetDiagnosesForPatientRoute";

    public static final String OPENEHR_SAVE_DIAGNOSIS_FOR_PATIENT_ROUTE = "openEHRSaveDiagnosisForPatientRoute";

    public static final String OPENEHR_GET_DIAGNOSIS_FOR_PATIENT_ROUTE = "openEHRGetDiagnosisForPatientRoute";
    public static final String LEGACY_GET_DIAGNOSIS_FOR_PATIENT_ROUTE = "legacyGetDiagnosisForPatientRoute";

    public static final String OPENEHR_UPDATE_DIAGNOSIS_FOR_PATIENT_ROUTE = "openEHRUpdateDiagnosisForPatientRoute";

    public static final String START = "start";
    public static final String END = "end";

    private RouteConstants() {
        // Prevent creation
    }

    public static String HEADER(String header) {
        return "${header." + header + "}";
    }
}
