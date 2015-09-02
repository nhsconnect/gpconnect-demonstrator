package org.rippleosi.common.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sessionResponseData")
public class LoginResponse {
    private String action;
    private String sessionId;
    private Meta meta;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
