package net.nhs.esb.rest;

import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.openehr.model.ActionRestResponseData;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.openehr.model.QueryResponseData;
import net.nhs.esb.rest.domain.EhrResponse;
import net.nhs.esb.rest.domain.LoginResponse;
import org.springframework.stereotype.Controller;

@Controller
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OpenEhr {

    @POST
    @Path("/session")
    public LoginResponse login(@QueryParam("username") String username, @QueryParam("password") String password) {
        return null;
    }

    @GET
    @Path("/ehr")
    public EhrResponse ehr(@QueryParam("subjectNamespace") String subjectNamespace, @QueryParam("subjectId") String subjectId) {
        return null;
    }

    @GET
    @Path("/query")
    public QueryResponseData query(@QueryParam("aql") String aql) {
        return null;
    }

    @GET
    @Path("/composition/{compositionId}")
    public CompositionResponseData findComposition(@PathParam("compositionId") String compositionId, @QueryParam("format") String format) {
        return null;
    }

    @POST
    @Path("/composition")
    public ActionRestResponseData createComposition(@QueryParam("templateId") String templateId, @QueryParam("ehrId") String ehrId, @QueryParam("format") String format, Map<String,Object> body) {
        return null;
    }

    @PUT
    @Path("/composition/{compositionId}")
    public ActionRestResponseData updateComposition(@PathParam("compositionId") String compositionId, @QueryParam("templateId") String templateId, @QueryParam("ehrId") String ehrId, @QueryParam("format") String format, Map<String,Object> body) {
        return null;
    }

    @DELETE
    @Path("/composition/{compositionId}")
    public ActionRestResponseData deleteComposition(@PathParam("compositionId") String compositionId) {
        return null;
    }
}
