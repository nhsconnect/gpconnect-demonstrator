package uk.gov.hscic.patient.observations.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.hscic.patient.observations.model.ObservationEntity;

import java.util.List;

public interface ObservationRepository extends JpaRepository<ObservationEntity, Long> {
    List<ObservationEntity> findBynhsNumberOrderByObservationDateDesc(String patientNHSNumber);

    @Query(value = "Select * from observations ob where ob.observationDate >= :from and ob.nhsNumber = :nhsNr", nativeQuery = true)
    List<ObservationEntity> findByNhsNumberAndAfterDate(@Param("nhsNr") Long nhsNr, @Param("from") String startDate);

    @Query(value = "Select * from observations ob where ob.observationDate <= :to and ob.nhsNumber = :nhsNr", nativeQuery = true)
    List<ObservationEntity> findByNhsNumberAndBeforeDate(@Param("nhsNr") Long nhsNr, @Param("to") String endDate);

    @Query(value = "Select * from observations ob where  ob.observationDate between :from and :to and ob.nhsNumber = :nhsNr", nativeQuery = true)
    List<ObservationEntity> findByNhsNumberBetweenDates(
            @Param("nhsNr") Long nhsNr, @Param("from") String startDate, @Param("to") String endDate);

}
