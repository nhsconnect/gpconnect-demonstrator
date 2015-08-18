package net.nhs.esb.lab.results.route.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.lab.results.model.LabResult;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class LabResultCompositionConverter {

    private static final String LAB_RESULT_PREFIX = "laboratory_test_report/laboratory_test";

    @Converter
    public LabResult convertResponseToLabResult(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        List<LabResult.LabResultDetail> resultDetailList = extractDetails(rawComposition);

        String sampleTaken = MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/time");
        String dateReported = MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/test_status_timestamp");

        LabResult labResult = new LabResult();
        labResult.setCompositionId(MapUtils.getString(rawComposition, "laboratory_test_report/_uid"));
        labResult.setAuthor(MapUtils.getString(rawComposition, "laboratory_test_report/ctx/composer_name"));
        labResult.setCode(MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/test_name|code"));
        labResult.setName(MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/test_name|value"));
        labResult.setStatusCode(MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/test_status|code"));
        labResult.setStatus(MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/test_status|value"));
        labResult.setConclusion(MapUtils.getString(rawComposition, LAB_RESULT_PREFIX + "/conclusion"));
        labResult.setDateReported(DateFormatter.toDate(dateReported));
        labResult.setSampleTaken(DateFormatter.toDate(sampleTaken));
        labResult.setResultDetails(resultDetailList);

        return labResult;
    }

    private List<LabResult.LabResultDetail> extractDetails(Map<String, Object> rawComposition) {

        List<LabResult.LabResultDetail> detailList = new ArrayList<>();

        int index = 0;
        boolean hasAnotherEntry = true;

        while (hasAnotherEntry) {
            String prefix = LAB_RESULT_PREFIX + "/laboratory_test_panel/laboratory_result:" + index;

            LabResult.LabResultDetail labResultDetail = new LabResult.LabResultDetail();
            labResultDetail.setResult(MapUtils.getString(rawComposition, prefix + "/result_value/_name|code"));
            labResultDetail.setValue(MapUtils.getString(rawComposition, prefix + "result_value|magnitude"));
            labResultDetail.setUnit(MapUtils.getString(rawComposition, prefix + "/result_value|unit"));
            labResultDetail.setNormalRange(MapUtils.getString(rawComposition, prefix + "/result_value|normal_range"));
            labResultDetail.setComment(MapUtils.getString(rawComposition, prefix + "/comment"));

            hasAnotherEntry = isNotEmpty(labResultDetail);

            if (hasAnotherEntry) {
                detailList.add(labResultDetail);
                index++;
            }
        }

        return detailList;
    }

    private boolean isNotEmpty(LabResult.LabResultDetail labResultDetail) {
        return labResultDetail.getResult() != null ||
                labResultDetail.getValue() != null ||
                labResultDetail.getUnit() != null;
    }
}
