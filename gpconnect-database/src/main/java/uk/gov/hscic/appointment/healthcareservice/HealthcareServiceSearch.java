package uk.gov.hscic.appointment.healthcareservice;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.model.appointment.HealthcareServiceDetail;

@Service
public class HealthcareServiceSearch {

    private final HealthcareServiceEntityToHealthcareServiceDetailTransformer transformer = new HealthcareServiceEntityToHealthcareServiceDetailTransformer();

    @Autowired
    private HealthcareServiceRepository healthcareServiceRepository;

    public HealthcareServiceDetail findHealthcareServiceByID(String healthcareServiceId) {
        final HealthcareServiceEntity item;
        try {
            item = healthcareServiceRepository.findOne(Long.parseLong(healthcareServiceId));
        } catch (NumberFormatException e) {
            return null;
        }

        return item == null ? null : transformer.transform(item);
    }

    public List<HealthcareServiceDetail> findHealthcareServiceForIdentifier(String identifier) {
        return healthcareServiceRepository.findByIdentifier(identifier)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }

    public List<HealthcareServiceDetail> findAllHealthcareServices() {
        return healthcareServiceRepository.findAll()
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
