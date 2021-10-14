package uk.gov.hscic.appointment.healthcareservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_healthcareservices")
public class HealthcareServiceEntity {
    
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "identifier")
    private String identifier;
    
    @Column(name = "healthcareservice_name")
    private String name;
    
    @Column(name = "organizationId")
    private Long organizationId;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
