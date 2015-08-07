package net.nhs.esb.transfer.route.converter;

import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.problem.model.ProblemComposition;
import net.nhs.esb.transfer.model.TransferOfCareSummary;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class TransferSummaryAggregator {

    public TransferOfCareSummary aggregate(@Header("compositionId") String compositionId,
                                           @Header("allergies") AllergyComposition allergies,
                                           @Header("contacts") ContactComposition contacts,
                                           @Header("medication") MedicationComposition medication,
                                           @Header("problems") ProblemComposition problems) {

        TransferOfCareSummary summary = new TransferOfCareSummary();
        summary.setCompositionId(compositionId);
        summary.setAllergies(allergies);
        summary.setContacts(contacts);
        summary.setMedication(medication);
        summary.setProblems(problems);
        summary.setSource("local");

        return summary;
    }
}
