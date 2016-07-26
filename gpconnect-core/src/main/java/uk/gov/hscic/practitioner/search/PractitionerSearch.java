package uk.gov.hscic.practitioner.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.practitioner.model.PractitionerDetails;

public interface PractitionerSearch extends Repository {

    PractitionerDetails findPractitionerDetails(String practitionerId);
    
    PractitionerDetails findPractitionerByUserId(String practitionerUserId);
}
