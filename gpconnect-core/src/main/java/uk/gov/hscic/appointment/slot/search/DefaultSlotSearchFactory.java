package uk.gov.hscic.appointment.slot.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultSlotSearchFactory extends AbstractRepositoryFactory<SlotSearch> implements SlotSearchFactory {

    @Override
    protected SlotSearch defaultRepository() {
        return new NotConfiguredSlotSearch();
    }

    @Override
    protected Class<SlotSearch> repositoryClass() {
        return SlotSearch.class;
    }
}