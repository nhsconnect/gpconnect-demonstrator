/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.allergies.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.allergies.model.AllergyDetails;

/**
 */
public class AllergyDetailsTransformer implements Transformer<Map<String, Object>, AllergyDetails> {

    @Override
    public AllergyDetails transform(Map<String, Object> input) {

        AllergyDetails allergy = new AllergyDetails();
        allergy.setSource("Marand");
        allergy.setSourceId(MapUtils.getString(input, "uid"));
        allergy.setCause(MapUtils.getString(input, "cause"));
        allergy.setReaction(MapUtils.getString(input, "reaction"));
        allergy.setCauseCode(MapUtils.getString(input, "cause_code"));
        allergy.setCauseTerminology(MapUtils.getString(input, "cause_terminology"));
        allergy.setTerminologyCode(MapUtils.getString(input, "cause_code"));
        allergy.setAuthor(MapUtils.getString(input, "author"));

        String dateCreated = MapUtils.getString(input, "date_created");
        allergy.setDateCreated(DateFormatter.toDate(dateCreated));

        return allergy;
    }
}
