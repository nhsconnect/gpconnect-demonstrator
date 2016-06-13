package uk.gov.hscic.patient.medications.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;
import uk.gov.hscic.patient.medications.model.MedicationHtmlEntity;

public class MedicationEntityToListTransformer implements Transformer<MedicationHtmlEntity, MedicationListHTML> {

    @Override
    public MedicationListHTML transform(final MedicationHtmlEntity medicationHtmlEntity) {
        final MedicationListHTML medicationList = new MedicationListHTML();

        medicationList.setSourceId(String.valueOf(medicationHtmlEntity.getId()));
        medicationList.setSource(RepoSourceType.LEGACY.getSourceName());

        medicationList.setProvider(medicationHtmlEntity.getProvider());
        medicationList.setHtml(medicationHtmlEntity.getHtml());

        return medicationList;
    }
}
