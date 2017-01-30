package uk.gov.hscic.common.ldap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ProviderRouting {
    private String spineProxy;

    @JsonProperty("ASID")
    private String asid;

    private List<Practice> practices;

    public String getSpineProxy() {
        return spineProxy;
    }

    public void setSpineProxy(String spineProxy) {
        this.spineProxy = spineProxy;
    }

    public String getAsid() {
        return asid;
    }

    public void setAsid(String asid) {
        this.asid = asid;
    }

    public List<Practice> getPractices() {
        return practices;
    }

    public void setPractices(List<Practice> practices) {
        this.practices = practices;
    }
}
