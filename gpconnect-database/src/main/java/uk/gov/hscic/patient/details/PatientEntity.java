/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.patient.details;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.gov.hscic.medical.department.DepartmentEntity;
import uk.gov.hscic.medical.practicitioners.doctor.GPEntity;

@Entity
@Table(name = "patients")
public class PatientEntity {

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "address_3")
    private String address3;

    @Column(name = "address_4")
    private String address4;

    @Column(name = "address_5")
    private String address5;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deceased")
    private Date deceasedDateTime;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "gender")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "gp_id")
    private GPEntity gp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdated")
    private Date lastUpdated;

    @Column(name = "managing_organization")
    private String managingOrganization;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "multiple_birth")
    private boolean multipleBirth;
    
    @Column(name = "nhs_number")
    private String nhsNumber;

    @Column(name = "pas_number")
    private String pasNumber;

    @Column(name = "phone")
    private String phone;

    @Column(name = "postcode")
    private String postcode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_end")
    private Date registrationEndDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_start")
    private Date registrationStartDateTime;

    @Column(name = "registration_status")
    private String registrationStatus;

    @Column(name = "registration_type")
    private String registrationType;

    @Column(name = "sensitive_flag")
    private boolean sensitive;
    
    @Column(name = "title")
    private String title;
    
    public String getAddress1() {
        return address1;
    }
    
    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getAddress4() {
        return address4;
    }    

    public String getAddress5() {
        return address5;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDeceasedDateTime() {
		return deceasedDateTime;
	}

    public DepartmentEntity getDepartment() {
        return department;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public GPEntity getGp() {
        return gp;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getManagingOrganization() {
        return managingOrganization;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public String getPasNumber() {
        return pasNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public Date getRegistrationEndDateTime() {
		return registrationEndDateTime;
	}

    public Date getRegistrationStartDateTime() {
		return registrationStartDateTime;
	}

    public String getRegistrationStatus() {
		return registrationStatus;
	}

    public String getRegistrationType() {
		return registrationType;
	}

    public String getTitle() {
        return title;
    }

    public boolean isMultipleBirth() {
		return multipleBirth;
	}

    public boolean isSensitive() {
        return sensitive;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDeceasedDateTime(Date deceasedDateTime) {
		this.deceasedDateTime = deceasedDateTime;
	}

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGp(GPEntity gp) {
        this.gp = gp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

	public void setManagingOrganization(String managingOrganization) {
        this.managingOrganization = managingOrganization;
    }

	public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

	public void setMultipleBirth(boolean multipleBirth) {
		this.multipleBirth = multipleBirth;
	}

	public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

	public void setPasNumber(String pasNumber) {
        this.pasNumber = pasNumber;
    }

	public void setPhone(String phone) {
        this.phone = phone;
    }

	public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setRegistrationEndDateTime(Date registrationEndDateTime) {
		this.registrationEndDateTime = registrationEndDateTime;
	}

    public void setRegistrationStartDateTime(Date registrationStartDateTime) {
		this.registrationStartDateTime = registrationStartDateTime;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

	public void setTitle(String title) {
        this.title = title;
    }
}
