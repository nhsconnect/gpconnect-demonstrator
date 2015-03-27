package net.nhs.esb.transfer.route.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.problem.model.Problem;
import net.nhs.esb.transfer.model.TransferDetail;
import net.nhs.esb.transfer.model.TransferOfCare;
import net.nhs.esb.transfer.model.TransferOfCareComposition;
import org.apache.camel.Converter;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class TransferCompositionConverter {

    private static final String TRANSFER_UID = "idcr_handover_summary_report/_uid";

    @Converter
    public TransferOfCareComposition convertResponseToTransferOfCareComposition(CompositionResponseData response) {

        Map<String, Object> rawComposition = response.getComposition();

        String compositionId = MapUtils.getString(rawComposition, TRANSFER_UID);

        List<TransferOfCare> transferOfCareList = new ArrayList<>();

        int transferOfCareCount = countDataEntries(rawComposition, "idcr_handover_summary_report/reason_for_contact:");
        for (int i = 0; i < transferOfCareCount; i++) {
            TransferOfCare transferOfCare = createTransferOfCare(rawComposition, i);
            transferOfCareList.add(transferOfCare);
        }

        TransferOfCareComposition transferOfCareComposition = new TransferOfCareComposition();
        transferOfCareComposition.setCompositionId(compositionId);
        transferOfCareComposition.setTransfers(transferOfCareList);

        return transferOfCareComposition;
    }


    protected <T> List<T> extractCompositionData(Map<String,Object> rawComposition, String dataDefinitionPrefix, AbstractFactory<T> factory) {

        factory.setRawComposition(rawComposition);

        List<T> list = new ArrayList<>();

        int count = countDataEntries(rawComposition, dataDefinitionPrefix);
        for (int i = 0; i < count; i++) {
            factory.setPrefix(dataDefinitionPrefix + i);

            T t = factory.create();
            list.add(t);
        }

        return list;
    }

    private int countDataEntries(Map<String, Object> rawComposition, String prefix) {

        int maxEntry = -1;
        for (String key : rawComposition.keySet()) {
            if (StringUtils.startsWith(key, prefix)) {
                String index = StringUtils.substringAfter(key, prefix);
                index = StringUtils.substringBefore(index, "/");

                maxEntry = Math.max(maxEntry, Integer.parseInt(index));
            }
        }

        return maxEntry + 1;

    }

    private TransferOfCare createTransferOfCare(Map<String, Object> rawComposition, int index) {


        List<Allergy> allergyList = extractCompositionData(rawComposition, "idcr_handover_summary_report/allergies_and_adverse_reactions:" + index + "/adverse_reaction:", new AllergyFactory());
        List<Contact> contactList = extractCompositionData(rawComposition, "idcr_handover_summary_report/patient_information/relevant_contacts:" + index + "/relevant_contact:", new ContactFactory());
        List<Medication> medicationList = extractCompositionData(rawComposition, "idcr_handover_summary_report/medication_and_medical_devices:" + index + "/current_medication:0/medication_statement:", new MedicationFactory());
        List<Problem> problemList = extractCompositionData(rawComposition, "idcr_handover_summary_report/problems_and_issues:" + index + "/problem_diagnosis:", new ProblemFactory());

        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setReasonForContact(MapUtils.getString(rawComposition, "idcr_handover_summary_report/reason_for_contact:" + index + "/reason_for_contact/presenting_problem:0"));
        transferDetail.setClinicalSummary(MapUtils.getString(rawComposition, "idcr_handover_summary_report/clinical_summary:" + index + "/clinical_summary/summary"));

        TransferOfCare transferOfCare = new TransferOfCare();
        transferOfCare.setAllergies(allergyList);
        transferOfCare.setContacts(contactList);
        transferOfCare.setMedication(medicationList);
        transferOfCare.setProblems(problemList);
        transferOfCare.setTransferDetail(transferDetail);

        return transferOfCare;
    }

    private abstract class AbstractFactory<T> implements Factory<T> {

        protected Map<String,Object> rawComposition;
        protected String prefix;

        public void setRawComposition(Map<String, Object> rawComposition) {
            this.rawComposition = rawComposition;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    private class AllergyFactory extends AbstractFactory<Allergy> {

        @Override
        public Allergy create() {

            Allergy allergy = new Allergy();

            allergy.setCause(MapUtils.getString(rawComposition, prefix + "/causative_agent|value"));
            allergy.setCauseCode(MapUtils.getString(rawComposition, prefix + "/causative_agent|code"));
            allergy.setCauseTerminology(MapUtils.getString(rawComposition, prefix + "/causative_agent|terminology"));
            allergy.setReaction(MapUtils.getString(rawComposition, prefix + "/reaction_details/reaction|value"));

            return allergy;
        }
    }

    private class ContactFactory extends AbstractFactory<Contact> {

        @Override
        public Contact create() {

            Contact contact = new Contact();

            contact.setName(MapUtils.getString(rawComposition, prefix + "/personal_details/person_name/unstructured_name"));
            contact.setContactInformation(MapUtils.getString(rawComposition, prefix + "/personal_details/telecom_details/unstuctured_telcoms"));
            contact.setRelationshipCode(MapUtils.getString(rawComposition, prefix + "/relationship_category|code"));
            contact.setRelationshipType(MapUtils.getString(rawComposition, prefix + "/relationship_category|value"));
            contact.setRelationshipTerminology(MapUtils.getString(rawComposition, prefix + "/relationship_category|terminology"));
            contact.setRelationship(MapUtils.getString(rawComposition, prefix + "/relationship"));
            contact.setNextOfKin(MapUtils.getBoolean(rawComposition, prefix + "/is_next_of_kin"));
            contact.setNote(MapUtils.getString(rawComposition, prefix + "/note"));

            return contact;
        }
    }

    private class MedicationFactory extends AbstractFactory<Medication> {

        @Override
        public Medication create() {

            Medication medication = new Medication();
            medication.setName(MapUtils.getString(rawComposition, prefix + "/medication_item/medication_name|value"));
            medication.setCode(MapUtils.getString(rawComposition, prefix + "/medication_item/medication_name|code"));
            medication.setRoute(MapUtils.getString(rawComposition, prefix + "/medication_item/route:0|value"));
            medication.setTerminology(MapUtils.getString(rawComposition, prefix + "/medication_item/route:0|terminology"));
            medication.setDoseDirections(MapUtils.getString(rawComposition, prefix + "/medication_item/dose_directions_description"));
            medication.setDoseAmount(MapUtils.getString(rawComposition, prefix + "/medication_item/dose_amount_description"));
            medication.setDoseTiming(MapUtils.getString(rawComposition, prefix + "/medication_item/dose_timing_description"));
            medication.setStartDateTime(MapUtils.getString(rawComposition, prefix + "/medication_item/course_details/start_datetime"));

            return medication;
        }
    }

    private class ProblemFactory extends AbstractFactory<Problem> {

        @Override
        public Problem create() {
            Problem problem = new Problem();
            problem.setProblem(MapUtils.getString(rawComposition, prefix + "/problem_diagnosis"));
            problem.setDescription(MapUtils.getString(rawComposition, prefix + "/description"));
            problem.setDateOfOnset(MapUtils.getString(rawComposition, prefix + "/date_of_onset"));

            return problem;
        }
    }
}
