package net.nhs.esb.transfer.route.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.problem.model.Problem;
import net.nhs.esb.transfer.model.TransferDetail;
import net.nhs.esb.transfer.model.TransferOfCare;
import net.nhs.esb.transfer.model.TransferOfCareComposition;
import net.nhs.esb.transfer.model.TransferOfCareUpdate;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class TransferUpdateConverter {

    @Converter
    public TransferOfCareUpdate convertCompositionToTransferUpdate(TransferOfCareComposition composition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        for (TransferOfCare transferOfCare : composition.getTransfers()) {
            addTransferSummary(content, index, transferOfCare.getTransferDetail());
            addAllergies(content, index, transferOfCare.getAllergies());
            addContacts(content, index, transferOfCare.getContacts());
            addMedication(content, index, transferOfCare.getMedication());
            addProblems(content, index, transferOfCare.getProblems());
        }

        return new TransferOfCareUpdate(content);
    }

    private void addTransferSummary(Map<String, String> content, int index, TransferDetail transferDetail) {
        content.put("idcr_handover_summary_report/clinical_summary:" + index + "/clinical_summary/summary", transferDetail.getClinicalSummary());
        content.put("idcr_handover_summary_report/reason_for_contact:" + index + "/reason_for_contact/presenting_problem:0", transferDetail.getReasonForContact());
    }

    private void addAllergies(Map<String, String> content, int transferIndex, List<Allergy> allergyList) {

        int index = 0;
        for (Allergy allergy : allergyList) {

            String prefix = "idcr_handover_summary_report/allergies_and_adverse_reactions:" + transferIndex + "/adverse_reaction:" + index;

            content.put(prefix + "/causative_agent|value", allergy.getCause());
            content.put(prefix + "/causative_agent|code", allergy.getCauseCode());
            content.put(prefix + "/reaction_details/reaction|value", allergy.getReaction());
            content.put(prefix + "/reaction_details/reaction|code", "unknown");

            index++;
        }
    }

    private void addContacts(Map<String, String> content, int transferIndex,  List<Contact> contactList) {

        int index = 0;
        for (Contact contact : contactList) {

            String prefix = "idcr_handover_summary_report/patient_information/relevant_contacts:" + transferIndex + "/relevant_contact:" + index;

            String nextOfKin = contact.getNextOfKin() == null ? null : contact.getNextOfKin().toString();

            content.put(prefix + "/personal_details/person_name/unstructured_name", contact.getName());
            content.put(prefix + "/personal_details/telecom_details/unstuctured_telcoms", contact.getContactInformation());
            content.put(prefix + "/relationship_category|code", contact.getRelationshipCode());
            content.put(prefix + "/relationship_category|value", contact.getRelationshipType());
            content.put(prefix + "/relationship", contact.getRelationship());
            content.put(prefix + "/is_next_of_kin", nextOfKin);
            content.put(prefix + "/note", contact.getNote());

            index++;
        }
    }

    private void addMedication(Map<String, String> content, int transferIndex, List<Medication> medicationList) {

        int index = 0;
        for (Medication medication : medicationList) {

            String prefix = "idcr_handover_summary_report/medication_and_medical_devices:" + transferIndex + "/current_medication:0/medication_statement:" + index;

            content.put(prefix + "/medication_item/medication_name|value", medication.getName());
            content.put(prefix + "/medication_item/medication_name|code", medication.getCode());
            content.put(prefix + "/medication_item/route:0|value", medication.getRoute());
            content.put(prefix + "/medication_item/route:0|code", "unknown");
            content.put(prefix + "/medication_item/dose_directions_description", medication.getDoseDirections());
            content.put(prefix + "/medication_item/dose_amount_description", medication.getDoseAmount());
            content.put(prefix + "/medication_item/dose_timing_description", medication.getDoseTiming());
            content.put(prefix + "/medication_item/course_details/start_datetime", medication.getStartDateTime());

            index++;
        }
    }

    private void addProblems(Map<String, String> content, int transferIndex, List<Problem> problemList) {

        int index = 0;
        for (Problem problem : problemList) {

            String prefix = "idcr_handover_summary_report/problems_and_issues:" + transferIndex + "/problem_diagnosis:" + index;

            content.put(prefix + "/problem_diagnosis", problem.getProblem());
            content.put(prefix + "/description", problem.getDescription());
            content.put(prefix + "/date_of_onset", problem.getDateOfOnset());

            index++;
        }
    }
}
