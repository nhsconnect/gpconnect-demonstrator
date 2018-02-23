package uk.gov.hscic.medications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationDispense;
import org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import uk.gov.hscic.medication.dispense.MedicationDispenseSearch;
import uk.gov.hscic.model.medication.MedicationDispenseDetail;

@Component
public class MedicationDispenseResourceProvider implements IResourceProvider {

    @Autowired
    private MedicationDispenseSearch medicationDispenseSearch;

    @Override
    public Class<MedicationDispense> getResourceType() {
        return MedicationDispense.class;
    }

    @Search
    public List<MedicationDispense> getMedicationDispensesForPatientId(
            @RequiredParam(name = "patient") String patientId) {
        ArrayList<MedicationDispense> medicationDispenses = new ArrayList<>();

        List<MedicationDispenseDetail> medicationDispenseDetailList = medicationDispenseSearch
                .findMedicationDispenseForPatient(Long.parseLong(patientId));

        if (medicationDispenseDetailList != null && !medicationDispenseDetailList.isEmpty()) {
            for (MedicationDispenseDetail medicationDispenseDetail : medicationDispenseDetailList) {
                MedicationDispense medicationDispense = new MedicationDispense();
                medicationDispense.setId(String.valueOf(medicationDispenseDetail.getId()));
                medicationDispense.getMeta().setLastUpdated(medicationDispenseDetail.getLastUpdated());
                medicationDispense.getMeta()
                        .setVersionId(String.valueOf(medicationDispenseDetail.getLastUpdated().getTime()));

                switch (medicationDispenseDetail.getStatus().toLowerCase(Locale.UK)) {
                case "completed":
                    medicationDispense.setStatus(MedicationDispenseStatus.COMPLETED);
                    break;
                case "entered_in_error":
                    medicationDispense.setStatus(MedicationDispenseStatus.ENTEREDINERROR);
                    break;
                case "in_progress":
                    medicationDispense.setStatus(MedicationDispenseStatus.INPROGRESS);
                    break;
                case "on_hold":
                    medicationDispense.setStatus(MedicationDispenseStatus.ONHOLD);
                    break;
                case "stopped":
                    medicationDispense.setStatus(MedicationDispenseStatus.STOPPED);
                    break;
                }

                medicationDispense.setSubject(new Reference("Patient/" + patientId));
                medicationDispense.setAuthorizingPrescription(Collections.singletonList(
                        new Reference("MedicationOrder/" + medicationDispenseDetail.getMedicationOrderId())));

                Medication medication = new Medication();
                Coding coding = new Coding();

                coding.setCode(String.valueOf(medicationDispenseDetail.getMedicationId()));
                coding.setDisplay(medicationDispenseDetail.getMedicationName());
                CodeableConcept codeableConcept = new CodeableConcept();
                codeableConcept.setCoding(Collections.singletonList(coding));
                medication.setCode(codeableConcept);

                medicationDispense.addDosageInstruction().setText(medicationDispenseDetail.getDosageText());
                medicationDispenses.add(medicationDispense);
            }
        }

        return medicationDispenses;
    }
}
