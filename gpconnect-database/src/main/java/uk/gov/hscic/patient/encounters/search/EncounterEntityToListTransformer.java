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

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;

public class EncounterEntityToListTransformer implements Transformer<EncounterEntity, EncounterListHTML> {

    @Override
    public EncounterListHTML transform(final EncounterEntity encounterEntity) {
        final EncounterListHTML encounterList = new EncounterListHTML();

        encounterList.setSourceId(String.valueOf(encounterEntity.getId()));
        encounterList.setSource(RepoSourceType.LEGACY.getSourceName());

        encounterList.setProvider(encounterEntity.getProvider());
        encounterList.setHtml(encounterEntity.getHtml());

        return encounterList;
    }
}
