package uk.gov.hscic.medications;

import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.medication.administration.model.MedicationAdministrationDetail;
import uk.gov.hscic.medication.administration.search.MedicationAdministrationSearch;
import uk.gov.hscic.medication.administration.search.MedicationAdministrationSearchFactory;

public class MedicationAdministrationResourceProvider  implements IResourceProvider {

    ApplicationContext applicationContext;

    public MedicationAdministrationResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<MedicationAdministration> getResourceType() {
        return MedicationAdministration.class;
    }

    @Search
    public List<MedicationAdministration> getMedicationAdministrationsForPatientId(@RequiredParam(name = "patientId") String patientId, @OptionalParam(name = "fromDate") String fromDate, @OptionalParam(name = "toDate") String toDate) {
        
        RepoSource sourceType = RepoSourceType.fromString(null);
        MedicationAdministrationSearch medicationAdministrationSearch = applicationContext.getBean(MedicationAdministrationSearchFactory.class).select(sourceType);
        ArrayList<MedicationAdministration> medicationAdministrations = new ArrayList();

        List<MedicationAdministrationDetail> medicationAdministrationDetailList = medicationAdministrationSearch.findMedicationAdministrationForPatient(Long.parseLong(patientId));
        if (medicationAdministrationDetailList != null && medicationAdministrationDetailList.size() > 0) {
            for(MedicationAdministrationDetail medicationAdministrationDetail : medicationAdministrationDetailList){
                MedicationAdministration medicationAdministration = new MedicationAdministration();
                medicationAdministration.setId(String.valueOf(medicationAdministrationDetail.getId()));
                medicationAdministration.setPatient(new ResourceReferenceDt("Patient/"+medicationAdministrationDetail.getPatientId()));
                medicationAdministration.setPractitioner(new ResourceReferenceDt("Practitioner/"+medicationAdministrationDetail.getPractitionerId()));
                medicationAdministration.setPrescription(new ResourceReferenceDt("MedicationOrder/"+medicationAdministrationDetail.getPrescriptionId()));
                medicationAdministration.setEffectiveTime(new DateTimeDt(medicationAdministrationDetail.getAdministrationDate()));
                medicationAdministration.setMedication(new ResourceReferenceDt("Medication/"+medicationAdministrationDetail.getMedicationId()));
                medicationAdministrations.add(medicationAdministration);
            }
        }
        
        return medicationAdministrations;
    }
}