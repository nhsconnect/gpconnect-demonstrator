package net.nhs.esb.referrals.model;

import java.util.Map;


public class ReferralUpdate {

    private final Map<String,String> content;

    public ReferralUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
