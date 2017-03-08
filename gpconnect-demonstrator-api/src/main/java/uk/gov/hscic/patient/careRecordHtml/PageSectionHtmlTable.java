package uk.gov.hscic.patient.careRecordHtml;

import java.util.List;

public class PageSectionHtmlTable {
    private final List<String> headers;
    private final List<List<Object>> rows;

    public PageSectionHtmlTable(List<String> headers, List<List<Object>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<Object>> getRows() {
        return rows;
    }
}
