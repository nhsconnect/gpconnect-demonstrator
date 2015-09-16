package org.rippleosi.patient.labresults.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.rippleosi.patient.labresults.model.LabResultSummary;

/**
 */
public interface LabResultSearch extends Repository {

    List<LabResultSummary> findAllLabResults(String patientId);

    LabResultDetails findLabResult(String patientId, String labResultId);
}
