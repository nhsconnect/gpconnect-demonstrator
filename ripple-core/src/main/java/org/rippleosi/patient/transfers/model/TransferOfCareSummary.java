package org.rippleosi.patient.transfers.model;

import java.util.Date;

public class TransferOfCareSummary {

	private Long sourceId;
    private String siteTo;
    private String siteFrom;
    private Date dateOfTransfer;
    private String source;

    public Long getsourceId() {
        return sourceId;
    }

    public void setsourceId(Long id) {
        this.sourceId = id;
    }

    public String getSiteTo() {
        return siteTo;
    }

    public void setSiteTo(String siteTo) {
        this.siteTo = siteTo;
    }

    public String getSiteFrom() {
        return siteFrom;
    }

    public void setSiteFrom(String siteFrom) {
        this.siteFrom = siteFrom;
    }

    public Date getDateOfTransfer() {
        return dateOfTransfer;
    }

    public void setDateOfTransfer(Date dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
