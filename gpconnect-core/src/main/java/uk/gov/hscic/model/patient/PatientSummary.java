package uk.gov.hscic.model.patient;

import java.util.Date;

public class PatientSummary {
    private String address;
    private Date dateOfBirth;
    private Date deceased;
    private String department;
    private String forename;
    private String gender;
    private String id;
    private Date lastUpdated;
    private String name;
    private String nhsNumber;
    private boolean sensitive;
    private String surname;
    private String title;

    public String getAddress() {
        return address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDeceased() {
        return deceased;
    }

    public String getDepartment() {
        return department;
    }

    public String getForename() {
        return forename;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getName() {
        return name;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public String getSurname() {
        return surname;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDeceased(Date deceased) {
        this.deceased = deceased;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
