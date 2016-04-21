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
package uk.gov.hscic.patient.procedures.rest;

import java.util.List;

import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.procedures.model.ProcedureDetails;
import uk.gov.hscic.patient.procedures.model.ProcedureSummary;
import uk.gov.hscic.patient.procedures.search.ProcedureSearch;
import uk.gov.hscic.patient.procedures.search.ProcedureSearchFactory;
import uk.gov.hscic.patient.procedures.store.ProcedureStore;
import uk.gov.hscic.patient.procedures.store.ProcedureStoreFactory;
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
        final ProcedureSearch etherCISProcedureSearch = procedureSearchFactory.select(sourceType);

        return etherCISProcedureSearch.findAllProcedures(patientId);
    }

    @RequestMapping(value = "/{procedureId}", method = RequestMethod.GET)
    public ProcedureDetails findProcedure(@PathVariable("patientId") String patientId,
                                          @PathVariable("procedureId") String procedureId,
                                          @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ProcedureSearch procedureSearch = procedureSearchFactory.select(sourceType);

        return procedureSearch.findProcedure(patientId, procedureId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createProcedure(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody ProcedureDetails procedure) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ProcedureStore procedureStore = procedureStoreFactory.select(sourceType);

        procedureStore.create(patientId, procedure);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateProcedure(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody ProcedureDetails procedure) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ProcedureStore procedureStore = procedureStoreFactory.select(sourceType);

        procedureStore.update(patientId, procedure);
    }
}
