package uk.gov.hscic.medications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {

    // TODO there's a harcoded schema name here not sure it can be easily removed though
    @Query(value="SELECT m.id FROM gpconnect1_5.medications m WHERE m.display=?1", nativeQuery = true)
    Long getMedicationIdByName(String name);
}