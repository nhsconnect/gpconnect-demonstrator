package uk.gov.hscic.medications;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.valueset.MedicationOrderStatusEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.order.model.MedicationOrderDetails;
import uk.gov.hscic.medication.order.search.MedicationOrderSearch;
import uk.gov.hscic.medication.order.search.MedicationOrderSearchFactory;

public class MedicationOrderResourceProvider implements IResourceProvider {

    ApplicationContext applicationContext;

    public MedicationOrderResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<MedicationOrder> getResourceType() {
        return MedicationOrder.class;
    }

    @Search
    public List<MedicationOrder> getMedicationOrdersForPatientId(@RequiredParam(name = "patientId") String patientId, @OptionalParam(name = "fromDate") String fromDate, @OptionalParam(name = "toDate") String toDate) {
        RepoSource sourceType = RepoSourceType.fromString(null);
        MedicationOrderSearch medicationOrderSearch = applicationContext.getBean(MedicationOrderSearchFactory.class).select(sourceType);
        ArrayList<MedicationOrder> medicationOrders = new ArrayList();

        List<MedicationOrderDetails> medicationOrderDetailsList = medicationOrderSearch.findMedicationOrdersForPatient(Long.parseLong(patientId));
        if (medicationOrderDetailsList != null && medicationOrderDetailsList.size() > 0) {
            for(MedicationOrderDetails medicationOrderDetails : medicationOrderDetailsList){
                MedicationOrder medicationOrder = new MedicationOrder();
                medicationOrder.setId(String.valueOf(medicationOrderDetails.getId()));
                medicationOrder.setDateWritten(new DateTimeDt(medicationOrderDetails.getDateWritten()));
                
                switch(medicationOrderDetails.getOrderStatus().toLowerCase()){
                    case "active" : medicationOrder.setStatus(MedicationOrderStatusEnum.ACTIVE); break;
                    case "completed" : medicationOrder.setStatus(MedicationOrderStatusEnum.COMPLETED); break;
                    case "draft" : medicationOrder.setStatus(MedicationOrderStatusEnum.DRAFT); break;
                    case "entered_in_error" : medicationOrder.setStatus(MedicationOrderStatusEnum.ENTERED_IN_ERROR); break;
                    case "on_hold" : medicationOrder.setStatus(MedicationOrderStatusEnum.ON_HOLD); break;
                    case "stopped" : medicationOrder.setStatus(MedicationOrderStatusEnum.STOPPED); break;
                }
                
                medicationOrder.setPatient(new ResourceReferenceDt("Patient/"+patientId));
                medicationOrder.setPrescriber(new ResourceReferenceDt("Practitioner/"+medicationOrderDetails.getAutherId()));
                medicationOrder.setMedication(new ResourceReferenceDt("Medication/"+medicationOrderDetails.getMedicationId()));
                medicationOrder.addDosageInstruction().setText(medicationOrderDetails.getDosageText());
                
                MedicationOrder.DispenseRequest dispenseRequest = new MedicationOrder.DispenseRequest();
                dispenseRequest.addUndeclaredExtension(new ExtensionDt(true, "http://fhir.nhs.net/StructureDefinition/extension-medication-quantity-text-1-0", new StringDt(medicationOrderDetails.getDispenseQuantityText())));
                dispenseRequest.addUndeclaredExtension(new ExtensionDt(true, "http://fhir.nhs.net/StructureDefinition/extension-prescription-repeat-review-date-1-0", new DateTimeDt(medicationOrderDetails.getDispenseReviewDate())));
                dispenseRequest.setMedication(new ResourceReferenceDt("Medication/"+medicationOrderDetails.getDispenseMedicationId()));
                dispenseRequest.setNumberOfRepeatsAllowed(medicationOrderDetails.getDispenseRepeatsAllowed());
                medicationOrder.setDispenseRequest(dispenseRequest);
                
                medicationOrders.add(medicationOrder);
            }
        }
        
        return medicationOrders;
    }
}
