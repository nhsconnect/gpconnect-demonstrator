package uk.gov.hscic.medications;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestDispenseRequestComponent;
import org.hl7.fhir.dstu3.model.MedicationRequest.MedicationRequestStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.medication.orders.MedicationOrderSearch;
import uk.gov.hscic.model.medication.MedicationOrderDetails;

@Component
public class MedicationOrderResourceProvider implements IResourceProvider {

    @Autowired
    private MedicationOrderSearch medicationOrderSearch;

    @Override
    public Class<MedicationRequest> getResourceType() {
        return MedicationRequest.class;
    }

    @Search
    public List<MedicationRequest> getMedicationOrdersForPatientId(@RequiredParam(name = "patient") String patientId) {
        ArrayList<MedicationRequest> medicationOrders = new ArrayList<>();
        List<MedicationOrderDetails> medicationOrderDetailsList = medicationOrderSearch.findMedicationOrdersForPatient(Long.parseLong(patientId));

        if (medicationOrderDetailsList != null && !medicationOrderDetailsList.isEmpty()) {
            for (MedicationOrderDetails medicationOrderDetails : medicationOrderDetailsList) {
                medicationOrders.add(medicationOrderDetailsToMedicationOrderResourceConverter(medicationOrderDetails));
            }
        }

        return medicationOrders;
    }

    @Read()
    public MedicationRequest getMedicationOrderById(@IdParam IdType medicationOrderId) {
        MedicationOrderDetails medicationOrderDetails = medicationOrderSearch.findMedicationOrderByID(medicationOrderId.getIdPartAsLong());

        if (medicationOrderDetails == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics("No medicationOrder details found for ID: " + medicationOrderId.getIdPart());
            throw new InternalErrorException("No medicationOrder details found for ID: " + medicationOrderId.getIdPart(), operationalOutcome);
        }

        return medicationOrderDetailsToMedicationOrderResourceConverter(medicationOrderDetails);
    }


    private MedicationRequest medicationOrderDetailsToMedicationOrderResourceConverter(MedicationOrderDetails medicationOrderDetails) {
        MedicationRequest medicationOrder = new MedicationRequest();

        String resourceId = String.valueOf(medicationOrderDetails.getId());
        String versionId = String.valueOf(medicationOrderDetails.getLastUpdated().getTime());
        String resourceType = medicationOrder.getResourceType().toString();

        IdType id = new IdType(resourceType, resourceId, versionId);

        medicationOrder.setId(id);
        medicationOrder.getMeta().setVersionId(versionId);
        medicationOrder.getMeta().setLastUpdated(medicationOrderDetails.getLastUpdated());

        //medicationOrder.setDateWritten(new DateTimeDt(medicationOrderDetails.getDateWritten()));

        switch (medicationOrderDetails.getOrderStatus().toLowerCase(Locale.UK)) {
            case "active":
                medicationOrder.setStatus(MedicationRequestStatus.ACTIVE);
                break;
            case "completed":
                medicationOrder.setStatus(MedicationRequestStatus.COMPLETED);
                break;
            case "draft":
                medicationOrder.setStatus(MedicationRequestStatus.DRAFT);
                break;
            case "entered_in_error":
                medicationOrder.setStatus(MedicationRequestStatus.ENTEREDINERROR);
                break;
            case "on_hold":
                medicationOrder.setStatus(MedicationRequestStatus.ONHOLD);
                break;
            case "stopped":
                medicationOrder.setStatus(MedicationRequestStatus.STOPPED);
                break;
        }

        if (medicationOrderDetails.getPatientId() != null) {
            medicationOrder.setSubject(new Reference("Patient/"+medicationOrderDetails.getPatientId()));
        } else {
            medicationOrder.setSubject(new Reference());
        }

        medicationOrder.setRecorder(new Reference("Practitioner/"+medicationOrderDetails.getAutherId()));
        medicationOrder.setMedication(new Reference("Medication/"+medicationOrderDetails.getMedicationId()));
        medicationOrder.addDosageInstruction().setText(medicationOrderDetails.getDosageText());

        MedicationRequestDispenseRequestComponent dispenseRequest = new MedicationRequestDispenseRequestComponent();
        dispenseRequest.addExtension(new Extension(SystemURL.SD_EXTENSION_MEDICATION_QUANTITY_TEXT, new StringDt(medicationOrderDetails.getDispenseQuantityText())));
        dispenseRequest.addExtension(new Extension(SystemURL.SD_EXTENSION_PERSCRIPTION_REPEAT_REVIEW_DATE, new DateTimeDt(medicationOrderDetails.getDispenseReviewDate())));
        dispenseRequest.setId("Medication/"+medicationOrderDetails.getDispenseMedicationId());
        dispenseRequest.setNumberOfRepeatsAllowed(medicationOrderDetails.getDispenseRepeatsAllowed());
        medicationOrder.setDispenseRequest(dispenseRequest);

        return medicationOrder;
    }
}