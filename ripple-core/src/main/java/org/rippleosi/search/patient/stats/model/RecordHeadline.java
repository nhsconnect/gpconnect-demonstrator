/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package org.rippleosi.search.patient.stats.model;

import java.util.Date;

public class RecordHeadline {

    private String source;
    private String sourceId;
    private Date latestEntry;
    private String totalEntries;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Date getLatestEntry() {
        return latestEntry;
    }

    public void setLatestEntry(Date latestEntry) {
        this.latestEntry = latestEntry;
    }

    public String getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(String totalEntries) {
        this.totalEntries = totalEntries;
    }
}
