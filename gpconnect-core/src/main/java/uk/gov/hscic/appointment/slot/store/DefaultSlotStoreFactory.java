package uk.gov.hscic.appointment.slot.store;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultSlotStoreFactory  extends AbstractRepositoryFactory<SlotStore> implements SlotStoreFactory {

    @Override
    protected SlotStore defaultRepository() {
        return new NotConfiguredSlotStore();
    }

    @Override
    protected Class<SlotStore> repositoryClass() {
        return SlotStore.class;
    }
}