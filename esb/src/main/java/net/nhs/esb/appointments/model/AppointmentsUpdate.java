package net.nhs.esb.appointments.model;

import java.util.Map;


public class AppointmentsUpdate {

    private final Map<String,String> content;

    public AppointmentsUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
