/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.patient.details.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hscic.common.exception.DataNotFoundException;
import uk.gov.hscic.medical.practicitioners.doctor.model.GPEntity;
import uk.gov.hscic.patient.allergies.search.AllergySearchFactory;
import uk.gov.hscic.patient.details.model.PatientEntity;
import uk.gov.hscic.patient.medication.search.MedicationSearch;
import uk.gov.hscic.patient.medication.search.MedicationSearchFactory;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.problems.search.ProblemSearchFactory;
import uk.gov.hscic.patient.summary.model.PatientDetails;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class PatientEntityToDetailsTransformerTest {

    private static final String PATIENT_ID = "PATIENT";

    @Mock
    private ProblemSearch mockProblemSearch;

    @Mock
    private ProblemSearchFactory mockProblemSearchFactory;

    private PatientEntityToDetailsTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new PatientEntityToDetailsTransformer();

        ReflectionTestUtils.setField(transformer, "problemSearchFactory", mockProblemSearchFactory);

        when(mockProblemSearchFactory.select(null)).thenReturn(mockProblemSearch);
    }

    @Test
    public void shouldRemoveEmptyLinesFromAddressString() {

        PatientEntity patientEntity = dummyPatientEntity();
        patientEntity.setAddress1("line 1");
        patientEntity.setAddress2(null);
        patientEntity.setAddress3("line 3");
        patientEntity.setAddress5("");
        patientEntity.setAddress5("line 5");
        patientEntity.setPostcode("postcode");

        PatientDetails patientDetails = transformer.transform(patientEntity);
        assertNotNull(patientDetails);

        assertEquals("line 1, line 3, postcode", patientDetails.getAddress());
    }

    @Test
    public void shouldReturnEmptyMedicationListWhenMedicationSearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
    }

    @Test
    public void shouldReturnEmptyProblemListWhenProblemSearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        when(mockProblemSearch.findProblemHeadlines(PATIENT_ID)).thenThrow(new DataNotFoundException("Expected test exception"));

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
    }

    private PatientEntity dummyPatientEntity() {
        PatientEntity patient = new PatientEntity();
        patient.setNhsNumber(PATIENT_ID);
        patient.setGp(new GPEntity());

        return patient;
    }
}
