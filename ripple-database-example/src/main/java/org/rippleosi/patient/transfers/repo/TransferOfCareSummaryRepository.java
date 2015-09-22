package org.rippleosi.patient.transfers.repo;

import java.util.List;

import org.rippleosi.patient.transfers.model.TransferOfCareSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferOfCareSummaryRepository extends JpaRepository<TransferOfCareSummaryEntity, Long> {

    @Query("SELECT toc FROM TransferOfCareSummaryEntity toc WHERE toc.patient.nhsNumber=:patientId")
    List<TransferOfCareSummaryEntity> findAllByPatientId(@Param("patientId") String patientId);
}
