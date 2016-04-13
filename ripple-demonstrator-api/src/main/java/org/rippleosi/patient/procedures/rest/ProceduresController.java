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
package org.rippleosi.patient.procedures.rest;

import java.util.List;

import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummary;
import org.rippleosi.patient.procedures.search.ProcedureSearch;
import org.rippleosi.patient.procedures.search.ProcedureSearchFactory;
import org.rippleosi.patient.procedures.store.ProcedureStore;
import org.rippleosi.patient.procedures.store.ProcedureStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/procedures")
public class ProceduresController {

    @Autowired
    private ProcedureSearchFactory procedureSearchFactory;

    @Autowired
    private ProcedureStoreFactory procedureStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<ProcedureSummary> findAllProcedures(@PathVariable("patientId") String patientId,
                                                    @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ProcedureSearch etherCISProcedureSearch = procedureSearchFactory.select(sourceType);
        List<ProcedureSummary> procedures = etherCISProcedureSearch.findAllProcedures(patientId);

        ProcedureSearch marandProcedureSearch = procedureSearchFactory.select(RepoSourceType.MARAND);
        procedures.addAll(marandProcedureSearch.findAllProcedures(patientId));

        return procedures;
    }

    @RequestMapping(value = "/{procedureId}", method = RequestMethod.GET)
    public ProcedureDetails findProcedure(@PathVariable("patientId") String patientId,
                                          @PathVariable("procedureId") String procedureId,
                                          @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ProcedureSearch procedureSearch = procedureSearchFactory.select(sourceType);

        return procedureSearch.findProcedure(patientId, procedureId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createProcedure(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody ProcedureDetails procedure) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ProcedureStore procedureStore = procedureStoreFactory.select(sourceType);

        procedureStore.create(patientId, procedure);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateProcedure(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody ProcedureDetails procedure) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        ProcedureStore procedureStore = procedureStoreFactory.select(sourceType);

        procedureStore.update(patientId, procedure);
    }
}
