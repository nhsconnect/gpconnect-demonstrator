package uk.gov.hscic.patient.procedures.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.procedures.model.ProcedureDetails;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;
import uk.gov.hscic.patient.procedures.search.ProcedureSearch;
import uk.gov.hscic.patient.procedures.search.ProcedureSearchFactory;
import uk.gov.hscic.patient.procedures.store.ProcedureStore;
import uk.gov.hscic.patient.procedures.store.ProcedureStoreFactory;

@RestController
@RequestMapping("/patients/{patientId}/procedures")
public class ProceduresController {

    @Autowired
    private ProcedureSearchFactory procedureSearchFactory;

    @Autowired
    private ProcedureStoreFactory procedureStoreFactory;

    @RequestMapping(value="/htmlTables", method = RequestMethod.GET)
    public List<ProcedureListHTML> findAllProcedureHTMLTables(@PathVariable("patientId") String patientId,
                                                          @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ProcedureSearch procedureSearch = procedureSearchFactory.select(sourceType);

        return procedureSearch.findAllProceduresHTMLTables(patientId);
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
