package uk.gov.hscic.patient.adminitems.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearchFactory;

import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/adminitems")
public class AdminItemsController {

    @Autowired
    private AdminItemSearchFactory adminItemSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<AdminItemListHTML> findAllAdminItemHTMLTables(@PathVariable("patientId") String patientId,
                                                              @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AdminItemSearch adminItemSearch = adminItemSearchFactory.select(sourceType);

        return adminItemSearch.findAllAdminItemHTMLTables(patientId);
    }
}
