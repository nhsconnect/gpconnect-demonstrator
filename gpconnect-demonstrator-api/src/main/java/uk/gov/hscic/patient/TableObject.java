package uk.gov.hscic.patient;

import java.util.List;

public class TableObject {
    private final List<String> headers;
    private final List<List<Object>> rows;
    private final String title;
    
    public TableObject(List<String> headers, List<List<Object>> rows, String title) {
        this.headers = headers;
        this.rows = rows;
        this.title = title;
    }
    
    public List<String> getHeaders() {
        return headers;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }

}
  
