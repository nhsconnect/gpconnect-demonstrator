package org.rippleosi.search.setting.table.model;

import org.rippleosi.search.patient.table.model.AbstractPageableTableQuery;

public class SettingTableQuery extends AbstractPageableTableQuery {

    private String searchString;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
