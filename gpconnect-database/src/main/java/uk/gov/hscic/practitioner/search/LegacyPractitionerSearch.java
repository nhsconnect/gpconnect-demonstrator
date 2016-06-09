package uk.gov.hscic.practitioner.search;

import uk.gov.hscic.common.service.AbstractLegacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.practitioner.model.PractitionerDetails;
import uk.gov.hscic.practitioner.model.PractitionerEntity;
import uk.gov.hscic.practitioner.repo.PractitionerRepository;

@Service
public class LegacyPractitionerSearch extends AbstractLegacyService implements PractitionerSearch {

    @Autowired
    private PractitionerRepository practitionerRepository;

    private final PractitionerEntityToObjectTransformer transformer = new PractitionerEntityToObjectTransformer();

    @Override
    public PractitionerDetails findPractitionerDetails(final String practitionerId) {

        final PractitionerEntity item = practitionerRepository.findOne(Long.parseLong(practitionerId));

        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }
}
