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
package uk.gov.hscic.patient.summary.search;

import uk.gov.hscic.patient.summary.search.NotConfiguredPatientSearch;
import org.junit.Before;
import org.junit.Test;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

import static org.junit.Assert.assertEquals;

public class NotConfiguredPatientSearchTest {

    private NotConfiguredPatientSearch patientSearch;

    @Before
    public void setUp() throws Exception {
        patientSearch = new NotConfiguredPatientSearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals(RepoSourceType.NONE, patientSearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllPatients() {
        patientSearch.findAllPatients();
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindPatient() {
        patientSearch.findPatient(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAPatientSummary() {
        patientSearch.findPatientSummary(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindPatientsByQueryObject() {
        patientSearch.findPatientsByQueryObject(null);
    }
}
