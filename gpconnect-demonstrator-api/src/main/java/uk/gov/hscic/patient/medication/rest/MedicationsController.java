package uk.gov.hscic.patient.medication.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;
import uk.gov.hscic.patient.medication.search.MedicationSearch;
import uk.gov.hscic.patient.medication.search.MedicationSearchFactory;

@RestController
@RequestMapping("/patients/{patientId}/medications")
public class MedicationsController {

    @Autowired
    private MedicationSearchFactory medicationSearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<MedicationListHTML> findMedicationHTMLTables(@PathVariable("patientId") String patientId,
                                                             @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final MedicationSearch medicationSearch = medicationSearchFactory.select(sourceType);

        return medicationSearch.findMedicationHTMLTables(patientId);
    }
}
