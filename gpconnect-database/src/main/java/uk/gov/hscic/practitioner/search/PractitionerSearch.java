package uk.gov.hscic.practitioner.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.practitioner.model.PractitionerDetails;
import uk.gov.hscic.practitioner.model.PractitionerEntity;
import uk.gov.hscic.practitioner.repo.PractitionerRepository;

@Service
public class PractitionerSearch {
    private final PractitionerEntityToObjectTransformer transformer = new PractitionerEntityToObjectTransformer();

    @Autowired
    private PractitionerRepository practitionerRepository;

    public PractitionerDetails findPractitionerDetails(final String practitionerId) {
        final PractitionerEntity item = practitionerRepository.findById(Long.parseLong(practitionerId)).get();

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
