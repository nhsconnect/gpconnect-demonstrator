package net.nhs.esb.endoflife.rest;

import java.util.Collections;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.endoflife.model.EndOfLifeComposition;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EndOfLifeController {

    @GET
    @Path("/{patientId}/eolcareplans")
    public List<EndOfLifeComposition> findPatientEndOfLifePlan(@PathParam("patientId") Long patientId) {
        return Collections.emptyList();
    }

    @POST
    @Path("/{patientId}/eolcareplans")
    public void createPatientEndOfLifePlan(@PathParam("patientId") Long patientId, EndOfLifeComposition endOfLifeComposition) {

    }

    @PUT
    @Path("/{patientId}/eolcareplans")
    public void updatePatientEndOfLifePlan(@PathParam("patientId") Long patientId, EndOfLifeComposition endOfLifeComposition) {

    }
}
