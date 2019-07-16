package uk.gov.hscic.medications.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;

import java.util.List;

public interface MedicationHtmlRepository extends JpaRepository<PatientMedicationHtmlEntity, Long> {

    List<PatientMedicationHtmlEntity> findBynhsNumberOrderByStartDateDesc(String patientId);

    @Query(value = "SELECT * FROM medications_html md"
            + " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumber(@Param("nhsNr") String patientId, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md"
            + " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate between :from and :to", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAndBetweenDates(@Param("nhsNr") String patientId, @Param("from") String from, @Param("to") String to, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md"
            + " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate >= :from", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAfterDate(@Param("nhsNr") String patientId, @Param("from") String from, @Param("currRepPast") String currRepPast);

    @Query(value = "SELECT * FROM medications_html md"
            + " where md.nhsNumber = :nhsNr and md.currentRepeatPast=:currRepPast and md.startDate <= :to", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findByNhsNumberAndBeforeDate(@Param("nhsNr") String patientId, @Param("to") String to, @Param("currRepPast") String currRepPast);

    // see https://developer.nhs.uk/apis/gpconnect-0-7-2/accessrecord_view_medications.html for search date selection rules 
    
    // all medication items
    @Query(value = "SELECT * FROM medications_html md"
            + " where md.nhsNumber = :nhsNr"
            + " and ("
            + "(:from is null and :to is null) or "
            + "md.startDate is not null and ("
            + "     (md.scheduledEnd is not null and (not (:from is not null and md.scheduledEnd < :from or :to is not null and md.startDate > :to ))) or"
            + "     (md.scheduledEnd is null and (((md.typeMed = 'Repeat' and (:to is not null and md.startDate < :to or :from is not null and md.startDate < :from ))) or "
            + "                                   ((md.typeMed != 'Repeat' and (:from is not null and md.startDate > :from and :to is not null and md.startDate < :to )))))"
            + ")"
            + ")"
            + " order by md.medicationItem asc, md.lastIssued desc", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumberOrderByMedicationItemAscLastIssuedDesc(@Param("nhsNr") String patientId, @Param("from") String from, @Param("to") String to);

    // see https://developer.nhs.uk/apis/gpconnect-0-7-2/accessrecord_view_medications.html for search date selection rules 
    // all medication items grouped
    @Query(value = "SELECT max(md.id) as id, md.typeMed, min(md.startDate) as startDate, md.medicationItem, md.dosageInstruction, md.quantity, md.currentRepeatPast, count(*) as numberIssued, max(md.startDate) as lastIssued, md.daysDuration, md.details, md.discontinuationReason, md.maxIssues, md.nhsNumber, md.reviewDate, md.scheduledEnd FROM medications_html md"
            + " where md.nhsNumber = :nhsNr "
            + " and ("
            + "(:from is null and :to is null) or "
            + "md.startDate is not null and ("
            + "     (md.scheduledEnd is not null and (not (:from is not null and md.scheduledEnd < :from or :to is not null and md.startDate > :to ))) or"
            + "     (md.scheduledEnd is null and (((md.typeMed = 'Repeat' and (:to is not null and md.startDate < :to or :from is not null and md.startDate < :from ))) or "
            + "                                   ((md.typeMed != 'Repeat' and (:from is not null and md.startDate > :from and :to is not null and md.startDate < :to )))))"
            + ")"
            + ")"
            + " group by md.typeMed, md.medicationItem, md.dosageInstruction, md.quantity, md.currentRepeatPast, md.daysDuration, md.details, md.discontinuationReason, md.maxIssues, md.nhsNumber, md.reviewDate, md.scheduledEnd"
            + " order by md.medicationItem asc, startDate desc", nativeQuery = true)
    List<PatientMedicationHtmlEntity> findBynhsNumberGrouped(@Param("nhsNr") String patientId, @Param("from") String from, @Param("to") String to);
}
