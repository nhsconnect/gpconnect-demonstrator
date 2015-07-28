package net.nhs.esb.mdtreport.rest;

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

import net.nhs.esb.mdtreport.model.MDTReportComposition;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MDTReportController {

    @GET
    @Path("/{patientId}/mdtreports")
    public List<MDTReportComposition> findMDTReports(@PathParam("patientId") Long patientId) {
        return Collections.emptyList();
    }

    @POST
    @Path("/{patientId}/mdtreports")
    public void createMDTReport(@PathParam("patientId") Long patientId, MDTReportComposition mdtReportComposition) {

    }

    @PUT
    @Path("/{patientId}/mdtreports")
    public void updateMDTReport(@PathParam("patientId") Long patientId, MDTReportComposition mdtReportComposition) {

    }
}
