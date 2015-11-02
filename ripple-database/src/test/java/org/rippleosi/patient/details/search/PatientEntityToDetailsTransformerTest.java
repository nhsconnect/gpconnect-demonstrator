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
package org.rippleosi.patient.details.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.medical.practicitioners.doctor.model.GPEntity;
import org.rippleosi.patient.allergies.search.AllergySearch;
import org.rippleosi.patient.allergies.search.AllergySearchFactory;
import org.rippleosi.patient.contacts.search.ContactSearch;
import org.rippleosi.patient.contacts.search.ContactSearchFactory;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.medication.search.MedicationSearch;
import org.rippleosi.patient.medication.search.MedicationSearchFactory;
import org.rippleosi.patient.problems.search.ProblemSearch;
import org.rippleosi.patient.problems.search.ProblemSearchFactory;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.transfers.search.TransferOfCareSearch;
import org.rippleosi.patient.transfers.search.TransferOfCareSearchFactory;
import org.springframework.test.util.ReflectionTestUtils;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class PatientEntityToDetailsTransformerTest {

    private static final String PATIENT_ID = "PATIENT";

    @Mock
    private AllergySearch mockAllergySearch;

    @Mock
    private ContactSearch mockContactSearch;

    @Mock
    private MedicationSearch mockMedicationSearch;

    @Mock
    private ProblemSearch mockProblemSearch;

    @Mock
    private TransferOfCareSearch mockTransferSearch;

    @Mock
    private AllergySearchFactory mockAllergySearchFactory;

    @Mock
    private ContactSearchFactory mockContactSearchFactory;

    @Mock
    private MedicationSearchFactory mockMedicationSearchFactory;

    @Mock
    private ProblemSearchFactory mockProblemSearchFactory;

    @Mock
    private TransferOfCareSearchFactory mockTransferSearchFactory;

    private PatientEntityToDetailsTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new PatientEntityToDetailsTransformer();

        ReflectionTestUtils.setField(transformer, "allergySearchFactory", mockAllergySearchFactory);
        ReflectionTestUtils.setField(transformer, "contactSearchFactory", mockContactSearchFactory);
        ReflectionTestUtils.setField(transformer, "problemSearchFactory", mockProblemSearchFactory);
        ReflectionTestUtils.setField(transformer, "medicationSearchFactory", mockMedicationSearchFactory);
        ReflectionTestUtils.setField(transformer, "transferOfCareSearchFactory", mockTransferSearchFactory);

        when(mockAllergySearchFactory.select(null)).thenReturn(mockAllergySearch);
        when(mockContactSearchFactory.select(null)).thenReturn(mockContactSearch);
        when(mockMedicationSearchFactory.select(null)).thenReturn(mockMedicationSearch);
        when(mockProblemSearchFactory.select(null)).thenReturn(mockProblemSearch);
        when(mockProblemSearchFactory.select("vista")).thenReturn(mockProblemSearch);
        when(mockTransferSearchFactory.select(null)).thenReturn(mockTransferSearch);
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
    public void shouldReturnEmptyAllergyListWhenAllergySearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        when(mockAllergySearch.findAllergyHeadlines(PATIENT_ID)).thenThrow(new DataNotFoundException("Expected test exception"));

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
        assertTrue(patientDetails.getAllergies().isEmpty());
    }

    @Test
    public void shouldReturnEmptyContactListWhenContactSearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        when(mockContactSearch.findContactHeadlines(PATIENT_ID)).thenThrow(new DataNotFoundException("Expected test exception"));

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
        assertTrue(patientDetails.getAllergies().isEmpty());
    }

    @Test
    public void shouldReturnEmptyMedicationListWhenMedicationSearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        when(mockMedicationSearch.findMedicationHeadlines(PATIENT_ID)).thenThrow(new DataNotFoundException("Expected test exception"));

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
        assertTrue(patientDetails.getAllergies().isEmpty());
    }

    @Test
    public void shouldReturnEmptyProblemListWhenProblemSearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        when(mockProblemSearch.findProblemHeadlines(PATIENT_ID)).thenThrow(new DataNotFoundException("Expected test exception"));

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
        assertTrue(patientDetails.getAllergies().isEmpty());
    }

    @Test
    public void shouldReturnEmptyTransferListWhenTransferSearchThrowsException() {

        PatientEntity patientEntity = dummyPatientEntity();

        when(mockTransferSearch.findAllTransfers(PATIENT_ID)).thenThrow(new DataNotFoundException("Expected test exception"));

        PatientDetails patientDetails = transformer.transform(patientEntity);

        assertNotNull(patientDetails);
        assertTrue(patientDetails.getAllergies().isEmpty());
    }

    private PatientEntity dummyPatientEntity() {
        PatientEntity patient = new PatientEntity();
        patient.setNhsNumber(PATIENT_ID);
        patient.setGp(new GPEntity());

        return patient;
    }
}
