package uk.gov.hscic.organization.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.organization.model.OrganizationEntity;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

    List<OrganizationEntity> findByOrgCode(String org_code);
    
    List<OrganizationEntity> findByOrgCodeAndSiteCode(String org_code, String site_code);
    
}
