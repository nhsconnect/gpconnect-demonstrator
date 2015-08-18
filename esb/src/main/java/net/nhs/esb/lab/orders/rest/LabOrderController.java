package net.nhs.esb.lab.orders.rest;

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

import net.nhs.esb.lab.orders.model.LabOrder;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LabOrderController {

    @GET
    @Path("/{patientId}/laborders")
    public List<LabOrder> findLabOrders(@PathParam("patientId") Long patientId) {
        return Collections.emptyList();
    }

    @POST
    @Path("/{patientId}/laborders")
    public void createLabOrderComposition(@PathParam("patientId") Long patientId, LabOrder labOrder) {

    }

    @PUT
    @Path("/{patientId}/laborders")
    public void updateLabOrderComposition(@PathParam("patientId") Long patientId, LabOrder labOrder) {

    }
}
