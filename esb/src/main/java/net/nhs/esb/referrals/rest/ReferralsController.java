package net.nhs.esb.referrals.rest;

import net.nhs.esb.referrals.model.Referral;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RestController
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/{patientId}/referrals")
public class ReferralsController {

    @GET
    public List<Referral> findAllPatientReferralCompositions(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    public void createPatientReferralsComposition(@PathParam("patientId") Long patientId, Referral referral) {
    }

    @PUT
    public void updatePatientReferralsComposition(@PathParam("patientId") Long patientId, Referral referral) {
    }
}
