package uk.gov.hscic.appointment.schedule.search;

import java.util.Date;
import java.util.List;
import uk.gov.hscic.appointment.schedule.model.ScheduleDetail;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

public class NotConfiguredScheduleSearch  implements ScheduleSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public ScheduleDetail findScheduleByID(Long id) {
        throw ConfigurationException.unimplementedTransaction(ScheduleSearch.class);
    }

    @Override
    public List<ScheduleDetail> findScheduleForLocationId(Long locationId, Date startDate, Date endDate) {
        throw ConfigurationException.unimplementedTransaction(ScheduleSearch.class);
    }
}
