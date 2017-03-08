package uk.gov.hscic.patient.careRecordHtml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PageSection {
    private final String header;
    private Date fromDate;
    private Date toDate;
    private final List<String> banners;
    private PageSectionHtmlTable table;

    public PageSection(String sectionHeader) {
        header = sectionHeader;
        banners = new ArrayList<>();
    }

    public void setTable(PageSectionHtmlTable sectionTable) {
        table = sectionTable;
    }

    public void adddBanner(String bannerString) {
        banners.add(bannerString);
    }

    public String getHeader() {
        return header;
    }

    public List<String> getBanners() {
        return banners;
    }

    public PageSectionHtmlTable getTable() {
        return table;
    }

    public void setDateRange(Date from, Date to) {
        fromDate = from;
        toDate = to;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }
}
