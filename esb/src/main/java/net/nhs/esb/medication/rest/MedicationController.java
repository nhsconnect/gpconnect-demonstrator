package net.nhs.esb.medication.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.medication.model.MedicationComposition;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicationController {

    @GET
    @Path("/{patientId}/medications")
    public MedicationComposition findPatientMedicationComposition(@PathParam("patientId") Long patientId) {
        return null;
    }

    @PUT
    @Path("/{patientId}/medications")
    public void createPatientMedicationComposition(@PathParam("patientId") Long patientId, MedicationComposition medicationComposition) {

    }

    @POST
    @Path("/{patientId}/medications")
    public void updatePatientMedicationComposition(@PathParam("patientId") Long patientId, MedicationComposition medicationComposition) {

    }
}
