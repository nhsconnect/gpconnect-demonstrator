package uk.gov.hscic.appointment.slot.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Modifying
    @Transactional
    public SlotDetail saveSlot(SlotDetail slotDetail) {
        SlotEntity slotEntity = detailToEntityTransformer.transform(slotDetail);
        slotEntity = slotRepository.saveAndFlush(slotEntity);
        return entityToDetailTransformer.transform(slotEntity);
    }
}
