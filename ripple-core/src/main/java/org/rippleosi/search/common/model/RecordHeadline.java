package org.rippleosi.search.common.model;

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
