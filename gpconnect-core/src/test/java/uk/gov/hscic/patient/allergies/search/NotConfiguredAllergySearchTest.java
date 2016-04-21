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
package uk.gov.hscic.patient.allergies.search;

import uk.gov.hscic.patient.allergies.search.NotConfiguredAllergySearch;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;

/**
 */
public class NotConfiguredAllergySearchTest {

    private NotConfiguredAllergySearch allergySearch;

    @Before
    public void setUp() throws Exception {
        allergySearch = new NotConfiguredAllergySearch();
    }

    @Test
    public void shouldReportAsNotConfiguredImplementation() {
        assertEquals(RepoSourceType.NONE, allergySearch.getSource());
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllergyHeadlines() {
        allergySearch.findAllergyHeadlines(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllAllergies() {
        allergySearch.findAllAllergies(null);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExceptionWhenTryingToFindAllergyDetails() {
        allergySearch.findAllergy(null, null);
    }
}
