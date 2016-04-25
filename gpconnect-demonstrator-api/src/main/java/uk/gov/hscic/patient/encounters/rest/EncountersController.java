package uk.gov.hscic.patient.encounters.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;
import uk.gov.hscic.patient.encounters.search.EncounterSearch;
import uk.gov.hscic.patient.encounters.search.EncounterSearchFactory;

@RestController
@RequestMapping("/patients/{patientId}/encounters")
public class EncountersController {

    @Autowired
    private EncounterSearchFactory encounterSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<EncounterListHTML> findAllEncounterHTMLTables(@PathVariable("patientId") String patientId,
                                                              @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final EncounterSearch encounterSearch = encounterSearchFactory.select(sourceType);

        return encounterSearch.findAllEncounterHTMLTables(patientId);
    }
}
