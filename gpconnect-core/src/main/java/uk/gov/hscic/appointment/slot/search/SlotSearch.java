package uk.gov.hscic.appointment.slot.search;

import java.util.Date;
import java.util.List;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.common.repo.Repository;

public interface SlotSearch extends Repository {
    
    SlotDetail findSlotByID(Long id);
    
    List<SlotDetail> findSlotsForScheduleId(Long scheduleId, Date startDate, Date endDate);
    
}