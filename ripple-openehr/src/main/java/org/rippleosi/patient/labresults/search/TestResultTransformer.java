package org.rippleosi.patient.labresults.search;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class TestResultTransformer implements Transformer<Map<String, Object>, LabResultDetails.TestResult> {

    private static final Logger logger = LoggerFactory.getLogger(TestResultTransformer.class);

    private String getValueAsString(Map<String, Object> input, String path) {
        try {
            Object value = PropertyUtils.getNestedProperty(input, path);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception ex) {
            logger.debug("{}: {}", ex.getClass().getName(), ex.getMessage());
        }

        return null;
    }

    @Override
    public LabResultDetails.TestResult transform(Map<String, Object> input) {

        LabResultDetails.TestResult testResult = new LabResultDetails.TestResult();

        String name = getValueAsString(input, "(items)[0].name.value");
        String value = getValueAsString(input, "(items)[0].value.magnitude");
        String units = getValueAsString(input, "(items)[0].value.units");
        String comment = getValueAsString(input, "(items)[1].value.value");

        testResult.setResult(name);
        testResult.setValue(value);
        testResult.setUnit(units);
        testResult.setComment(comment);

        return testResult;
    }
}
