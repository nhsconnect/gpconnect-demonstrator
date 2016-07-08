package uk.gov.hscic.appointment.slot.store;

import uk.gov.hscic.common.repo.RepositoryFactory;

@FunctionalInterface
public interface SlotStoreFactory extends RepositoryFactory<SlotStore> {

}
