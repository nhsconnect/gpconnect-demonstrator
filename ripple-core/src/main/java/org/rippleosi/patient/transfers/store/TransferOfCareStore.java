package org.rippleosi.patient.transfers.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;

@InOnly
public interface TransferOfCareStore extends Repository {

    void create(@Header("patientId") String patientId, @Body TransferOfCareDetails transferOfCare);
}
