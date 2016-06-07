package uk.gov.hscic.patient.practitioner.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.practitioner.model.PractitionerDetails;

public interface PractitionerSearch extends Repository {

    PractitionerDetails findPractitionerDetails(String practitionerId);
}
