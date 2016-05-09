package uk.gov.hscic.patient.keyindicator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.keyindicator.model.KeyIndicatorListHTML;
import uk.gov.hscic.patient.keyindicator.search.KeyIndicatorSearch;
import uk.gov.hscic.patient.keyindicator.search.KeyIndicatorSearchFactory;

import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/keyindicators")
public class KeyIndicatorsController {

    @Autowired
    private KeyIndicatorSearchFactory keyIndicatorSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<KeyIndicatorListHTML> findAllKeyIndicatorHTMLTables(@PathVariable("patientId") String patientId,
                                                                    @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final KeyIndicatorSearch keyIndicatorSearch = keyIndicatorSearchFactory.select(sourceType);

        return keyIndicatorSearch.findAllKeyIndicatorHTMLTables(patientId);
    }
}
