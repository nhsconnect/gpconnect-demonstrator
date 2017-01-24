package uk.gov.hscic.organization.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
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

    @Override
    public List<OrganizationDetails> findOrganizationDetailsByOrgODSCode(String organizationODSCode) {
        ArrayList<OrganizationDetails> organizations = new ArrayList<>();
        List<OrganizationEntity> items = organizationRepository.findByOrgCode(organizationODSCode);
        for(OrganizationEntity item : items){
            organizations.add(transformer.transform(item));
        }
        return organizations;
    }

    @Override
    public List<OrganizationDetails> findOrganizationDetailsByOrgODSCodeAndSiteODSCode(String organizationODSCode, String siteODSCode) {
        ArrayList<OrganizationDetails> organizations = new ArrayList<>();
        List<OrganizationEntity> items = organizationRepository.findByOrgCodeAndSiteCode(organizationODSCode, siteODSCode);
        for(OrganizationEntity item : items){
            organizations.add(transformer.transform(item));
        }
        return organizations;
    }

}
