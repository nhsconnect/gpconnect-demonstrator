package uk.gov.hscic.model.appointment;

import java.util.Date;

public class BookingOrgDetail {
    private Long id;
    private String orgCode;
    private String name;
    private String telephone;
    private Date lastUpdated;
    private AppointmentDetail appointmentDetail;
    
    public AppointmentDetail getAppointmentDetail() {
        return appointmentDetail;
    }

    public void setAppointmentDetail(AppointmentDetail appointmentDetail) {
        this.appointmentDetail = appointmentDetail;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
