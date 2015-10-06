package org.rippleosi.search.reports.table.model;

import java.util.Date;

public class RecordHeadline {

    private String source;
    private String sourceId;
    private Date latestEntry;
    private int totalEntries;

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

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }
}
