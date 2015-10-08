package org.rippleosi.search.setting;

import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableResults;
import org.rippleosi.search.setting.table.search.SettingTableSearch;
import org.rippleosi.search.setting.table.search.SettingTableSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/setting")
public class SearchBySettingController {

    @Autowired
    private SettingTableSearchFactory settingTableSearchFactory;

    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public SettingTableResults getSettingTable(@RequestParam(required = false) String source,
                                               @RequestBody SettingTableQuery tableQuery) {
        SettingTableSearch search = settingTableSearchFactory.select(source);
        return search.findAllPatientsByQuery(tableQuery);
    }
}
