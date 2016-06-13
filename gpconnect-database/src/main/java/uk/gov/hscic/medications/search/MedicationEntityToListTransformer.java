package uk.gov.hscic.medications.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;

public class MedicationEntityToListTransformer implements Transformer<PatientMedicationHtmlEntity, PatientMedicationHTML> {

    @Override
    public PatientMedicationHTML transform(final PatientMedicationHtmlEntity medicationHtmlEntity) {
        final PatientMedicationHTML medicationList = new PatientMedicationHTML();

        medicationList.setSourceId(String.valueOf(medicationHtmlEntity.getId()));
        medicationList.setSource(RepoSourceType.LEGACY.getSourceName());

        medicationList.setProvider(medicationHtmlEntity.getProvider());
        medicationList.setHtml(medicationHtmlEntity.getHtml());

        return medicationList;
    }
}
