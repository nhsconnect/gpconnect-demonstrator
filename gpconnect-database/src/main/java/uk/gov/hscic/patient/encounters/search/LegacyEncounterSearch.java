/*
 * Copyright 2016 HSCIC
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

package uk.gov.hscic.patient.encounters.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;
import uk.gov.hscic.patient.encounters.repo.EncounterRepository;

@Service
public class LegacyEncounterSearch extends AbstractLegacyService implements EncounterSearch {

    @Autowired
    private EncounterRepository encounterRepository;

    @Override
    public List<EncounterListHTML> findAllEncounterHTMLTables(final String patientId) {
        final List<EncounterEntity> encounters = encounterRepository.findAll();

        return CollectionUtils.collect(encounters, new EncounterEntityToListTransformer(), new ArrayList<>());
    }
}
