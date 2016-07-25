package uk.gov.hscic.patient.patientsummary.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryEntity;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;

public class PatientSummaryEntityToListTransformer implements Transformer<PatientSummaryEntity, PatientSummaryListHTML> {

    @Override
    public PatientSummaryListHTML transform(PatientSummaryEntity patientSummaryEntity) {
        final PatientSummaryListHTML patientSummaryList = new PatientSummaryListHTML();

        patientSummaryList.setSourceId(String.valueOf(patientSummaryEntity.getId()));
        patientSummaryList.setSource(RepoSourceType.LEGACY.getSourceName());

        patientSummaryList.setProvider(patientSummaryEntity.getProvider());
        patientSummaryList.setHtml(patientSummaryEntity.getHtml());

        patientSummaryList.setLastUpdated(patientSummaryEntity.getLastUpdated());
        return patientSummaryList;
    }
}
