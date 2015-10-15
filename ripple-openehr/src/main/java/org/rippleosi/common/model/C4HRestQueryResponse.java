package org.rippleosi.common.model;

import java.util.List;

import org.rippleosi.search.setting.table.model.OpenEHRSettingResponse;

public class C4HRestQueryResponse {

    private List<OpenEHRSettingResponse> body;

    public List<OpenEHRSettingResponse> getBody() {
        return body;
    }

    public void setBody(List<OpenEHRSettingResponse> body) {
        this.body = body;
    }
}
