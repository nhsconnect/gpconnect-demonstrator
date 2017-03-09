package uk.gov.hscic.appointment.schedule.search;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.schedule.model.ScheduleDetail;
import uk.gov.hscic.appointment.schedule.model.ScheduleEntity;
import uk.gov.hscic.appointment.schedule.repo.ScheduleRepository;

@Service
public class ScheduleSearch {
    private final ScheduleEntityToScheduleDetailTransformer transformer = new ScheduleEntityToScheduleDetailTransformer();

    @Autowired
    private ScheduleRepository scheduleRepository;

    public ScheduleDetail findScheduleByID(Long id) {
        final ScheduleEntity item = scheduleRepository.findOne(id);

        return item == null
                ? null
                : transformer.transform(item);
    }

    public List<ScheduleDetail> findScheduleForLocationId(Long locationId, Date startDate, Date endDate) {
        return scheduleRepository.findByLocationIdAndEndDateTimeAfterAndStartDateTimeBefore(locationId, startDate, endDate)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
