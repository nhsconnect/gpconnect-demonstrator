package uk.gov.hscic.common.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.ListResource;
import static uk.gov.hscic.SystemConstants.CONFIDENTIAL_ITEMS_NOTE;
import static uk.gov.hscic.SystemConstants.DATA_AWAITING_FILING_NOTE;
import static uk.gov.hscic.SystemConstants.DATA_IN_TRANSIT_NOTE;

import uk.gov.hscic.SystemURL;

public class WarningCodeExtHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    public static void addWarningCodeExtensions(Set<String> warningCodes, ListResource list) {
        StringBuilder sb = new StringBuilder();
        warningCodes.forEach(warningCode -> {
            if (warningCode != null) {
                String warningCodeDisplay = "";
                Annotation annotation = new Annotation();
                switch (warningCode) {
                    case "confidential-items":
                        warningCodeDisplay = "Confidential Items";
                        annotation.setText(CONFIDENTIAL_ITEMS_NOTE); // #266
                        //list.addNote(annotation);
                        sb.append("\r\n").append(annotation.getText());
                        break;
                    case "data-in-transit":
                        warningCodeDisplay = "Data in Transit";
                        Calendar cal = new GregorianCalendar();
                        Date now = new Date();
                        cal.setTime(now);
                        cal.add(Calendar.DAY_OF_YEAR, -7); // a week before now
                        annotation.setText(String.format(DATA_IN_TRANSIT_NOTE, DATE_FORMAT.format(cal.getTime()))); // #266
                        //list.addNote(annotation);
                        sb.append("\r\n").append(annotation.getText());
                        break;
                    case "data-awaiting-filing":
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
                annotation.setText(annotation.getText()+sb.toString());
            } else {
                annotation = new Annotation();
                list.addNote(annotation);
                annotation.setText(sb.toString().replaceFirst("^\r\n",""));
            }
        }
    }
}
