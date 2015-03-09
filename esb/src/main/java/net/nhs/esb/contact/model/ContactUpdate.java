package net.nhs.esb.contact.model;

import java.util.Map;

/**
 */
public class ContactUpdate {

    private final Map<String,String> content;

    public ContactUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
