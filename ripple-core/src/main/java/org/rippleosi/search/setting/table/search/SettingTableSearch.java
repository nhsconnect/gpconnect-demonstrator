package org.rippleosi.search.setting.table.search;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableResults;

public interface SettingTableSearch extends Repository {

    SettingTableResults findAllPatientsByQuery(SettingTableQuery tableQuery);
}
