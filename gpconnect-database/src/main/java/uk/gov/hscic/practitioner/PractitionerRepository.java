package uk.gov.hscic.practitioner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PractitionerRepository extends JpaRepository<PractitionerEntity, Long> {
    PractitionerEntity findByuserid(String practitionerUserId);
}
