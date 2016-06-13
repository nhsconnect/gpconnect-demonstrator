package uk.gov.hscic.medications.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.medication.model.MedicationDetails;
import uk.gov.hscic.medications.model.MedicationEntity;

public class MedicationEntityToMedicationTransformer  implements Transformer<MedicationEntity, MedicationDetails> {

    @Override
    public MedicationDetails transform(MedicationEntity item) {
        
        MedicationDetails medication = new MedicationDetails();
        medication.setId(String.valueOf(item.getId()));
        medication.setName(item.getName());
        
        return medication;
    }
    
}
