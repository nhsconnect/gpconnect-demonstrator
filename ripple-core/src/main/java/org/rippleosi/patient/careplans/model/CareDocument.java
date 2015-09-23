package org.rippleosi.patient.careplans.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class CareDocument implements Serializable {

    private String name;
    private String type;
    private String author;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
