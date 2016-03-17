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

package org.rippleosi.patient.allergies.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractEtherCISQueryStrategy;
import org.rippleosi.patient.allergies.model.AllergyDetails;

/**
 */
public class EtherCISAllergyDetailsQueryStrategy extends AbstractEtherCISQueryStrategy<AllergyDetails> {

    private final String allergyId;

    public EtherCISAllergyDetailsQueryStrategy(String patientId, String allergyId) {
        super(patientId);
        this.allergyId = allergyId;
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT " +
            "ehr.entry.composition_id as uid, " +
            "ehr.party_identified.name as author, " +
            "ehr.event_context.start_time as date_created, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0002 and name/value=''Causative agent''],/value,value" +
                "}' as cause, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0002 and name/value=''Causative agent''],/value,definingCode,codeString" +
                "}' as cause_code, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, /items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0002 and name/value=''Causative agent''],/value,definingCode,terminologyId,value" +
                "}' as cause_terminology, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0011],0,/value,/value,value" +
                "}' as reaction, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0011],0,/value,/value,definingCode,codeString" +
                "}' as reaction_code, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0011],0,/value,/value,definingCode,terminologyId,value" +
                "}' as reaction_terminology, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0021],0,/value,/value,value" +
                "}' as certainty, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0021],0,/value,/value,definingCode,codeString" +
                "}' as certainty_code, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0021],0,/value,/value,definingCode,terminologyId,value" +
                "}' as certainty_terminology, " +
            "ehr.entry.entry #>> " +
                "'{" +
                    "/composition[openEHR-EHR-COMPOSITION.adverse_reaction_list.v1 and name/value=''Adverse reaction list''], " +
                    "/content[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1],0, " +
                    "/items[openEHR-EHR-EVALUATION.adverse_reaction_risk.v1],0,/data[at0001],/items[at0009 and name/value=''Reaction details''],/items[at0032],0,/value,/value,value" +
                "}' as comment " +
            "FROM ehr.entry " +
            "INNER JOIN ehr.composition ON ehr.composition.id=ehr.entry.composition_id " +
            "INNER JOIN ehr.event_context ON ehr.event_context.composition_id = ehr.entry.composition_id " +
            "INNER JOIN ehr.party_identified ON ehr.composition.composer = ehr.party_identified.id " +
            "WHERE (ehr.composition.ehr_id = '" + ehrId + "') " +
            "AND (ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.adverse_reaction_list.v1') " +
            "AND ehr.entry.composition_id = '" + allergyId + "';";
    }

    @Override
    public AllergyDetails transform(List<Map<String, Object>> resultSet) {
        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new EtherCISAllergyDetailsTransformer().transform(data);
    }
}
