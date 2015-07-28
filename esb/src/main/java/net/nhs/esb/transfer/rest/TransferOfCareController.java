package net.nhs.esb.transfer.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.transfer.model.TransferOfCare;
import net.nhs.esb.transfer.model.TransferOfCareComposition;
import net.nhs.esb.transfer.model.TransferOfCareSummary;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferOfCareController {

    @GET
    @Path("/{patientId}/transfer-of-care/summary")
    public TransferOfCareSummary findPatientTransferSummary(@PathParam("patientId") Long patientId) {
        return null;
    }


    @POST
    @Path("/{patientId}/transfer-of-care")
    public void createPatientTransferComposition(@PathParam("patientId") Long patientId, TransferOfCareComposition transferOfCareComposition) {

    }

    @PUT
    @Path("/{patientId}/transfer-of-care")
    public void updatePatientTransferComposition(@PathParam("patientId") Long patientId, TransferOfCareComposition transferOfCareComposition) {

    }

    @GET
    @Path("/{patientId}/transfer-of-care")
    public TransferOfCareComposition findPatientTransferComposition(@PathParam("patientId") Long patientId) {
        return null;
    }

    @DELETE
    @Path("/{patientId}/transfer-of-care/{compositionId}")
    public void deletePatientTransferComposition(@PathParam("patientId") Long patientId, @PathParam("compositionId") String compositionId) {

    }

    @GET
    @Path("/{patientId}/transfer-of-care/{transferOfCareId}")
    public TransferOfCare findTransferOfCare(@PathParam("patientId") Long patitentId, @PathParam("transferOfCareId") Long transferOfCareId) {
        return null;
    }
}
