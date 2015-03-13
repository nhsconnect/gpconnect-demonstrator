package net.nhs.esb.transfer.route.converter;

import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.problem.model.ProblemComposition;
import net.nhs.esb.transfer.model.TransferOfCare;
import net.nhs.esb.transfer.model.TransferOfCareComposition;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class TransferCompositionAggregator {

    public TransferOfCareComposition aggregate(@Header("transfer") TransferOfCare transferOfCare,
                                               @Header("allergies") AllergyComposition allergies,
                                               @Header("contacts") ContactComposition contacts,
                                               @Header("medication") MedicationComposition medication,
                                               @Header("problems") ProblemComposition problems) {

        TransferOfCareComposition composition = new TransferOfCareComposition();
        composition.setTransferOfCare(transferOfCare);
        composition.setAllergies(allergies);
        composition.setContacts(contacts);
        composition.setMedication(medication);
        composition.setProblems(problems);

        return composition;
    }
}
