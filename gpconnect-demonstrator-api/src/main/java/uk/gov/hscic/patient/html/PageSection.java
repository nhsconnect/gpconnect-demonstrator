package uk.gov.hscic.patient.html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PageSection {
    private final String header;
    private Date fromDate;
    private Date toDate;
    private final List<String> banners;
    private Table table;
    private String id; // html id

    public PageSection(String header, String id, Table table) {
        this.header = header;
        this.table = table;
        this.id = id;
        banners = new ArrayList<>();
    }

    public PageSection(String header, String id, Table table, Date fromDate, Date toDate) {
        this(header, id, table);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public void addBanner(String bannerString) {
        banners.add(bannerString);
    }

    public String getHeader() {
        return header;
    }

    public List<String> getBanners() {
        return banners;
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
}
