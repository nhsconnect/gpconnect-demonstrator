package net.nhs.esb.rest.domain;

import java.util.List;
import java.util.Map;

/**
 */
public class QueryResponseData {

    private Meta meta;
    private String aql;
    private String executedAql;
    private Map<String,Object> aqlParameters;
    private List<Map<String,Object>> resultSet;

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

    public Map<String, Object> getAqlParameters() {
        return aqlParameters;
    }

    public void setAqlParameters(Map<String, Object> aqlParameters) {
        this.aqlParameters = aqlParameters;
    }

    public List<Map<String, Object>> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<Map<String, Object>> resultSet) {
        this.resultSet = resultSet;
    }
}
