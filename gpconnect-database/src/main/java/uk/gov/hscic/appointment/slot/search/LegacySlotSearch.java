package uk.gov.hscic.appointment.slot.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.model.SlotEntity;
import uk.gov.hscic.appointment.slot.repo.SlotRepository;
import uk.gov.hscic.common.service.AbstractLegacyService;

@Service
public class LegacySlotSearch extends AbstractLegacyService implements SlotSearch {

    @Autowired
    private SlotRepository slotRepository;
    
    private final SlotEntityToSlotDetailTransformer transformer = new SlotEntityToSlotDetailTransformer();
    
    @Override
    public SlotDetail findSlotByID(Long id) {
        final SlotEntity item = slotRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }

    @Override
    public List<SlotDetail> findSlotsForScheduleId(Long scheduleId, Date startDate, Date endDate) {
        List<SlotEntity> items = slotRepository.findByScheduleReferenceAndEndDateTimeAfterAndStartDateTimeBefore(scheduleId, startDate, endDate);
        ArrayList<SlotDetail> slotDetails = new ArrayList();
        for(SlotEntity entity : items){
            slotDetails.add(transformer.transform(entity));
        }
        return slotDetails;
    }
    
}
