package uk.gov.hscic.patient.keyindicator.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.keyindicator.model.KeyIndicatorListHTML;

import java.util.List;

public interface KeyIndicatorSearch extends Repository {

    List<KeyIndicatorListHTML> findAllKeyIndicatorHTMLTables(String patientId);
}
