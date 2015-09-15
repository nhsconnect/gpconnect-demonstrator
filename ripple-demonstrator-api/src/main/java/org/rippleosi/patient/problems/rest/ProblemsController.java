package org.rippleosi.patient.problems.rest;

import java.util.List;

import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.model.ProblemSummary;
import org.rippleosi.patient.problems.search.ProblemSearch;
import org.rippleosi.patient.problems.search.ProblemSearchFactory;
import org.rippleosi.patient.problems.store.ProblemStore;
import org.rippleosi.patient.problems.store.ProblemStoreFactory;
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
@RequestMapping("/patients/{patientId}/diagnoses")
public class ProblemsController {

    @Autowired
    private ProblemSearchFactory problemSearchFactory;

    @Autowired
    private ProblemStoreFactory problemStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<ProblemSummary> findAllProblems(@PathVariable("patientId") String patientId,
                                                @RequestParam(required = false) String source) {

        ProblemSearch problemSearch = problemSearchFactory.select(source);

        return problemSearch.findAllProblems(patientId);
    }

    @RequestMapping(value = "/headlines", method = RequestMethod.GET)
    public List<ProblemHeadline> findProblemHeadlines(@PathVariable("patientId") String patientId,
                                                      @RequestParam(required = false) String source) {
        ProblemSearch problemSearch = problemSearchFactory.select(source);

        return problemSearch.findProblemHeadlines(patientId);
    }

    @RequestMapping(value = "/{problemId}", method = RequestMethod.GET)
    public ProblemDetails findProblem(@PathVariable("patientId") String patientId,
                                      @PathVariable("problemId") String problemId,
                                      @RequestParam(required = false) String source) {

        ProblemSearch problemSearch = problemSearchFactory.select(source);

        return problemSearch.findProblem(patientId, problemId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPatientProblem(@PathVariable("patientId") String patientId,
                                     @RequestParam(required = false) String source,
                                     @RequestBody ProblemDetails problem) {

        ProblemStore problemStore = problemStoreFactory.select(source);

        problemStore.create(patientId, problem);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updatePatientProblem(@PathVariable("patientId") String patientId,
                                     @RequestParam(required = false) String source,
                                     @RequestBody ProblemDetails problem) {

        ProblemStore problemStore = problemStoreFactory.select(source);

        problemStore.update(patientId, problem);
    }
}
