package net.nhs.esb.lab.orders.model;

import java.util.Map;

/**
 */
public class LabOrderUpdate {

    private final Map<String,String> content;

    public LabOrderUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
