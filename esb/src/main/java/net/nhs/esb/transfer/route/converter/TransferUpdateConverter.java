package net.nhs.esb.transfer.route.converter;

import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.problem.model.ProblemComposition;
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

        addTransferSummary(content, composition.getTransferOfCare());
        addAllergies(content, composition.getAllergies());
        addContacts(content, composition.getContacts());
        addMedication(content, composition.getMedication());
        addProblems(content, composition.getProblems());

        return new TransferOfCareUpdate(content);
    }

    private void addTransferSummary(Map<String, String> content, TransferOfCare transferOfCare) {
        content.put("idcr_handover_summary_report/clinical_summary:0/clinical_summary/summary", transferOfCare.getClinicalSummary());
        content.put("idcr_handover_summary_report/reason_for_contact:0/reason_for_contact/presenting_problem:0", transferOfCare.getReasonForContact());
    }

    private void addAllergies(Map<String, String> content, AllergyComposition allergyComposition) {

        int index = 0;
        for (Allergy allergy : allergyComposition.getAllergies()) {

            String prefix = "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:" + index;

//            content.put(prefix + "", )

            index++;
        }
    }

    private void addContacts(Map<String, String> content, ContactComposition contactComposition) {

        int index = 0;
        for (Contact contact : contactComposition.getContacts()) {

            String prefix = "idcr_handover_summary_report/patient_information/relevant_contacts:0/relevant_contact:" + index;

            String nextOfKin = contact.getNextOfKin() == null ? null : contact.getNextOfKin().toString();

            content.put(prefix + "/personal_details/person_name/unstructured_name", contact.getName());
            content.put(prefix + "/personal_details/telecom_details/unstuctured_telcoms", contact.getContactInformation());
            content.put(prefix + "/relationship_category|code", contact.getRelationshipCode());
            content.put(prefix + "/relationship", contact.getRelationship());
            content.put(prefix + "/is_next_of_kin", nextOfKin);
            content.put(prefix + "/note", contact.getNote());

            index++;
        }
    }

    private void addMedication(Map<String, String> content, MedicationComposition medicationComposition) {

    }

    private void addProblems(Map<String, String> content, ProblemComposition problemComposition) {

    }

    /*{
       "idcr_handover_summary_report/reason_for_contact:0/reason_for_contact/presenting_problem:0": "Presenting Problem 42",
       "idcr_handover_summary_report/problems_and_issues:0/exclusion_of_a_problem_diagnosis:0/exclusion_statement:0": "Exclusion Statement 32",
       "idcr_handover_summary_report/problems_and_issues:0/exclusion_of_a_problem_diagnosis:0/date_last_updated": "2015-03-13T11:08:45.210Z",
       "idcr_handover_summary_report/problems_and_issues:0/problem_diagnosis:0/problem_diagnosis": "Problem/Diagnosis 35",
       "idcr_handover_summary_report/problems_and_issues:0/problem_diagnosis:0/description": "Description 65",
       "idcr_handover_summary_report/problems_and_issues:0/problem_diagnosis:0/date_of_onset": "2015-03-13T11:08:45.210Z",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/medication_name": "Medication name 38",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/route:0|code": "S.26",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/route:0|value": "S.26 description",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/dose_directions_description": "Dose directions description 52",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/dose_amount_description": "Dose amount description 18",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/dose_timing_description": "Dose timing description 22",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/course_details/start_datetime": "2015-03-13T11:08:45.210Z",
       "idcr_handover_summary_report/medication_and_medical_devices:0/current_medication:0/medication_statement:0/medication_item/course_details/indication": "Indication 72",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/causative_agent|code": "V.29",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/causative_agent|value": "V.29 description",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/reaction_details/reaction|code": "F.71",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/reaction_details/reaction|value": "F.71 description",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/reaction_details/severity|code": "at0011",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/reaction_details/certainty|code": "at0020",
       "idcr_handover_summary_report/allergies_and_adverse_reactions:0/adverse_reaction:0/reaction_details/comment": "Comment 76",
       "idcr_handover_summary_report/clinical_summary:0/clinical_summary/summary": "Summary 6"
    }
    */
}
