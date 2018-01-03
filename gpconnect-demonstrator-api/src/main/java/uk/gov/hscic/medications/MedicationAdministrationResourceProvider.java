package uk.gov.hscic.medications;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.dstu3.model.IdType;
import uk.gov.hscic.medication.administration.MedicationAdministrationSearch;
import uk.gov.hscic.model.medication.MedicationAdministrationDetail;

@Component
public class MedicationAdministrationResourceProvider  implements IResourceProvider {

    @Autowired
    private MedicationAdministrationSearch medicationAdministrationSearch;

    @Override
    public Class<MedicationAdministration> getResourceType() {
        return MedicationAdministration.class;
    }

    @Search
    public List<MedicationAdministration> getMedicationAdministrationsForPatientId(@RequiredParam(name = "patient") String patientId) {
        ArrayList<MedicationAdministration> medicationAdministrations = new ArrayList<>();

        List<MedicationAdministrationDetail> medicationAdministrationDetailList = medicationAdministrationSearch.findMedicationAdministrationForPatient(Long.parseLong(patientId));

        if (medicationAdministrationDetailList != null && !medicationAdministrationDetailList.isEmpty()) {
            for(MedicationAdministrationDetail medicationAdministrationDetail : medicationAdministrationDetailList) {
                MedicationAdministration medicationAdministration = new MedicationAdministration();
                
                String resourceId = String.valueOf(medicationAdministrationDetail.getId());
                String versionId = String.valueOf(medicationAdministrationDetail.getLastUpdated().getTime());
                String resourceType = medicationAdministration.getResourceType().toString();

                IdType id = new IdType(resourceType, resourceId, versionId);

                medicationAdministration.setId(id);
                medicationAdministration.getMeta().setVersionId(versionId);
                medicationAdministration.getMeta().setLastUpdated(medicationAdministrationDetail.getLastUpdated());       

                medicationAdministration.addDefinition(new Reference("Patient/"+medicationAdministrationDetail.getPatientId()));
                medicationAdministration.addDefinition(new Reference("Practitioner/"+medicationAdministrationDetail.getPractitionerId()));
                medicationAdministration.setPrescription(new Reference("MedicationOrder/"+medicationAdministrationDetail.getPrescriptionId()));               
                medicationAdministration.setEffective( new DateType(medicationAdministrationDetail.getAdministrationDate()));   
                medicationAdministration.setMedication(new Reference("Medication/"+medicationAdministrationDetail.getMedicationId()));
                medicationAdministrations.add(medicationAdministration);
            }
        }

        return medicationAdministrations;
    }
}
