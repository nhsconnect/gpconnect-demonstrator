package net.nhs.esb.lab.results.rest;

import java.util.Collections;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.lab.results.model.LabResult;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LabResultController {

    @GET
    @Path("/{patientId}/labresults")
    public List<LabResult> findLabResults(@PathParam("patientId") Long patientId) {
        return Collections.emptyList();
    }
}
