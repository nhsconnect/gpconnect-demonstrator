package uk.gov.hscic.appointment.slot.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.model.SlotEntity;
import uk.gov.hscic.appointment.slot.repo.SlotRepository;
import uk.gov.hscic.appointment.slot.search.SlotEntityToSlotDetailTransformer;

@Service
public class SlotStore {
    private final SlotEntityToSlotDetailTransformer entityToDetailTransformer = new SlotEntityToSlotDetailTransformer();
    private final SlotDetailToSlotEntityTransformer detailToEntityTransformer = new SlotDetailToSlotEntityTransformer();

    @Autowired
    private SlotRepository slotRepository;

    public SlotDetail saveSlot(SlotDetail slotDetail) {
        SlotEntity slotEntity = detailToEntityTransformer.transform(slotDetail);
        slotEntity = slotRepository.saveAndFlush(slotEntity);
        return entityToDetailTransformer.transform(slotEntity);
    }

    public void clearSlots(){
        slotRepository.deleteAll();
    }
}
