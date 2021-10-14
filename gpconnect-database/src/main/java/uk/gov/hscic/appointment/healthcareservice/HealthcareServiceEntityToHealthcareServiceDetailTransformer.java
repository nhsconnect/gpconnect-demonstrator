package uk.gov.hscic.appointment.healthcareservice;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.model.appointment.HealthcareServiceDetail;

public class HealthcareServiceEntityToHealthcareServiceDetailTransformer implements Transformer<HealthcareServiceEntity, HealthcareServiceDetail> {

    @Override
    public HealthcareServiceDetail transform(HealthcareServiceEntity item) {
        HealthcareServiceDetail healthcareServiceDetail = new HealthcareServiceDetail();
        healthcareServiceDetail.setId(item.getId());
        healthcareServiceDetail.setIdentifier(item.getIdentifier());
        healthcareServiceDetail.setName(item.getName());
        healthcareServiceDetail.setOrganizationId(item.getOrganizationId());
        return healthcareServiceDetail;
    }
}
