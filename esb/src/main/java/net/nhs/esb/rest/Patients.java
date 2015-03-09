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
import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.patient.model.PatientDetails;
import net.nhs.esb.patient.model.PatientDetailsArray;
import org.springframework.stereotype.Controller;

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Patients {

    public static final String PATIENT_ID_HEADER = "patientId";
    public static final String DIAGNOSIS_ID_HEADER = "diagnosisId";

    @GET
    @Path("/{" + PATIENT_ID_HEADER + "}/diagnoses")
    public List<Diagnoses> getDiagnosesByPatientId(@PathParam(PATIENT_ID_HEADER) Long patientId) {
        return null;
    }

    @POST
    @Path("/{" + PATIENT_ID_HEADER + "}/diagnoses")
    public Diagnoses saveDiagnosisByPatientId(@PathParam(PATIENT_ID_HEADER) Long patientId, Diagnoses diagnosis) {
        return null;
    }

    @GET
    @Path("/{" + PATIENT_ID_HEADER + "}/diagnoses/{" + DIAGNOSIS_ID_HEADER + "}")
    public Diagnoses getDiagnosisByIdAndPatientId(@PathParam(PATIENT_ID_HEADER) Long patientId, @PathParam(DIAGNOSIS_ID_HEADER) String diagnosisId) {
        return null;
    }

    @PUT
    @Path("/{" + PATIENT_ID_HEADER + "}/diagnoses/{" + DIAGNOSIS_ID_HEADER + "}")
    public Diagnoses updateDiagnosisByIdAndPatientId(@PathParam(PATIENT_ID_HEADER) Long patientId, @PathParam(DIAGNOSIS_ID_HEADER) String diagnosisId, Diagnoses diagnosis) {
        return null;
    }

    @GET
    public PatientDetailsArray findAllPatients() {
        return null;
    }

    @PUT
    public void createPatient(PatientDetails patientDetails) {
    }

    @GET
    @Path("/{id}")
    public PatientDetails findPatientById(@PathParam("id") Long id) {
        return null;
    }

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

    @GET
    @Path("/{patientId}/contacts")
    public ContactComposition findPatientContactComposition(@PathParam("patientId") Long patientId) {
        return null;
    }

    @PUT
    @Path("/{patientId}/contacts")
    public void createContact(@PathParam("patientId") Long patientId, ContactComposition contactComposition) {

    }

    @GET
    @Path("/{patientId}/medications")
    public MedicationComposition findPatientMedicationComposition(@PathParam("patientId") Long patientId) {
        return null;
    }

    @PUT
    @Path("/{patientId}/medications")
    public void createMedication(@PathParam("patientId") Long patientId, MedicationComposition medicationComposition) {

    }
}
