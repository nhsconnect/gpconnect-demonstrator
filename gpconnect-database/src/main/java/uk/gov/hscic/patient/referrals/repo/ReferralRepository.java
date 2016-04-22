package uk.gov.hscic.patient.referrals.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;

public interface ReferralRepository extends JpaRepository<ReferralEntity, Long> {

}
