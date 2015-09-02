package org.rippleosi.patient.procedures.model;

import java.util.Date;
import java.util.List;

/**
 */
public class ProcedureSummaries {

    private List<Procedure> procedures;

    public List<Procedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<Procedure> procedures) {
        this.procedures = procedures;
    }

    public static class Procedure {

        private String id;
        private String name;
        private Date date;
        private String source;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
