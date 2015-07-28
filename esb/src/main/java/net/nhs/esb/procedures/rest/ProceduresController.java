package net.nhs.esb.procedures.rest;

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

import net.nhs.esb.procedures.model.ProcedureComposition;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProceduresController {

    @GET
    @Path("/{patientId}/procedures")
    public List<ProcedureComposition> findPatientProcedures(@PathParam("patientId") Long patientId) {
        return Collections.emptyList();
    }

    @POST
    @Path("/{patientId}/procedures")
    public void createPatientProcedureComposition(@PathParam("patientId") Long patientId, ProcedureComposition procedureComposition) {

    }

    @PUT
    @Path("/{patientId}/procedures")
    public void updatePatientProcedureComposition(@PathParam("patientId") Long patientId, ProcedureComposition procedureComposition) {

    }
}
