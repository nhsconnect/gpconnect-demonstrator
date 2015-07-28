package net.nhs.esb.cancermdt.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.cancermdt.model.CancerMDTComposition;
import net.nhs.esb.cancermdt.model.CancerMDTsComposition;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CancerMDTController {

    @GET
    @Path("/{patientId}/cancermdt")
    public CancerMDTComposition findPatientCancerMDTComposition(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/cancermdt")
    public void createPatientCancerMDTComposition(@PathParam("patientId") Long patientId, CancerMDTsComposition cancerMDTsComposition) {

    }

    @PUT
    @Path("/{patientId}/cancermdt")
    public void updatePatientCancerMDTComposition(@PathParam("patientId") Long patientId, CancerMDTsComposition cancerMDTsComposition) {

    }
}
