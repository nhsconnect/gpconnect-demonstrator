package uk.gov.hscic.common.filters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebToken {
    private String aud;
    private int exp;
    private int iat;
    private String iss;
    private String sub;

    @JsonProperty("reason_for_request")
    private String reasonForRequest;

    @JsonProperty("requested_scope")
    private String requestedScope;

    @JsonProperty("requested_record")
    private RequestedRecord requestedRecord;

    @JsonProperty("requesting_device")
    private RequestingDevice requestingDevice;

    @JsonProperty("requesting_organization")
    private RequestingOrganization requestingOrganization;

    @JsonProperty("requesting_practitioner")
    private RequestingPractitioner requestingPractitioner;

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getReasonForRequest() {
        return reasonForRequest;
    }

    public void setReasonForRequest(String reasonForRequest) {
        this.reasonForRequest = reasonForRequest;
    }

    public String getRequestedScope() {
        return requestedScope;
    }

    public void setRequestedScope(String requestedScope) {
        this.requestedScope = requestedScope;
    }

    public RequestedRecord getRequestedRecord() {
        return requestedRecord;
    }

    public void setRequestedRecord(RequestedRecord requestedRecord) {
        this.requestedRecord = requestedRecord;
    }

    public RequestingDevice getRequestingDevice() {
        return requestingDevice;
    }

    public void setRequestingDevice(RequestingDevice requestingDevice) {
        this.requestingDevice = requestingDevice;
    }

    public RequestingOrganization getRequestingOrganization() {
        return requestingOrganization;
    }

    public void setRequestingOrganization(RequestingOrganization requestingOrganization) {
        this.requestingOrganization = requestingOrganization;
    }

    public RequestingPractitioner getRequestingPractitioner() {
        return requestingPractitioner;
    }

    public void setRequestingPractitioner(RequestingPractitioner requestingPractitioner) {
        this.requestingPractitioner = requestingPractitioner;
    }
    
    public boolean isReadRequestedScope() {
        return getRequestedScope().endsWith("read");
    }
    
    public boolean isWriteRequestedScope() {
        return getRequestedScope().endsWith("write");
    }    
}
