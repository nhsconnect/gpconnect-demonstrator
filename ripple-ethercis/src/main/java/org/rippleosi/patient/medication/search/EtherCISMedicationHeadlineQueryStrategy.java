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

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractEtherCISListQueryStrategy;
import org.rippleosi.patient.medication.model.MedicationHeadline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EtherCISMedicationHeadlineQueryStrategy extends AbstractEtherCISListQueryStrategy<MedicationHeadline> {

    EtherCISMedicationHeadlineQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT ehr.entry.composition_id as uid, " +
                "ehr.party_identified.name as author, " +
                "ehr.entry.entry #>> " +
                    "'{" +
                        "/composition[openEHR-EHR-COMPOSITION.medication_list.v0 and name/value=''Current medication list''], " +
                        "/content[openEHR-EHR-SECTION.medication_medical_devices_rcp.v1],0, " +
                        "/items[openEHR-EHR-SECTION.current_medication_rcp.v1],0,/items[openEHR-EHR-INSTRUCTION.medication_order.v0],0,/activities[at0001 and name/value=''Order''], /description[at0002],/items[at0070 and name/value=''Medication item''],/value,value " +
                    "}' as name " +
                "FROM ehr.entry " +
                "INNER JOIN ehr.composition ON ehr.composition.id=ehr.entry.composition_id " +
                "INNER JOIN ehr.party_identified ON ehr.composition.composer=ehr.party_identified.id " +
                "WHERE (ehr.composition.ehr_id = '" + ehrId + "') " +
                "AND (ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.medication_list.v0');";
    }

    @Override
    public List<MedicationHeadline> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new EtherCISMedicationHeadlineTransformer(), new ArrayList<>());
    }
}
