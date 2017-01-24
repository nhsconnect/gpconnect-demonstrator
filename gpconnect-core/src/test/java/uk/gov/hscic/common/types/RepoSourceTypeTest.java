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
package uk.gov.hscic.common.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class RepoSourceTypeTest {

    @Test
    public void validateTSESize() {
        assertEquals(6, RepoSourceType.values().length);
    }

    @Test
    public void verifyNONESourceName() {
        assertEquals("Not Configured", RepoSourceType.NONE.getSourceName());
    }

    @Test
    public void verifyACTIVEMQSourceName() {
        assertEquals("ActiveMQ", RepoSourceType.ACTIVEMQ.getSourceName());
    }

    @Test
    public void verifyLEGACYSourceName() {
        assertEquals("Legacy", RepoSourceType.LEGACY.getSourceName());
    }

    @Test
    public void verifyMARANDSourceName() {
        assertEquals("Marand", RepoSourceType.MARAND.getSourceName());
    }

    @Test
    public void verifyAUDITSourceName() {
        assertEquals("Audit", RepoSourceType.AUDIT.getSourceName());
    }

    @Test
    public void verifyTERMINOLOGYSourceName() {
        assertEquals("Terminology", RepoSourceType.TERMINOLOGY.getSourceName());
    }

    // Reverse Lookup Tests
    @Test
    public void reverseLookupOfTSEFromEmptyString() {
        assertNull(RepoSourceType.fromString(""));
    }

    @Test
    public void reverseLookupOfTSEFromNull() {
        assertNull(RepoSourceType.fromString(null));
    }

    @Test
    public void reverseLookupOfTSEFromCaseSensitivePopulatedString() {
        assertEquals(RepoSourceType.LEGACY, RepoSourceType.fromString("Legacy"));
    }

    @Test
    public void reverseLookupOfTSEFromLowercasePopulatedString() {
        assertEquals(RepoSourceType.LEGACY, RepoSourceType.fromString("legacy"));
    }

    @Test
    public void reverseLookupOfNONE() {
        assertEquals(RepoSourceType.NONE, RepoSourceType.fromString("Not Configured"));
    }
}