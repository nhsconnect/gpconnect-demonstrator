package org.rippleosi.patient.mdtreports.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;

/**
 */
public class NotConfiguredMDTReportStore implements MDTReportStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body MDTReportDetails mdtReport) {
        throw ConfigurationException.unimplementedTransaction(MDTReportStore.class);
    }

    @Override
    public void update(@Header("patientId") String patientId, @Body MDTReportDetails mdtReport) {
        throw ConfigurationException.unimplementedTransaction(MDTReportStore.class);
    }
}
