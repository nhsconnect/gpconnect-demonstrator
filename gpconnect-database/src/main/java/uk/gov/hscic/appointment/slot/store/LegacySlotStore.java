package uk.gov.hscic.appointment.slot.store;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.model.SlotEntity;
import uk.gov.hscic.appointment.slot.repo.SlotRepository;
import uk.gov.hscic.appointment.slot.search.SlotEntityToSlotDetailTransformer;
import uk.gov.hscic.common.service.AbstractLegacyService;

@Service
public class LegacySlotStore extends AbstractLegacyService implements SlotStore {

    @Autowired
    private SlotRepository slotRepository;

    private final SlotEntityToSlotDetailTransformer entityToDetailTransformer = new SlotEntityToSlotDetailTransformer();
    private final SlotDetailToSlotEntityTransformer detailToEntityTransformer = new SlotDetailToSlotEntityTransformer();

    @Override
    public SlotDetail saveSlot(SlotDetail slotDetail) {
        SlotEntity slotEntity = detailToEntityTransformer.transform(slotDetail);
        slotEntity = slotRepository.saveAndFlush(slotEntity);
        return entityToDetailTransformer.transform(slotEntity);
    }
    
    public void clearSlots(){
        slotRepository.deleteAll();
    }
}
