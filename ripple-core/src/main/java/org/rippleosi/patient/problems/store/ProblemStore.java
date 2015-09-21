package org.rippleosi.patient.problems.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.problems.model.ProblemDetails;

/**
 */
@InOnly
public interface ProblemStore extends Repository {

    void create(@Header("patientId") String patientId, @Body ProblemDetails problem);

    void update(@Header("patientId") String patientId, @Body ProblemDetails problem);
}
