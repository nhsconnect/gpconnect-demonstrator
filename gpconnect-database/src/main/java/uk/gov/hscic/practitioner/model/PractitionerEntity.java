package uk.gov.hscic.practitioner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "practitioners")
public class PractitionerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "p_user_id")
    private String p_user_id;
    
    @Column(name = "p_role_id")
    private String p_role_id;
    
    @Column(name = "p_name_family")
    private String p_name_family;
    
    @Column(name = "p_name_given")
    private String p_name_given;
    
    @Column(name = "p_name_prefix")
    private String p_name_prefix;
    
    @Column(name = "p_gender")
    private String p_gender;
    
    @Column(name = "p_organization_id")
    private Long p_organization_id;
    
    @Column(name = "p_role_code")
    private String p_role_code;
    
    @Column(name = "p_role_display")
    private String p_role_display;
    
    @Column(name = "p_com_code")
    private String p_com_code;
    
    @Column(name = "p_com_display")
    private String p_com_display;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getP_user_id() {
        return p_user_id;
    }

    public void setP_user_id(String p_user_id) {
        this.p_user_id = p_user_id;
    }

    public String getP_role_id() {
        return p_role_id;
    }

    public void setP_role_id(String p_role_id) {
        this.p_role_id = p_role_id;
    }

    public String getP_name_family() {
        return p_name_family;
    }

    public void setP_name_family(String p_name_family) {
        this.p_name_family = p_name_family;
    }

    public String getP_name_given() {
        return p_name_given;
    }

    public void setP_name_given(String p_name_given) {
        this.p_name_given = p_name_given;
    }

    public String getP_name_prefix() {
        return p_name_prefix;
    }

    public void setP_name_prefix(String p_name_prefix) {
        this.p_name_prefix = p_name_prefix;
    }

    public String getP_gender() {
        return p_gender;
    }

    public void setP_gender(String p_gender) {
        this.p_gender = p_gender;
    }

    public Long getP_organization_id() {
        return p_organization_id;
    }

    public void setP_organization_id(Long p_organization_id) {
        this.p_organization_id = p_organization_id;
    }

    public String getP_role_code() {
        return p_role_code;
    }

    public void setP_role_code(String p_role_code) {
        this.p_role_code = p_role_code;
    }

    public String getP_role_display() {
        return p_role_display;
    }

    public void setP_role_display(String p_role_display) {
        this.p_role_display = p_role_display;
    }

    public String getP_com_code() {
        return p_com_code;
    }

    public void setP_com_code(String p_com_code) {
        this.p_com_code = p_com_code;
    }

    public String getP_com_display() {
        return p_com_display;
    }

    public void setP_com_display(String p_com_display) {
        this.p_com_display = p_com_display;
    }
}
