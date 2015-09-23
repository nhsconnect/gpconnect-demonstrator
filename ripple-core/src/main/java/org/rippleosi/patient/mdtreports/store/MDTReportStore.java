package org.rippleosi.patient.mdtreports.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;

/**
 */
@InOnly
public interface MDTReportStore extends Repository {

    void create(@Header("patientId") String patientId, @Body MDTReportDetails mdtReport);

    void update(@Header("patientId") String patientId, @Body MDTReportDetails mdtReport);
}
