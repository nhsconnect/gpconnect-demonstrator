package uk.gov.hscic.patient.clinicalitems.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;

import java.util.List;

public interface ClinicalItemSearch extends Repository {

    List<ClinicalItemListHTML> findAllClinicalItemHTMLTables(String patientId);
}
