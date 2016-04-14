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
package org.rippleosi.common.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 */
public class RepoSourceTypeTest {

    //
    @Test
    public void validateTSESize() {
        assertEquals(9, RepoSourceType.values().length);
    }

    @Test
    public void verifyNONESourceName() {
        final String repoSourceName = RepoSourceType.NONE.getSourceName();
        assertEquals("Not Configured", repoSourceName);
    }

    @Test
    public void verifyACTIVEMQSourceName() {
        final String repoSourceName = RepoSourceType.ACTIVEMQ.getSourceName();
        assertEquals("ActiveMQ", repoSourceName);
    }

    @Test
    public void verifyLEGACYSourceName() {
        final String repoSourceName = RepoSourceType.LEGACY.getSourceName();
        assertEquals("Legacy", repoSourceName);
    }

    @Test
    public void verifyMARANDSourceName() {
        final String repoSourceName = RepoSourceType.MARAND.getSourceName();
        assertEquals("Marand", repoSourceName);
    }

    @Test
    public void verifyETHERCISSourceName() {
        final String repoSourceName = RepoSourceType.ETHERCIS.getSourceName();
        assertEquals("EtherCIS", repoSourceName);
    }

    @Test
    public void verifyORTHANCSourceName() {
        final String repoSourceName = RepoSourceType.ORTHANC.getSourceName();
        assertEquals("Orthanc", repoSourceName);
    }

    @Test
    public void verifyVISTASourceName() {
        final String repoSourceName = RepoSourceType.VISTA.getSourceName();
        assertEquals("Vista", repoSourceName);
    }

    @Test
    public void verifyAUDITSourceName() {
        final String repoSourceName = RepoSourceType.AUDIT.getSourceName();
        assertEquals("Audit", repoSourceName);
    }

    @Test
    public void verifyTERMINOLOGYSourceName() {
        final String repoSourceName = RepoSourceType.TERMINOLOGY.getSourceName();
        assertEquals("Terminology", repoSourceName);
    }

    // Reverse Lookup Tests
    @Test
    public void reverseLookupOfTSEFromEmptyString() {
        final String tseAsString = "";
        RepoSource sourceType = RepoSourceType.fromString(tseAsString);
        assertNull(sourceType);
    }

    @Test
    public void reverseLookupOfTSEFromNull() {
        RepoSource sourceType = RepoSourceType.fromString(null);
        assertNull(sourceType);
    }

    @Test
    public void reverseLookupOfTSEFromCaseSensitivePopulatedString() {
        final String tseAsString = "EtherCIS";
        RepoSource sourceType = RepoSourceType.fromString(tseAsString);
        assertEquals(RepoSourceType.ETHERCIS, sourceType);
    }

    @Test
    public void reverseLookupOfTSEFromLowercasePopulatedString() {
        final String tseAsString = "ethercis";
        RepoSource sourceType = RepoSourceType.fromString(tseAsString);
        assertEquals(RepoSourceType.ETHERCIS, sourceType);
    }

    @Test
    public void reverseLookupOfNONE() {
        final String tseAsString = "Not Configured";
        RepoSource sourceType = RepoSourceType.fromString(tseAsString);
        assertEquals(RepoSourceType.NONE, sourceType);
    }

}
