package org.rippleosi.patient.summary.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;

/**
 */
public interface PatientSearch extends Repository {

    List<PatientSummary> findAllPatients();

    PatientDetails findPatient(String patientId);
}
