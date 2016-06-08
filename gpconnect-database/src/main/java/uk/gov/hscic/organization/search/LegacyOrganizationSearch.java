package uk.gov.hscic.organization.search;

import uk.gov.hscic.common.service.AbstractLegacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.model.OrganizationEntity;
import uk.gov.hscic.organization.repo.OrganizationRepository;

@Service
public class LegacyOrganizationSearch extends AbstractLegacyService implements OrganizationSearch {

    @Autowired
    private OrganizationRepository organizationRepository;

    private final OrganizationEntityToObjectTransformer transformer = new OrganizationEntityToObjectTransformer();

    @Override
    public OrganizationDetails findOrganizationDetails(final String organizationId) {

        final OrganizationEntity item = organizationRepository.findOne(Long.parseLong(organizationId));

        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }
}
