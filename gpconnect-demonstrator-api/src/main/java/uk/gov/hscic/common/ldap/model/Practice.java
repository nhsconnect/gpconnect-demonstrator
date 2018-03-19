package uk.gov.hscic.common.ldap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Practice {
    private String id;
    private String odsCode;
    private String orgType;
    private String name;
    private List<String> interactionIds;
    private String endpointURL;
    private String apiEndpointURL;

    @JsonProperty("ASID")
    private String asid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOdsCode() {
        return odsCode;
    }

    public void setOdsCode(String odsCode) {
        this.odsCode = odsCode;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInteractionIds() {
        return interactionIds;
    }

    public void setInteractionIds(List<String> interactionIds) {
        this.interactionIds = interactionIds;
    }

    public String getEndpointURL() {
        return endpointURL;
    }

    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public String getApiEndpointURL() {
        return apiEndpointURL;
    }

    public void setApiEndpointURL(String apiEndpointURL) {
        this.apiEndpointURL = apiEndpointURL;
    }

    public String getAsid() {
        return asid;
    }

    public void setAsid(String asid) {
        this.asid = asid;
    }
}
