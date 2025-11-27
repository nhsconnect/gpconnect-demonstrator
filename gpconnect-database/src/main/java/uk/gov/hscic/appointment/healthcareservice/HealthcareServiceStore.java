package uk.gov.hscic.appointment.healthcareservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.model.appointment.HealthcareServiceDetail;

@Service
public class HealthcareServiceStore {
    private final HealthcareServiceEntityToHealthcareServiceDetailTransformer entityToDetailTransformer = new HealthcareServiceEntityToHealthcareServiceDetailTransformer();
    @Autowired
    private HealthcareServiceRepository healthcareServiceRepository;

    /**
     *
     * @param id
     * @return HealthcareServiceDetail
     */
    public HealthcareServiceDetail findHealthcareService(Long id){
        HealthcareServiceEntity healthcareServiceEntity = healthcareServiceRepository.findById(id).orElse(null);
        if (healthcareServiceEntity == null) {
            return null;
        }
        return entityToDetailTransformer.transform(healthcareServiceEntity);
    }
}
