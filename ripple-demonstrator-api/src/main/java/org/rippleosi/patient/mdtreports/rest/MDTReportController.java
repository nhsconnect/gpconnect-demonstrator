package org.rippleosi.patient.mdtreports.rest;

import java.util.List;

import org.rippleosi.patient.mdtreports.model.MDTReportDetails;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;
import org.rippleosi.patient.mdtreports.search.MDTReportSearch;
import org.rippleosi.patient.mdtreports.search.MDTReportSearchFactory;
import org.rippleosi.patient.mdtreports.store.MDTReportStore;
import org.rippleosi.patient.mdtreports.store.MDTReportStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/mdtreports")
public class MDTReportController {

    @Autowired
    private MDTReportSearchFactory mdtReportSearchFactory;

    @Autowired
    private MDTReportStoreFactory mdtReportStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<MDTReportSummary> findAllMDTReports(@PathVariable("patientId") String patientId,
                                                    @RequestParam(required = false) String source) {
        MDTReportSearch mdtReportSearch = mdtReportSearchFactory.select(source);

        return mdtReportSearch.findAllMDTReports(patientId);
    }

    @RequestMapping(value = "/{reportId}", method = RequestMethod.GET)
    public MDTReportDetails findMDTReport(@PathVariable("patientId") String patientId,
                                          @PathVariable("reportId") String reportId,
                                          @RequestParam(required = false) String source) {
        MDTReportSearch mdtReportSearch = mdtReportSearchFactory.select(source);

        return mdtReportSearch.findMDTReport(patientId, reportId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createMDTReport(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody MDTReportDetails mdtReport) {
        MDTReportStore mdtReportStore = mdtReportStoreFactory.select(source);

        mdtReportStore.create(patientId, mdtReport);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateMDTReport(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody MDTReportDetails mdtReport) {
        MDTReportStore mdtReportStore = mdtReportStoreFactory.select(source);

        mdtReportStore.update(patientId, mdtReport);
    }
}
