package net.nhs.esb.procedures.model;

/**
 */
public class Procedure {

    private Integer id;
    private String procedureName;
    private String procedureNotes;
    private String author;
    private String terminology;
    private String code;
    private String dateOfProcedure;
    private String timeOfProcedure;
    private String dateSubmitted;
    private String source;
    private String performer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureNotes() {
        return procedureNotes;
    }

    public void setProcedureNotes(String procedureNotes) {
        this.procedureNotes = procedureNotes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTerminology() {
        return terminology;
    }

    public void setTerminology(String terminology) {
        this.terminology = terminology;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateOfProcedure() {
        return dateOfProcedure;
    }

    public void setDateOfProcedure(String dateOfProcedure) {
        this.dateOfProcedure = dateOfProcedure;
    }

    public String getTimeOfProcedure() {
        return timeOfProcedure;
    }

    public void setTimeOfProcedure(String timeOfProcedure) {
        this.timeOfProcedure = timeOfProcedure;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }
}
