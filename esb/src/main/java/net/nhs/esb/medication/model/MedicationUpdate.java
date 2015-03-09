package net.nhs.esb.medication.model;

import java.util.Map;

/**
 */
public class MedicationUpdate {

    private final Map<String,String> content;

    public MedicationUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
