package org.rippleosi.common.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ehrResponseData")
public class EhrResponse {
    private EhrStatus ehrStatus;
    private String ehrId;
    private Meta meta;
    private String action;

    public EhrStatus getEhrStatus() {
        return ehrStatus;
    }

    public void setEhrStatus(EhrStatus ehrStatus) {
        this.ehrStatus = ehrStatus;
    }

    public String getEhrId() {
        return ehrId;
    }

    public void setEhrId(String ehrId) {
        this.ehrId = ehrId;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
