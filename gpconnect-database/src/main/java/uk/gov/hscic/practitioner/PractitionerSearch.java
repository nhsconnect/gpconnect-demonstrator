package uk.gov.hscic.practitioner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.model.practitioner.PractitionerDetails;

@Service
public class PractitionerSearch {
    private final PractitionerEntityToObjectTransformer transformer = new PractitionerEntityToObjectTransformer();

    @Autowired
    private PractitionerRepository practitionerRepository;

    public PractitionerDetails findPractitionerDetails(final String practitionerId) {
        final PractitionerEntity item = practitionerRepository.findOne(Long.parseLong(practitionerId));

        return item == null
                ? null
                : transformer.transform(item);
    }

    public PractitionerDetails findPractitionerByUserId(final String practitionerUserId) {
        final PractitionerEntity practitioner = practitionerRepository.findByuserid(practitionerUserId);

        return practitioner == null
                ? null
                : transformer.transform(practitioner);
    }
}
