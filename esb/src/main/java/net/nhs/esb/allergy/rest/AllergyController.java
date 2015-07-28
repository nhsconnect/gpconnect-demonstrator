package net.nhs.esb.allergy.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.allergy.model.AllergyComposition;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AllergyController {

    @GET
    @Path("/{patientId}/allergies")
    public AllergyComposition findPatientAllergyComposition(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/allergies")
    public void createPatientAllergyComposition(@PathParam("patientId") Long patientId, AllergyComposition allergyComposition) {

    }

    @PUT
    @Path("/{patientId}/allergies")
    public void updatePatientAllergyComposition(@PathParam("patientId") Long patientId, AllergyComposition allergyComposition) {

    }

    @DELETE
    @Path("/{patientId}/allergies/{compositionId}")
    public void deletePatientAllergyComposition(@PathParam("patientId") Long patientId, @PathParam("compositionId") String compositionId) {

    }
}
