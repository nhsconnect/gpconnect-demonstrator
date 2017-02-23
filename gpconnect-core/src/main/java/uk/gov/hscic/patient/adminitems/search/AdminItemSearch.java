package uk.gov.hscic.patient.adminitems.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.adminitems.model.AdminItemData;

import java.util.Date;
import java.util.List;

public interface AdminItemSearch extends Repository {

    List<AdminItemData> findAllAdminItemHTMLTables(String patientId, Date fromDate, Date toDate);
}
