package uk.gov.hscic.patient.clinicalitems.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearch;
import uk.gov.hscic.patient.clinicalitems.search.ClinicalItemSearchFactory;

import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/clinicalitems")
public class ClinicalItemsController {

    @Autowired
    private ClinicalItemSearchFactory clinicalItemSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<ClinicalItemListHTML> findAllClinicalItemHTMLTables(@PathVariable("patientId") String patientId,
                                                                    @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ClinicalItemSearch clinicalItemSearch = clinicalItemSearchFactory.select(sourceType);

        return clinicalItemSearch.findAllClinicalItemHTMLTables(patientId);
    }
}
