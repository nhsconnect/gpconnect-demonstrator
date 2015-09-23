package org.rippleosi.patient.terminology.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.terminology.model.Terminology;

public class NotConfiguredTerminologySearch implements TerminologySearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<Terminology> findTerms(String type) {
        throw ConfigurationException.unimplementedTransaction(TerminologySearch.class);
    }
}
