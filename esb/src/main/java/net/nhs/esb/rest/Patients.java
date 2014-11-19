package net.nhs.esb.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.domain.openehr.model.Diagnoses;

import org.springframework.stereotype.Controller;

@Controller
@Path("/")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class Patients {
    
	public static final String PATIENT_ID_HEADER = "patientId";
	public static final String DIAGNOSIS_ID_HEADER = "diagnosisId";
	
	@GET
	@Path("/{"+ PATIENT_ID_HEADER + "}/diagnoses")
    public List<Diagnoses> getDiagnosesByPatientId(@PathParam(PATIENT_ID_HEADER) Integer patientId) {
        return null;
    }
	
	@POST
	@Path("/{"+ PATIENT_ID_HEADER + "}/diagnoses")
    public Diagnoses saveDiagnosisByPatientId(@PathParam(PATIENT_ID_HEADER) Integer patientId, Diagnoses diagnosis) {
		return null;
    }
	
	@GET
	@Path("/{"+ PATIENT_ID_HEADER + "}/diagnoses/{"+ DIAGNOSIS_ID_HEADER + "}")
    public Diagnoses getDiagnosisByIdAndPatientId(@PathParam(PATIENT_ID_HEADER) Integer patientId, @PathParam(DIAGNOSIS_ID_HEADER) String diagnosisId) {
        return null;
    }
	
	@PUT
	@Path("/{"+ PATIENT_ID_HEADER + "}/diagnoses/{"+ DIAGNOSIS_ID_HEADER + "}")
    public Diagnoses updateDiagnosisByIdAndPatientId(@PathParam(PATIENT_ID_HEADER) Integer patientId, @PathParam(DIAGNOSIS_ID_HEADER) String diagnosisId, Diagnoses diagnosis) {
		return null;
    }
}