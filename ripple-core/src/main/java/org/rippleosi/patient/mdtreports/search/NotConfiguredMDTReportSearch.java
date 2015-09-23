package org.rippleosi.patient.mdtreports.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;

/**
 */
public class NotConfiguredMDTReportSearch implements MDTReportSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<MDTReportSummary> findAllMDTReports(String patientId) {
        throw ConfigurationException.unimplementedTransaction(MDTReportSearch.class);
    }

    @Override
    public MDTReportDetails findMDTReport(String patientId, String mdtReportId) {
        throw ConfigurationException.unimplementedTransaction(MDTReportSearch.class);
    }
}
