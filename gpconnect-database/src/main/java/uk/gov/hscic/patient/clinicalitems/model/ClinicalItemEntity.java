package uk.gov.hscic.patient.clinicalitems.model;

import javax.persistence.*;

@Entity
@Table(name = "clinicalitems")
public class ClinicalItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "html")
    private String html;

    @Column(name = "provider")
    private String provider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
