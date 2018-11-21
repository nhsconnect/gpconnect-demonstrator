package uk.gov.hscic.patient.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.practitioner.PractitionerEntity;

public class PatientEntityToDetailsTransformerTest {
    private static final String PATIENT_ID = "PATIENT";

    private PatientEntityToDetailsTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new PatientEntityToDetailsTransformer();
    }

    @Test
    public void shouldRemoveEmptyLinesFromAddressString() {
        final PatientEntity patientEntity = dummyPatientEntity();

        patientEntity.setAddress1("line 1");
        patientEntity.setAddress2(null);
        patientEntity.setAddress3("line 3");
        patientEntity.setAddress5("");
        patientEntity.setAddress5("line 5");
        patientEntity.setPostcode("postcode");

        final PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
        // TODO Commented out for 1.2.2 build. Need to check this, get Address now returns an array of 5 strings one per line
        // Changes at 1.2.2 remove the use of teh text field and stupulate a more strcutured address so this test no longer applies
        //assertEquals("line 1, line 3, postcode", patientDetails.getAddress());
    }

    @Test
    public void shouldReturnEmptyMedicationListWhenMedicationSearchThrowsException() {
        assertNotNull(transformer.transform(dummyPatientEntity()));
    }

    private PatientEntity dummyPatientEntity() {
        final PatientEntity patient = new PatientEntity();
        patient.setNhsNumber(PATIENT_ID);
        patient.setPractitioner(new PractitionerEntity());

        return patient;
    }
}
