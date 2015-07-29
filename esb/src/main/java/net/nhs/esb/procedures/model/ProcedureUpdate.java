package net.nhs.esb.procedures.model;

import java.util.Map;

/**
 */
public class ProcedureUpdate {

    private final Map<String,String> content;

    public ProcedureUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
