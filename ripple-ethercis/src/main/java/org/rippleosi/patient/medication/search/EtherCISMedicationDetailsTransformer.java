/*
 *  Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */
package org.rippleosi.patient.medication.search;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.medication.model.MedicationDetails;

import java.util.Date;
import java.util.Map;

public class EtherCISMedicationDetailsTransformer implements Transformer<Map<String, Object>, MedicationDetails> {

    @Override
    public MedicationDetails transform(Map<String, Object> input) {

        String startDateTimeAsString = MapUtils.getString(input, "start_date");
        String dateCreatedAsString = MapUtils.getString(input, "date_created");

        Date startDate = DateFormatter.toDateOnly(startDateTimeAsString);
        Date startTime = DateFormatter.toTimeOnly(startDateTimeAsString);
        Date dateCreated = DateFormatter.toDate(dateCreatedAsString);

        MedicationDetails medication = new MedicationDetails();
        medication.setSource("EtherCIS");
        medication.setSourceId(MapUtils.getString(input, "uid"));
        medication.setName(MapUtils.getString(input, "name"));
        medication.setDoseAmount(MapUtils.getString(input, "dose_amount"));
        medication.setDoseTiming(MapUtils.getString(input, "dose_timing"));
        medication.setDoseDirections(MapUtils.getString(input, "dose_directions"));
        medication.setRoute(MapUtils.getString(input, "route"));
        medication.setMedicationCode(MapUtils.getString(input, "medication_code"));
        medication.setMedicationTerminology(MapUtils.getString(input, "medication_terminology"));
        medication.setStartDate(startDate);
        medication.setStartTime(startTime);
        medication.setAuthor(MapUtils.getString(input, "author"));
        medication.setDateCreated(dateCreated);

        return medication;
    }
}
