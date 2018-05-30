package uk.gov.hscic.medication.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRequestRepository extends JpaRepository<MedicationRequestEntity, Long>{
	
	public List<MedicationRequestEntity> findByIntentCodeAndGroupIdentifier(String intentCode, String groupIdentifier);

    @Query(value = "SELECT mr FROM MedicationRequestEntity mr where mr.guid = :guid")
    public MedicationRequestEntity findByGUID(@Param("guid") String requestGUID);
//




}
