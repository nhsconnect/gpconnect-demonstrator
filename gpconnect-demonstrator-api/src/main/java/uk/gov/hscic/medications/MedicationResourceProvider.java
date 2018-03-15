package uk.gov.hscic.medications;

import java.util.Date;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

@Component
public class MedicationResourceProvider implements IResourceProvider {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    public Class<Medication> getResourceType() {
        return Medication.class;
    }

    @Read()
    public Medication getMedicationById(@IdParam IdType medicationId) {
        MedicationEntity medicationEntity = medicationRepository.findOne(medicationId.getIdPartAsLong());

        if (medicationEntity == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics("No medication details found for ID: " + medicationId.getIdPart());
            throw new InternalErrorException("No medication details found for ID: " + medicationId.getIdPart(), operationalOutcome);
        }

        Medication medication = new Medication();

        String resourceId = String.valueOf(medicationEntity.getId());
        String versionId = String.valueOf(new Date());
        String resourceType = medication.getResourceType().toString();

        IdType id = new IdType(resourceType, resourceId, versionId);

        medication.setId(id);
        medication.getMeta().setVersionId(versionId);
                
        return medication;
    }
}
