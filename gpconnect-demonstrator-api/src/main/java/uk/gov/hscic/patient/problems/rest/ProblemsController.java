package uk.gov.hscic.patient.problems.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.problems.search.ProblemSearchFactory;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/problem")
public class ProblemsController {

    @Autowired
    private ProblemSearchFactory problemSearchFactory;

    @RequestMapping(value="/htmlTables", method = RequestMethod.GET)
    public List<ProblemListHTML> findAllProblemHTMLTables(@PathVariable("patientId") String patientId,
                                                @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ProblemSearch problemSearch = problemSearchFactory.select(sourceType);

        return problemSearch.findAllProblemHTMLTables(patientId);
    }
}
