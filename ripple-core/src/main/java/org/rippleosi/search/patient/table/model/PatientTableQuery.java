package org.rippleosi.search.patient.table.model;

import org.rippleosi.search.common.model.PageableTableQuery;

public class PatientTableQuery extends PageableTableQuery {

    private String searchString;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
