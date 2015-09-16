package org.rippleosi.patient.laborders.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.laborders.model.LabOrderDetails;

/**
 */
@InOnly
public interface LabOrderStore extends Repository {

    void create(@Header("patientId") String patientId, @Body LabOrderDetails labOrder);

    void update(@Header("patientId") String patientId, @Body LabOrderDetails labOrder);
}
