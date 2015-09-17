package org.rippleosi.patient.medication.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.medication.model.MedicationDetails;

/**
 */
public class MedicationDetailsQueryStrategy extends AbstractQueryStrategy<MedicationDetails> {

    private final String medicationId;

    MedicationDetailsQueryStrategy(String patientId, String medicationId) {
        super(patientId);
        this.medicationId = medicationId;
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
               "a/composer/name as author, " +
               "a_a/items/items/data[at0001]/items/items[at0001]/value/value as name, " +
               "a_a/items/items/data[at0001]/items/items[at0001]/value/defining_code/code_string as medication_code, " +
               "a_a/items/items/data[at0001]/items/items[at0001]/value/defining_code/terminology_id/value as medication_terminology, " +
               "a_a/items/items/data[at0001]/items/items[at0002]/value/defining_code/code_string as route, " +
               "a_a/items/items/data[at0001]/items/items[at0003]/value/value as dose_directions, " +
               "a_a/items/items/data[at0001]/items/items[at0020]/value/value as dose_amount, " +
               "a_a/items/items/data[at0001]/items/items[at0021]/value/value as dose_timing, " +
               "a_a/items/items/data[at0001]/items/items[at0046]/items/value/value as start_date " +
               "from EHR e[ehr_id/value='" + ehrId + "'] " +
               "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
               "contains SECTION a_a[openEHR-EHR-SECTION.medication_medical_devices_rcp.v1] " +
               "where a/name/value='Current medication list' " +
               "and a/uid/value='" + medicationId + "' ";
    }

    @Override
    public MedicationDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(2);

        String startDateTimeAsString = MapUtils.getString(data, "start_date");

        Date startDateTime = DateFormatter.toDate(startDateTimeAsString);

        MedicationDetails medication = new MedicationDetails();
        medication.setSource("openehr");
        medication.setSourceId(MapUtils.getString(data, "uid"));
        medication.setName(MapUtils.getString(data, "name"));
        medication.setDoseAmount(MapUtils.getString(data, "dose_amount"));
        medication.setDoseTiming(MapUtils.getString(data, "dose_timing"));
        medication.setDoseDirections(MapUtils.getString(data, "dose_directions"));
        medication.setRoute(MapUtils.getString(data, "route"));
        medication.setMedicationCode(MapUtils.getString(data, "medication_code"));
        medication.setMedicationTerminology(MapUtils.getString(data, "medication_terminology"));
        medication.setStartDateTime(startDateTime);

        return medication;
    }
}
