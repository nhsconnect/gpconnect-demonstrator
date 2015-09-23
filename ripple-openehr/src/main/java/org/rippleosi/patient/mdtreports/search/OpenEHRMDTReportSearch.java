package org.rippleosi.patient.mdtreports.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRMDTReportSearch extends AbstractOpenEhrService implements MDTReportSearch {

    @Override
    public List<MDTReportSummary> findAllMDTReports(String patientId) {
        MDTReportSummaryQueryStrategy query = new MDTReportSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public MDTReportDetails findMDTReport(String patientId, String mdtReportId) {
        MDTReportDetailsQueryStrategy query = new MDTReportDetailsQueryStrategy(patientId, mdtReportId);

        return findData(query);
    }
}
