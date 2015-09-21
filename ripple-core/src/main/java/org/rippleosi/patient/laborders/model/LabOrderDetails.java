package org.rippleosi.patient.laborders.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class LabOrderDetails implements Serializable {

    private String sourceId;
    private String source;
    private String name;
    private String code;
    private String terminology;
    private Date orderDate;
    private String author;
    private Date dateCreated;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTerminology() {
        return terminology;
    }

    public void setTerminology(String terminology) {
        this.terminology = terminology;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
