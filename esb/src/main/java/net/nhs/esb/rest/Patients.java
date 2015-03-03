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
import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.allergy.model.AllergyArray;
import net.nhs.esb.clinicalsynopsis.model.ClinicalSynopsis;
import net.nhs.esb.clinicalsynopsis.model.ClinicalSynopsisArray;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactArray;
import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.medication.model.MedicationArray;
import net.nhs.esb.patient.model.PatientDetails;
import net.nhs.esb.patient.model.PatientDetailsArray;
import net.nhs.esb.transfer.model.TransferOfCareArray;
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
    public AllergyArray findAllergiesByPatientId(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/allergies")
    public AllergyArray findAllergiesByPatientIdWithPost(@PathParam("patientId") Long patientId) {
        return null;
    }

    @GET
    @Path("/{patientId}/allergies/{id}")
    public Allergy findAllergyById(@PathParam("patientId") Long patientId, @PathParam("id") Long id) {
        return null;
    }

    @PUT
    @Path("/{patientId}/allergies/{id}")
    public void createAllergy(@PathParam("patientId") Long patientId, @PathParam("id") Long id, Allergy allergy) {

    }

    @GET
    @Path("/{patientId}/contacts")
    public ContactArray findContactsByPatientId(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/contacts")
    public ContactArray findContactsByPatientIdWithPost(@PathParam("patientId") Long patientId) {
        return null;
    }

    @GET
    @Path("/{patientId}/contacts/{id}")
    public Contact findContactById(@PathParam("patientId") Long patientId, @PathParam("id") Long id) {
        return null;
    }

    @PUT
    @Path("/{patientId}/contacts/{id}")
    public void createContact(@PathParam("patientId") Long patientId, @PathParam("id") Long id, Contact contact) {

    }

    @GET
    @Path("/{patientId}/medications")
    public MedicationArray findMedicationsByPatientId(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/medications")
    public MedicationArray findMedicationsByPatientIdWithPost(@PathParam("patientId") Long patientId) {
        return null;
    }

    @GET
    @Path("/{patientId}/medications/{id}")
    public Medication findMedicationById(@PathParam("patientId") Long patientId, @PathParam("id") Long id) {
        return null;
    }

    @PUT
    @Path("/{patientId}/medications/{id}")
    public void createMedication(@PathParam("patientId") Long patientId, @PathParam("id") Long id, Medication medication) {

    }

    @GET
    @Path("/{patientId}/clinical-synopses")
    public ClinicalSynopsisArray findClinicalSynopsesByPatientId(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/clinical-synopses")
    public ClinicalSynopsisArray findClinicalSynopsesByPatientIdWithPost(@PathParam("patientId") Long patientId) {
        return null;
    }

    @GET
    @Path("/{patientId}/clinical-synopses/{id}")
    public ClinicalSynopsis findClinicalSynopsisById(@PathParam("patientId") Long patientId, @PathParam("id") Long id) {
        return null;
    }

    @PUT
    @Path("/{patientId}/clinical-synopses/{id}")
    public void createClinicalSynopsis(@PathParam("patientId") Long patientId, @PathParam("id") Long id, ClinicalSynopsis clinicalSynopsis) {

    }

    @GET
    @Path("/{patientId}/transfer-of-care")
    public TransferOfCareArray findTransfersOfCareByPatientId(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    @Path("/{patientId}/transfer-of-care")
    public TransferOfCareArray findTransfersOfCareByPatientIdWithPost(@PathParam("patientId") Long patientId) {
        return null;
    }
}
