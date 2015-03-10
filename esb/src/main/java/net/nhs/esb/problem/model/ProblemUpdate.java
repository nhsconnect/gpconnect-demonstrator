package net.nhs.esb.problem.model;

import java.util.Map;

/**
 */
public class ProblemUpdate {

    private final Map<String,String> content;

    public ProblemUpdate(Map<String, String> content) {
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
