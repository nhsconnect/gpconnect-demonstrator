package uk.gov.hscic.patient.immunisations.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;
import uk.gov.hscic.patient.immunisations.search.ImmunisationSearch;
import uk.gov.hscic.patient.immunisations.search.ImmunisationSearchFactory;

import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/immunisations")
public class ImmunisationsController {

    @Autowired
    private ImmunisationSearchFactory immunisationSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<ImmunisationListHTML> findAllImmunisationHTMLTables(@PathVariable("patientId") String patientId,
                                                                    @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ImmunisationSearch immunisationSearch = immunisationSearchFactory.select(sourceType);

        return immunisationSearch.findAllImmunisationHTMLTables(patientId);
    }
}
