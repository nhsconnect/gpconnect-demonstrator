package uk.gov.hscic.appointment.slot.store;

import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.common.repo.Repository;

public interface SlotStore extends Repository {
    
    SlotDetail saveSlot(SlotDetail slotDetail);
    
}
