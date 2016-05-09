package uk.gov.hscic.patient.adminitems.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;

import java.util.List;

public interface AdminItemSearch extends Repository {

    List<AdminItemListHTML> findAllAdminItemHTMLTables(String patientId);
}
