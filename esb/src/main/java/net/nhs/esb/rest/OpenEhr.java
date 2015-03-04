package net.nhs.esb.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.rest.domain.EhrDiagnosisRequest;
import net.nhs.esb.rest.domain.EhrDiagnosisResponse;
import net.nhs.esb.rest.domain.EhrResponse;
import net.nhs.esb.rest.domain.LoginResponse;
import net.nhs.esb.rest.domain.QueryResponseData;
import org.springframework.stereotype.Controller;

@Controller
@Path("/")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
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

    @POST
    @Path("/composition")
    public EhrDiagnosisResponse create(EhrDiagnosisRequest ehrDiagnosisRequest, @QueryParam("templateId") String templateId, @QueryParam("ehrId") String ehrId, @QueryParam("format") String format) {
        return null;
    }

    @PUT
    @Path("/composition/{compositionId}")
    public EhrDiagnosisResponse update(EhrDiagnosisRequest ehrDiagnosisRequest, @PathParam("compositionId") String compositionId, @QueryParam(value = "templateId") String templateId, @QueryParam(value = "ehrId") String ehrId, @QueryParam(value = "format") String format) {
        return null;
    }
}
