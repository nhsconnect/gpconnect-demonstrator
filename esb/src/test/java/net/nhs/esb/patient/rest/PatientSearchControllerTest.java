package net.nhs.esb.patient.rest;

import static org.junit.Assert.assertNull;

import net.nhs.esb.patient.model.PatientDetailsArray;
import net.nhs.esb.patient.model.PatientSearchCriteria;
import org.junit.Before;
import org.junit.Test;

public class PatientSearchControllerTest {

    private PatientSearchController searchController;

    @Before
    public void setUp() throws Exception {
        searchController = new PatientSearchController();
    }

    @Test
    public void shouldAlwaysReturnNullPatientDetails() {
        PatientSearchCriteria patientSearchCriteria =  new PatientSearchCriteria();

        PatientDetailsArray patientDetails = searchController.getPatientDetailsBySearch(patientSearchCriteria);
        assertNull(patientDetails);
    }
}
