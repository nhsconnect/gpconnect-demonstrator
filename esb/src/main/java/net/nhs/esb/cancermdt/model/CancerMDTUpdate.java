package net.nhs.esb.cancermdt.model;

import java.util.Map;

/**
 */
public class CancerMDTUpdate {

    private final Map<String,String> content;
    
    public CancerMDTUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
