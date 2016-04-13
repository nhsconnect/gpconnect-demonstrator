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
import org.rippleosi.patient.medication.model.MedicationHeadline;

import java.util.Map;

public class EtherCISMedicationHeadlineTransformer implements Transformer<Map<String, Object>, MedicationHeadline> {

    @Override
    public MedicationHeadline transform(Map<String, Object> input) {

        MedicationHeadline medication = new MedicationHeadline();
        medication.setSource("EtherCIS");
        medication.setSourceId(MapUtils.getString(input, "uid"));
        medication.setName(MapUtils.getString(input, "name"));

        return medication;
    }
}