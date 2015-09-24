package org.rippleosi.patient.careplans.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.careplans.model.CarePlanSummary;

/**
 */
public class CarePlanSummaryTransformer implements Transformer<Map<String, Object>, CarePlanSummary> {

    @Override
    public CarePlanSummary transform(Map<String, Object> input) {

        Date date = DateFormatter.toDate(MapUtils.getString(input, "date_created"));

        CarePlanSummary carePlan = new CarePlanSummary();
        carePlan.setSource("openehr");
        carePlan.setSourceId(MapUtils.getString(input, "uid"));
        carePlan.setName("End of Life Care");
        carePlan.setDate(date);
        carePlan.setType("Document");

        return carePlan;
    }
}
