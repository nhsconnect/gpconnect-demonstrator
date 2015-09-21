package org.rippleosi.patient.procedures.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.procedures.model.ProcedureDetails;

/**
 */
@InOnly
public interface ProcedureStore extends Repository {

    void create(@Header("patientId") String patientId, @Body ProcedureDetails procedure);

    void update(@Header("patientId") String patientId, @Body ProcedureDetails procedure);
}
