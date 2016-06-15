package uk.gov.hscic.medication.orders.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.medication.order.model.MedicationOrderDetails;
import uk.gov.hscic.medication.order.search.MedicationOrderSearch;
import uk.gov.hscic.medication.orders.model.MedicationOrderEntity;
import uk.gov.hscic.medication.orders.repo.MedicationOrderRepository;

@Service
public class LegacyMedicationOrderSearch extends AbstractLegacyService implements MedicationOrderSearch {

    @Autowired
    private MedicationOrderRepository medicationOrderRepository;
    
    private final MedicationOrderEntityToMedicationOrderDetailsTransformer transformer = new MedicationOrderEntityToMedicationOrderDetailsTransformer();
    
    @Override
    public MedicationOrderDetails findMedicationOrderByID(Long id) {
        final MedicationOrderEntity item = medicationOrderRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }
    
    @Override
    public List<MedicationOrderDetails> findMedicationOrdersForPatient(Long patientId){
        List<MedicationOrderEntity> items = medicationOrderRepository.findByPatientId(patientId);
        ArrayList<MedicationOrderDetails> medicationOrders = new ArrayList();
        for(MedicationOrderEntity entity : items){
            medicationOrders.add(transformer.transform(entity));
        }
        return medicationOrders;
    }
    
}
