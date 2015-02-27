package net.nhs.esb.patient.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.patient.model.PatientDetailsArray;
import net.nhs.esb.patient.model.PatientSearchCriteria;
import org.springframework.stereotype.Controller;

/**
 * Placeholder Controller for Camel routes.  The methods in this class are
 * never called.  Instead they are only needed to expose the json interface.
 * See {net.nhs.esb.patient.route.PatientSearchRouteBuilder} for
 * actual implementation details.
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientSearchController {

    @POST
    @Path("/smsp-search")
    public PatientDetailsArray getPatientDetailsBySearch(PatientSearchCriteria searchCriteria) {
        return null;
    }
}
