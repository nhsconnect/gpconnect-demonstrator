package uk.gov.hscic.patient.observations.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;
import uk.gov.hscic.patient.observations.search.ObservationSearch;
import uk.gov.hscic.patient.observations.search.ObservationSearchFactory;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/observations")
public class ObservationsController {

    @Autowired
    private ObservationSearchFactory observationSearchFactory;

    @RequestMapping(value="/htmlTables", method = RequestMethod.GET)
    public List<ObservationListHTML> findAllObservationHTMLTables(@PathVariable("patientId") String patientId,
                                                                  @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ObservationSearch observationSearch = observationSearchFactory.select(sourceType);

        return observationSearch.findAllObservationHTMLTables(patientId);
    }

}
