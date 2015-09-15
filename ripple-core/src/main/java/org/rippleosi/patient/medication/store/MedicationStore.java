package org.rippleosi.patient.medication.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.medication.model.MedicationDetails;

/**
 */
@InOnly
public interface MedicationStore extends Repository {

    void create(@Header("patientId") String patientId, @Body MedicationDetails medication);

    void update(@Header("patientId") String patientId, @Body MedicationDetails medication);
}
