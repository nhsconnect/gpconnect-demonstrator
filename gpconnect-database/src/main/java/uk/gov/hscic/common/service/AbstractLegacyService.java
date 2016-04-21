package uk.gov.hscic.common.service;

import org.springframework.beans.factory.annotation.Value;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.common.types.RepoSourceType;

public class AbstractLegacyService implements Repository {

    @Value("${legacy.datasource.priority:900}")
    private int priority;

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.LEGACY;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
