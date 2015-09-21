package org.rippleosi.patient.labresults.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.rippleosi.patient.labresults.model.LabResultSummary;

/**
 */
public class NotConfiguredLabResultSearch implements LabResultSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<LabResultSummary> findAllLabResults(String patientId) {
        throw ConfigurationException.unimplementedTransaction(LabResultSearch.class);
    }

    @Override
    public LabResultDetails findLabResult(String patientId, String labResultId) {
        throw ConfigurationException.unimplementedTransaction(LabResultSearch.class);
    }
}
