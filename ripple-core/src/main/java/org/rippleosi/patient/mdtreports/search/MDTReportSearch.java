package org.rippleosi.patient.mdtreports.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;

/**
 */
public interface MDTReportSearch extends Repository {

    List<MDTReportSummary> findAllMDTReports(String patientId);

    MDTReportDetails findMDTReport(String patientId, String mdtReportId);
}
