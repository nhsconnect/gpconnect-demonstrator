package org.rippleosi.patient.medication.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.medication.model.MedicationHeadline;

/**
 */
public class MedicationHeadlineQueryStrategy extends AbstractListQueryStrategy<MedicationHeadline> {

    MedicationHeadlineQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
               "a_a/items/items/data[at0001]/items/items[at0001]/value/value as name " +
               "from EHR e[ehr_id/value='" + ehrId + "'] " +
               "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
               "contains SECTION a_a[openEHR-EHR-SECTION.medication_medical_devices_rcp.v1] " +
               "where a/name/value='Current medication list'";
    }

    @Override
    public List<MedicationHeadline> transform(List<Map<String, Object>> resultSet) {

        List<MedicationHeadline> medicationList = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {

            MedicationHeadline medication = new MedicationHeadline();
            medication.setSource("openehr");
            medication.setSourceId(MapUtils.getString(data, "uid"));
            medication.setName(MapUtils.getString(data, "name"));

            medicationList.add(medication);
        }

        return medicationList;
    }
}
