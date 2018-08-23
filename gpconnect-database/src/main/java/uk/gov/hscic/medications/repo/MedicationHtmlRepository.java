package uk.gov.hscic.medications.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;

import java.util.List;

public interface MedicationHtmlRepository extends JpaRepository<PatientMedicationHtmlEntity, Long> {
    List<PatientMedicationHtmlEntity> findBynhsNumber(String patientId);


    @Query(value = "SELECT * FROM gpconnect.medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumber(@Param("nhsNr") String patientId, @Param("currRepPast") String currRepPast);


    @Query(value = "SELECT * FROM gpconnect.medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate between :from and :to", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAndBetweenDates(@Param("nhsNr") String patientId, @Param("from") String from, @Param("to") String to, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM gpconnect.medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate >= :from", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAfterDate(@Param("nhsNr") String patientId, @Param("from") String from, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM gpconnect.medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate <= :to", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAndBeforeDate(@Param("nhsNr") String patientId, @Param("to") String to, @Param("currRepPast") String currRepPast);
}
