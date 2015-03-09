package net.nhs.esb.allergy.model;

import java.util.Map;

/**
 */
public class AllergyUpdate {

    private final Map<String,String> content;

    public AllergyUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
