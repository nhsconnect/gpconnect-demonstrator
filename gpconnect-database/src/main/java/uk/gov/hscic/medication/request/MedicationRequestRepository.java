package uk.gov.hscic.medication.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRequestRepository extends JpaRepository<MedicationRequestEntity, Long>{
	
	public List<MedicationRequestEntity> findByIntentCodeAndGroupIdentifier(String intentCode, String groupIdentifier);
}
