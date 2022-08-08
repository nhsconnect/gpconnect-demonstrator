package uk.gov.hscic.appointment.slot.search;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.slot.model.SlotDetail;
import uk.gov.hscic.appointment.slot.model.SlotEntity;
import uk.gov.hscic.appointment.slot.repo.SlotRepository;

@Service
public class SlotSearch {
    private final SlotEntityToSlotDetailTransformer transformer = new SlotEntityToSlotDetailTransformer();

    @Autowired
    private SlotRepository slotRepository;

    public SlotDetail findSlotByID(Long id) {
        final SlotEntity item = slotRepository.findById(id).get();

        return item == null
                ? null
                : transformer.transform(item);
    }

    public List<SlotDetail> findSlotsForScheduleId(Long scheduleId, Date startDate, Date endDate) {
        return slotRepository.findByScheduleReferenceAndEndDateTimeAfterAndStartDateTimeBefore(scheduleId, startDate, endDate)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
