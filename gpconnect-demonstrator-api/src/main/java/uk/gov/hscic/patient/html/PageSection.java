package uk.gov.hscic.patient.html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PageSection {

    private final String header;
    private Date fromDate;
    private Date toDate;
    private final List<String> sectionBanners;
    private Table table;
    private String id; // html id
    private boolean isSingleTable = false; // #252

    /**
     * overload
     * @param header
     * @param id
     * @param table 
     */
    public PageSection(String header, String id, Table table) {
        this(header, id, table, null, null, false);
    }

    /**
     * overload
     * @param header
     * @param id
     * @param table
     * @param fromDate
     * @param toDate 
     */
    public PageSection(String header, String id, Table table, Date fromDate, Date toDate) {
        this(header, id, table, fromDate, toDate, false);
    }

    /**
     * 
     * @param header
     * @param id
     * @param table
     * @param fromDate
     * @param toDate
     * @param isSingleTable added for #252
     */
    public PageSection(String header, String id, Table table, Date fromDate, Date toDate, boolean isSingleTable) {
        this.header = header;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sectionBanners = new ArrayList<>();
        this.table = table;
        this.id = id;
        this.isSingleTable = isSingleTable;
    }

    public void addBanner(String bannerString) {
        sectionBanners.add(bannerString);
    }

    public String getHeader() {
        return header;
    }

    public List<String> getBanners() {
        return sectionBanners;
    }

    public Table getTable() {
        return table;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    /**
     * @return the html id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the isSingleTable
     */
    public boolean isSingleTable() {
        return isSingleTable;
    }

    /**
     * @param isSingleTable the isSingleTable to set
     */
    public void setIsSingleTable(boolean isSingleTable) {
        this.isSingleTable = isSingleTable;
    }
}
