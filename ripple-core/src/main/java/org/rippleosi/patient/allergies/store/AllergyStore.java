package org.rippleosi.patient.allergies.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.allergies.model.AllergyDetails;

/**
 */
@InOnly
public interface AllergyStore extends Repository {

    void create(@Header("patientId") String patientId, @Body AllergyDetails allergy);

    void update(@Header("patientId") String patientId, @Body AllergyDetails allergy);
}
