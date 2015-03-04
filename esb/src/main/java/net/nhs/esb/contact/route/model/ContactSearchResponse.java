package net.nhs.esb.contact.route.model;

import net.nhs.esb.rest.domain.Meta;

/**
 */
public class ContactSearchResponse {

    private Meta meta;
    private String aql;
    private String executedAql;
    private ContactSearchResult[] resultSet;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getAql() {
        return aql;
    }

    public void setAql(String aql) {
        this.aql = aql;
    }

    public String getExecutedAql() {
        return executedAql;
    }

    public void setExecutedAql(String executedAql) {
        this.executedAql = executedAql;
    }

    public ContactSearchResult[] getResultSet() {
        return resultSet;
    }

    public void setResultSet(ContactSearchResult[] resultSet) {
        this.resultSet = resultSet;
    }
}
