package org.rippleosi.search.setting.table.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.setting.table.model.SettingTableResults;

public interface SettingTableSearch extends Repository {

    SettingTableResults findAssociatedPatientData(List<PatientSummary> patientSummaries);
}
