package uk.gov.hscic.appointment.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.model.appointment.ScheduleDetail;

@Service
public class ScheduleStore {
    private final ScheduleEntityToScheduleDetailTransformer entityToDetailTransformer = new ScheduleEntityToScheduleDetailTransformer();
    @Autowired
    private ScheduleRepository scheduleRepository;

    public ScheduleDetail findSchedule(Long id){
        ScheduleEntity scheduleEntity = scheduleRepository.getById(id);
        return entityToDetailTransformer.transform(scheduleEntity);
    }
}
