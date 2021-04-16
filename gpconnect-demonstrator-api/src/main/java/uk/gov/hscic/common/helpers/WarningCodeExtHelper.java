package uk.gov.hscic.common.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.ListResource;
import static uk.gov.hscic.SystemConstants.CONFIDENTIAL_ITEMS_NOTE;
import static uk.gov.hscic.SystemConstants.DATA_AWAITING_FILING_NOTE;
import static uk.gov.hscic.SystemConstants.DATA_IN_TRANSIT_NOTE;

import uk.gov.hscic.SystemURL;
import uk.gov.hscic.medication.statement.MedicationStatementEntity;
import uk.gov.hscic.medication.statement.MedicationStatementRepository;
import uk.gov.hscic.patient.details.PatientEntity;
import uk.gov.hscic.patient.details.PatientRepository;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergySearch;

public class WarningCodeExtHelper {

    private static boolean dataInTransit = false;
    private static boolean dataAwaitingFiling = false;

    private static final String DATA_IN_TRANSIT = "data-in-transit";
    private static final String CONFIDENTIAL_ITEMS = "confidential-items";
    private static final String DATA_AWAITING_FILING = "data-awaiting-filing";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    /**
     * These are static so need resetting for each query
     */
    public static void resetWarningFlags() {
        dataInTransit = false;
        dataAwaitingFiling = false;
    }

    /**
     * confidential items are per record but the other two are global values per
     * patient
     *
     * @param warningCodes
     * @param list
     * @param patientRepository
     * @param medicationStatementRepository
     * @param structuredAllergySearch
     */
    public static void addWarningCodeExtensions(Set<String> warningCodes, ListResource list,
            PatientRepository patientRepository, MedicationStatementRepository medicationStatementRepository, StructuredAllergySearch structuredAllergySearch) {

        String NHS = list.getSubject().getIdentifier().getValue();
        
        PatientEntity patientEntity = patientRepository.findByNhsNumber(NHS);
        List<MedicationStatementEntity> medicationStatements = medicationStatementRepository.findByPatientId(patientEntity.getId());
        for (MedicationStatementEntity medicationStatement : medicationStatements) {
            setFlags(medicationStatement.getWarningCode());
        }

        List<StructuredAllergyIntoleranceEntity> allergies = structuredAllergySearch.getAllergyIntolerance(NHS);
        for (StructuredAllergyIntoleranceEntity allergy : allergies) {
            setFlags(allergy.getWarningCode());
        }

        // check medication_statements for either of the global flags
        if (dataInTransit) {
            warningCodes.add(DATA_IN_TRANSIT);
        }
        if (dataAwaitingFiling) {
            warningCodes.add(DATA_AWAITING_FILING);
        }

        StringBuilder sb = new StringBuilder();
        warningCodes.forEach(warningCode -> {
            if (warningCode != null) {
                String warningCodeDisplay = "";
                Annotation annotation = new Annotation();
                switch (warningCode) {
                    case CONFIDENTIAL_ITEMS:
                        warningCodeDisplay = "Confidential Items";
                        annotation.setText(CONFIDENTIAL_ITEMS_NOTE); // #266
                        //list.addNote(annotation);
                        sb.append("\r\n").append(annotation.getText());
                        break;
                    case DATA_IN_TRANSIT:
                        warningCodeDisplay = "Data in Transit";
                        Calendar cal = new GregorianCalendar();
                        Date now = new Date();
                        cal.setTime(now);
                        cal.add(Calendar.DAY_OF_YEAR, -7); // a week before now
                        annotation.setText(String.format(DATA_IN_TRANSIT_NOTE, DATE_FORMAT.format(cal.getTime()))); // #266
                        //list.addNote(annotation);
                        sb.append("\r\n").append(annotation.getText());
                        break;
                    case DATA_AWAITING_FILING:
                        warningCodeDisplay = "Data Awaiting Filing";
                        annotation.setText(DATA_AWAITING_FILING_NOTE); // #266
                        //list.addNote(annotation);
                        sb.append("\r\n").append(annotation.getText());
                        break;
                    default:
                        break;
                }
                // #182
                Extension warningExt = new Extension(SystemURL.WARNING_CODE, new CodeType(warningCode));
                list.addExtension(warningExt);
            }
        });

        // cardinality of note 0..1 #266
        if (sb.length() > 0) {
            Annotation annotation = null;
            if (list.getNote().size() > 0) {
                annotation = list.getNote().get(0);
                annotation.setText(annotation.getText());
                annotation.setText(annotation.getText() + sb.toString());
            } else {
                annotation = new Annotation();
                list.addNote(annotation);
                annotation.setText(sb.toString().replaceFirst("^\r\n", ""));
            }
        }
    }

    /**
     * set flags
     *
     * @param code
     */
    private static void setFlags(String code) {
        if (code != null) {
            switch (code) {
                case DATA_IN_TRANSIT:
                    dataInTransit = true;
                    break;
                case DATA_AWAITING_FILING:
                    dataAwaitingFiling = true;
                    break;
            }
        }
    }
}
