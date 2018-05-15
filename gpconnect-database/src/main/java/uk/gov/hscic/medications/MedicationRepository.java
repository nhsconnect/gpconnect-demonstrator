package uk.gov.hscic.medications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {

    @Query(value="SELECT m.id FROM gpconnect.medications m WHERE m.display=?1", nativeQuery = true)
    Long getMedicationIdByName(String name);
}