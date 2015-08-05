package net.nhs.esb.mdtreport.model;

import java.util.Map;

/**
 */
public class MDTReportUpdate {

    private final Map<String,String> content;

    public MDTReportUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
