package uk.gov.hscic.appointment.schedule.search;

import java.util.Date;
import java.util.List;
import uk.gov.hscic.appointment.schedule.model.ScheduleDetail;
import uk.gov.hscic.common.repo.Repository;

public interface ScheduleSearch extends Repository {
    
    ScheduleDetail findScheduleByID(Long id);
    
    List<ScheduleDetail> findScheduleForLocationId(Long locationId, Date startDate, Date endDate);
    
}