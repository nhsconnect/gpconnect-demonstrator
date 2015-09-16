package org.rippleosi.patient.labresults.search;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.rippleosi.patient.labresults.model.LabResultSummary;

/**
 */
public class NotConfiguredLabResultSearch implements LabResultSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<LabResultSummary> findAllLabResults(String patientId) {
        // TODO Replace this code with notImplemented()

        LabResultSummary labResult = new LabResultSummary();
        labResult.setSourceId("1");
        labResult.setSource("openehr");
        labResult.setDateCreated(new Date());
        labResult.setSampleTaken(new Date());
        labResult.setTestName("Urea, electrolytes and creatinine measurement");

        return Collections.singletonList(labResult);
    }

    @Override
    public LabResultDetails findLabResult(String patientId, String labResultId) {

        LabResultDetails.TestResult testResult = new LabResultDetails.TestResult();
        testResult.setResult("Potassium");
        testResult.setValue("3.5");
        testResult.setUnit("mmol/l");
        testResult.setComment("may be technical artefact");

        LabResultDetails labResult = new LabResultDetails();
        labResult.setSourceId("1");
        labResult.setSource("openehr");
        labResult.setDateCreated(new Date());
        labResult.setSampleTaken(new Date());
        labResult.setTestName("Urea, electrolytes and creatinine measurement");
        labResult.setStatus("Final");
        labResult.setConclusion("Rapidly deteriorating renal function");
        labResult.setAuthor("Dr John Smith");
        labResult.setTestResults(Collections.singletonList(testResult));

        return labResult;
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + LabResultSearch.class.getSimpleName() + " instance");
    }
}
