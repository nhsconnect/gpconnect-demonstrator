package net.nhs.esb.patient.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
@Entity
@Table(name = "patients")
public class PatientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @Column(name = "first_name")
    @JsonProperty("firstname")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("lastname")
    private String lastName;

    @Column(name = "address_1")
    @JsonProperty("address1")
    private String address1;

    @Column(name = "address_2")
    @JsonProperty("address2")
    private String address2;

    @Column(name = "address_3")
    @JsonProperty("address3")
    private String address3;

    @Column(name = "address_4")
    @JsonProperty("address4")
    private String address4;

    @Column(name = "address_5")
    @JsonProperty("address5")
    private String address5;

    @JsonProperty("postCode")
    private String postcode;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("born")
    private Date born;

    @JsonProperty("gender")
    private String gender;

    @Column(name = "nhs_number")
    @JsonProperty("nhsNo")
    private String nhsNumber;

    @JsonProperty("department")
    private String department;

    @JsonProperty("gPPractice")
    @Embedded
    private PracticeDetails gpPractice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public PracticeDetails getGpPractice() {
        return gpPractice;
    }

    public void setGpPractice(PracticeDetails gpPractice) {
        this.gpPractice = gpPractice;
    }
}
