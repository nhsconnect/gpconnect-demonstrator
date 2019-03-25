package uk.gov.hscic.medications.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;

import java.util.List;

public interface MedicationHtmlRepository extends JpaRepository<PatientMedicationHtmlEntity, Long> {
    List<PatientMedicationHtmlEntity> findBynhsNumberOrderByStartDateDesc(String patientId);


    @Query(value = "SELECT * FROM medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumber(@Param("nhsNr") String patientId, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate between :from and :to", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAndBetweenDates(@Param("nhsNr") String patientId, @Param("from") String from, @Param("to") String to, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate >= :from", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAfterDate(@Param("nhsNr") String patientId, @Param("from") String from, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md" +
            " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate <= :to", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAndBeforeDate(@Param("nhsNr") String patientId, @Param("to") String to, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md" +
            " where md.nhsNumber = :nhsNr order by md.medicationItem asc, md.lastIssued desc", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumberOrderByMedicationItemAscLastIssuedDesc(@Param("nhsNr") String patientId);

    @Query(value = "SELECT max(md.id) as id, md.typeMed, min(md.startDate) as startDate, md.medicationItem, md.dosageInstruction, md.quantity, md.currentRepeatPast, count(*) as numberIssued, max(md.startDate) as lastIssued, md.daysDuration, md.details, md.discontinuationReason, md.maxIssues, md.nhsNumber, md.reviewDate, md.scheduledEnd FROM medications_html md" +
            " where md.nhsNumber = :nhsNr "+
            " group by md.typeMed, md.medicationItem, md.dosageInstruction, md.quantity, md.currentRepeatPast, md.daysDuration, md.details, md.discontinuationReason, md.maxIssues, md.nhsNumber, md.reviewDate, md.scheduledEnd" +
            " order by md.medicationItem asc, startDate desc", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumberGrouped(@Param("nhsNr") String patientId);
}
