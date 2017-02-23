package uk.gov.hscic.patient.clinicalitems.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemData;

import java.util.Date;
import java.util.List;

public interface ClinicalItemSearch extends Repository {

    List<ClinicalItemData> findAllClinicalItemHTMLTables(String patientId, Date fromDate, Date toDate);
}
