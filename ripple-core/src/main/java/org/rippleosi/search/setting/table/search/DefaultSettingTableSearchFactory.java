package org.rippleosi.search.setting.table.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultSettingTableSearchFactory extends AbstractRepositoryFactory<SettingTableSearch> {

    @Override
    protected SettingTableSearch defaultRepository() {
        return new NotConfiguredSettingTableSearch();
    }

    @Override
    protected Class<SettingTableSearch> repositoryClass() {
        return SettingTableSearch.class;
    }
}
