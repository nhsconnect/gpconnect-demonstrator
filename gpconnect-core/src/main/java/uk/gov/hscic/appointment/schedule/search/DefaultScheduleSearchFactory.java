package uk.gov.hscic.appointment.schedule.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultScheduleSearchFactory extends AbstractRepositoryFactory<ScheduleSearch> implements ScheduleSearchFactory {

    @Override
    protected ScheduleSearch defaultRepository() {
        return new NotConfiguredScheduleSearch();
    }

    @Override
    protected Class<ScheduleSearch> repositoryClass() {
        return ScheduleSearch.class;
    }
}