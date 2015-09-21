package org.rippleosi.patient.contacts.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.contacts.model.ContactDetails;

/**
 */
@InOnly
public interface ContactStore extends Repository {

    void create(@Header("patientId") String patientId, @Body ContactDetails contact);

    void update(@Header("patientId") String patientId, @Body ContactDetails contact);
}
