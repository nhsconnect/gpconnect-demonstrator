package uk.gov.hscic.medication.order.search;

import uk.gov.hscic.common.repo.RepositoryFactory;

@FunctionalInterface
public interface MedicationOrderSearchFactory extends RepositoryFactory<MedicationOrderSearch> {
    
}
