package net.nhs.esb.converters;

import java.text.ParseException;

import net.nhs.domain.openehr.model.DataSourceId;
import net.nhs.domain.openehr.model.Diagnoses;
import net.nhs.esb.rest.domain.AqlResult;
import net.nhs.legacy.entity.Event;
import org.apache.camel.Converter;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Component;

@Converter
@Component
public class DiagnosesTypeConverter {

    private static final FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");

    @Converter
    public Diagnoses convert(Event event) {
        String diagnosesId = null;
        if (event.getEventId() != null) {
            diagnosesId = String.valueOf(event.getEventId() + "::" + DataSourceId.LEGACY.getValue() + "::1");
        }

        Diagnoses diagnoses = new Diagnoses();
        diagnoses.setId(diagnosesId);
        diagnoses.setPatientId(event.getPatientId());
        diagnoses.setProblemDiagnosis(event.getDiagnosis());
        //diagnoses.setDescription(event.get);
        diagnoses.setSeverity(event.getGrade());
        diagnoses.setDateOfOnset(event.getDiagnosisProgressionDate());
        //diagnoses.setAgeAtOnset(event.get);
        diagnoses.setBodySite(event.getPrimarySite());
        //diagnoses.setDateOfResolution(event.get);
        //diagnoses.setAgeAtResolution(event.get);


        return diagnoses;
    }

    @Converter
    public Diagnoses convert(AqlResult aqlResult) {
        Diagnoses diagnoses = new Diagnoses();
        diagnoses.setId(aqlResult.getUid());
        //diagnoses.setPatientId(aqlResult.get);
        diagnoses.setProblemDiagnosis(aqlResult.getProblemDiagnosis());
        diagnoses.setDescription(aqlResult.getDescription());
        diagnoses.setSeverity(aqlResult.getSeverity());

        try {
            if (aqlResult.getDateOfOnset() != null) {
                diagnoses.setDateOfOnset(formatter.parse(aqlResult.getDateOfOnset()));
            }
        } catch (ParseException e) {
        }

        //diagnoses.setAgeAtOnset(event.get);
        //diagnoses.setBodySite(event.getPrimarySite());
        //diagnoses.setDateOfResolution(event.get);
        //diagnoses.setAgeAtResolution(event.get);

        return diagnoses;
    }
}
