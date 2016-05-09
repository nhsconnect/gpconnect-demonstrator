package uk.gov.hscic.patient.adminitems.search;

import uk.gov.hscic.common.repo.RepositoryFactory;
import uk.gov.hscic.patient.adminitems.search.AdminItemSearch;

@FunctionalInterface
public interface AdminItemSearchFactory extends RepositoryFactory<AdminItemSearch> {
}
