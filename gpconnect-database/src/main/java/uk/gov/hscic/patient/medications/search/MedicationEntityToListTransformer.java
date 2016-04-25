package uk.gov.hscic.patient.medications.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;
import uk.gov.hscic.patient.medications.model.MedicationEntity;

public class MedicationEntityToListTransformer implements Transformer<MedicationEntity, MedicationListHTML> {

    @Override
    public MedicationListHTML transform(final MedicationEntity medicationEntity) {
        final MedicationListHTML medicationList = new MedicationListHTML();

        medicationList.setSourceId(String.valueOf(medicationEntity.getId()));
        medicationList.setSource(RepoSourceType.LEGACY.getSourceName());

        medicationList.setProvider(medicationEntity.getProvider());
        medicationList.setHtml(medicationEntity.getHtml());

        return medicationList;
    }
}
