package net.nhs.esb.transfer.model;

import java.util.Map;

/**
 */
public class TransferOfCareUpdate {

    private final Map<String,String> content;

    public TransferOfCareUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
