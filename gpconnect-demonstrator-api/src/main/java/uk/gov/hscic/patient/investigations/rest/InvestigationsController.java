package uk.gov.hscic.patient.investigations.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.investigations.model.InvestigationListHTML;
import uk.gov.hscic.patient.investigations.search.InvestigationSearch;
import uk.gov.hscic.patient.investigations.search.InvestigationSearchFactory;

import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/investigations")
public class InvestigationsController {

    @Autowired
    private InvestigationSearchFactory investigationSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<InvestigationListHTML> findAllInvestigationHTMLTables(@PathVariable("patientId") String patientId,
                                                                     @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final InvestigationSearch investigationSearch = investigationSearchFactory.select(sourceType);

        return investigationSearch.findAllInvestigationHTMLTables(patientId);
    }
}
