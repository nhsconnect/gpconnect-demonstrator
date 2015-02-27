package net.nhs.esb.patient.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
@Embeddable
public class PracticeDetails {

    @Column(name = "gp_name")
    @JsonProperty("name")
    private String name;

    @Column(name = "gp_address_1")
    @JsonProperty("address1")
    private String address1;

    @Column(name = "gp_address_2")
    @JsonProperty("address2")
    private String address2;

    @Column(name = "gp_address_3")
    @JsonProperty("address3")
    private String address3;

    @Column(name = "gp_address_4")
    @JsonProperty("address4")
    private String address4;

    @Column(name = "gp_address_5")
    @JsonProperty("address5")
    private String address5;

    @Column(name = "gp_postcode")
    @JsonProperty("postCode")
    private String postcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
