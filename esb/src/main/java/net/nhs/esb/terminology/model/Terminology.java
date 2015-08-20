package net.nhs.esb.terminology.model;

/**
 */
public class Terminology {

    private final String code;
    private final String text;

    public Terminology(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
