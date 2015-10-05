package org.rippleosi.search.reports.table.model;

import java.util.Date;

public class RecordHeadline {

    private String id;
    private Date latestEntry;
    private int totalEntries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
