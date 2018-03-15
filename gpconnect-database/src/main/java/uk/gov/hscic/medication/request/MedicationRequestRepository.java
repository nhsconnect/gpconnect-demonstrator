package uk.gov.hscic.medication.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRequestRepository extends JpaRepository<MedicationRequestEntity, Long>{

}
