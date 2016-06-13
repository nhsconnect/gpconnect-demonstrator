package uk.gov.hscic.patient.details.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.hscic.common.exception.DataNotFoundException;
import uk.gov.hscic.medical.practicitioners.doctor.model.GPEntity;
import uk.gov.hscic.patient.details.model.PatientEntity;
import uk.gov.hscic.medication.search.MedicationSearch;
import uk.gov.hscic.medication.search.MedicationSearchFactory;
import uk.gov.hscic.patient.problems.search.ProblemSearchFactory;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import org.springframework.test.util.ReflectionTestUtils;

/**
 */
@RunWith(MockitoJUnitRunner.class)
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
        assertEquals("line 1, line 3, postcode", patientDetails.getAddress());
    }

    @Test
    public void shouldReturnEmptyMedicationListWhenMedicationSearchThrowsException() {
        final PatientEntity patientEntity = dummyPatientEntity();

        final PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
    }

    private PatientEntity dummyPatientEntity() {
        final PatientEntity patient = new PatientEntity();
        patient.setNhsNumber(PATIENT_ID);
        patient.setGp(new GPEntity());

        return patient;
    }
}
