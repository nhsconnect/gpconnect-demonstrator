package uk.gov.hscic.medication.dispense.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseDetail;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseEntity;

public class MedicationDispenseEntityToMedicationDispenseDetailTransformer implements Transformer<MedicationDispenseEntity, MedicationDispenseDetail> {

    @Override
    public MedicationDispenseDetail transform(MedicationDispenseEntity item) {
        MedicationDispenseDetail dispenseDetail = new MedicationDispenseDetail();
        dispenseDetail.setId(item.getId());
        dispenseDetail.setStatus(item.getStatus());
        dispenseDetail.setPatientId(item.getPatientId());
        dispenseDetail.setMedicationOrderId(item.getMedicationOrderId());
        dispenseDetail.setMedicationId(item.getMedicationId());
        dispenseDetail.setMedicationName(item.getMedicationName());
        dispenseDetail.setDosageText(item.getDosageText());
        return dispenseDetail;
    }
}
