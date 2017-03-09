package uk.gov.hscic.organization.search;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.model.OrganizationEntity;
import uk.gov.hscic.organization.repo.OrganizationRepository;

@Service
public class OrganizationSearch {
    private final OrganizationEntityToObjectTransformer transformer = new OrganizationEntityToObjectTransformer();

    @Autowired
    private OrganizationRepository organizationRepository;

    public OrganizationDetails findOrganizationDetails(final String organizationId) {
        final OrganizationEntity item = organizationRepository.findOne(Long.parseLong(organizationId));

        return item == null
                ? null
                : transformer.transform(item);
    }

    public List<OrganizationDetails> findOrganizationDetailsByOrgODSCode(String organizationODSCode) {
        return organizationRepository.findByOrgCode(organizationODSCode)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }

    public List<OrganizationDetails> findOrganizationDetailsByOrgODSCodeAndSiteODSCode(String organizationODSCode, String siteODSCode) {
        return organizationRepository.findByOrgCodeAndSiteCode(organizationODSCode, siteODSCode)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
