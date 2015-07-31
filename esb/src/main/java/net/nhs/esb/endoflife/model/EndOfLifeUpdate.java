package net.nhs.esb.endoflife.model;

import java.util.Map;

/**
 */
public class EndOfLifeUpdate {

    private final Map<String,String> content;

    public EndOfLifeUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
