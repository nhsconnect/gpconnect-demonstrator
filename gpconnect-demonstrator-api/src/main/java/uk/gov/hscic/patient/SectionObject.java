package uk.gov.hscic.patient;

import java.util.Date;
import java.util.List;

public class SectionObject {
    private String title;
    private List<String> warnings;
    private Date toDate;
    private Date fromDate;
    private TableObject tableObject;
    private String code;
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public TableObject getTableObject() {
        return tableObject;
    }

    public void setTableObject(TableObject tableObject) {
        this.tableObject = tableObject;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    

}
