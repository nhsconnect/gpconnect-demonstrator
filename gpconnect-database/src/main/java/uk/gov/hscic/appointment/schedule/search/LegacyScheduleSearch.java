package uk.gov.hscic.appointment.schedule.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.schedule.model.ScheduleDetail;
import uk.gov.hscic.appointment.schedule.model.ScheduleEntity;
import uk.gov.hscic.appointment.schedule.repo.ScheduleRepository;
import uk.gov.hscic.common.service.AbstractLegacyService;

@Service
public class LegacyScheduleSearch extends AbstractLegacyService implements ScheduleSearch {

    @Autowired
    private ScheduleRepository scheduleRepository;
    
    private final ScheduleEntityToScheduleDetailTransformer transformer = new ScheduleEntityToScheduleDetailTransformer();
    
    @Override
    public ScheduleDetail findScheduleByID(Long id) {
        final ScheduleEntity item = scheduleRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }

    @Override
    public List<ScheduleDetail> findScheduleForLocationId(Long locationId, Date startDate, Date endDate) {
        List<ScheduleEntity> items = scheduleRepository.findByLocationIdAndEndDateTimeAfterAndStartDateTimeBefore(locationId, startDate, endDate);
        ArrayList<ScheduleDetail> scheduleDetails = new ArrayList();
        for(ScheduleEntity entity : items){
            scheduleDetails.add(transformer.transform(entity));
        }
        return scheduleDetails;
    }
    
}
