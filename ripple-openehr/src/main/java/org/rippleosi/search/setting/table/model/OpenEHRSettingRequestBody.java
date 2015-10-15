package org.rippleosi.search.setting.table.model;

public class OpenEHRSettingRequestBody {

    private String externalIds;
    private String externalNamespace;

    public String getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(String externalIds) {
        this.externalIds = externalIds;
    }

    public String getExternalNamespace() {
        return externalNamespace;
    }

    public void setExternalNamespace(String externalNamespace) {
        this.externalNamespace = externalNamespace;
    }
}
