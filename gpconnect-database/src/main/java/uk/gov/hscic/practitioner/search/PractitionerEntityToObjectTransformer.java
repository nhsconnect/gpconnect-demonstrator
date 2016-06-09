package uk.gov.hscic.practitioner.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.practitioner.model.PractitionerDetails;
import uk.gov.hscic.practitioner.model.PractitionerEntity;

public class PractitionerEntityToObjectTransformer implements Transformer<PractitionerEntity, PractitionerDetails> {

    @Override
    public PractitionerDetails transform(final PractitionerEntity practitionerEntity) {
        
        PractitionerDetails practitioner = new PractitionerDetails();
        
        practitioner.setId(practitionerEntity.getId());
        practitioner.setUserId(practitionerEntity.getP_user_id());
        practitioner.setRoleId(practitionerEntity.getP_role_id());
        practitioner.setNameFamily(practitionerEntity.getP_name_family());
        practitioner.setNameGiven(practitionerEntity.getP_name_given());
        practitioner.setNamePrefix(practitionerEntity.getP_name_prefix());
        practitioner.setGender(practitionerEntity.getP_gender());
        practitioner.setOrganizationId(practitionerEntity.getP_organization_id());
        practitioner.setRoleCode(practitionerEntity.getP_role_code());
        practitioner.setRoleDisplay(practitionerEntity.getP_role_display());
        practitioner.setComCode(practitionerEntity.getP_com_code());
        practitioner.setComDisplay(practitionerEntity.getP_com_display());
        
        return practitioner;
    }
}
