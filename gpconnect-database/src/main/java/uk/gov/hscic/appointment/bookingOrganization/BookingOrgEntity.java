package uk.gov.hscic.appointment.bookingOrganization;

import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import uk.gov.hscic.appointment.appointment.AppointmentEntity;

@Entity
@Table(name = "appointment_booking_orgz")
@Cacheable(false)
public class BookingOrgEntity {
    
    @Id
    @GeneratedValue(generator="myGenerator")  
    @GenericGenerator(name="myGenerator", strategy="foreign", parameters=@Parameter(value="appointmentEntity", name = "property"))
    private Long id;
    
    @OneToOne(cascade=CascadeType.ALL)  
    @JoinColumn(name="id")  
    private AppointmentEntity appointmentEntity;
  
    @Column(name = "org_code")
    private String orgCode;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "telephone")
    private String telephone;
      
    @Column(name = "lastUpdated")
    private Date lastUpdated;

    public AppointmentEntity getAppointmentEntity()  
    {  
        return appointmentEntity;  
    }  
    public void setAppointmentEntity(AppointmentEntity appointmentEntity)  
    {  
        this.appointmentEntity = appointmentEntity;  
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
