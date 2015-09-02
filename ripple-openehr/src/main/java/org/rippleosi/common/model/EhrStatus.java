package org.rippleosi.common.model;

public class EhrStatus {
    private String subjectId;
    private String subjectNamespace;
    private Boolean queryable;
    private Boolean modifiable;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectNamespace() {
        return subjectNamespace;
    }

    public void setSubjectNamespace(String subjectNamespace) {
        this.subjectNamespace = subjectNamespace;
    }

    public Boolean getQueryable() {
        return queryable;
    }

    public void setQueryable(Boolean queryable) {
        this.queryable = queryable;
    }

    public Boolean getModifiable() {
        return modifiable;
    }

    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }
}
