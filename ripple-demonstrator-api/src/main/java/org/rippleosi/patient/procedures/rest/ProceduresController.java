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

        ProcedureSearch procedureSearch = procedureSearchFactory.select(source);

        return procedureSearch.findAllProcedures(patientId);
    }

    @RequestMapping(value = "/{procedureId}", method = RequestMethod.GET)
    public ProcedureDetails findProcedure(@PathVariable("patientId") String patientId,
                                          @PathVariable("procedureId") String procedureId,
                                          @RequestParam(required = false) String source) {

        ProcedureSearch procedureSearch = procedureSearchFactory.select(source);

        return procedureSearch.findProcedure(patientId, procedureId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPatientProcedure(@PathVariable("patientId") String patientId,
                                       @RequestParam(required = false) String source,
                                       @RequestBody ProcedureDetails procedure) {

        ProcedureStore procedureStore = procedureStoreFactory.select(source);

        procedureStore.create(patientId, procedure);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updatePatientProcedure(@PathVariable("patientId") String patientId,
                                       @RequestParam(required = false) String source,
                                       @RequestBody ProcedureDetails procedure) {

        ProcedureStore procedureStore = procedureStoreFactory.select(source);

        procedureStore.update(patientId, procedure);
    }
}
