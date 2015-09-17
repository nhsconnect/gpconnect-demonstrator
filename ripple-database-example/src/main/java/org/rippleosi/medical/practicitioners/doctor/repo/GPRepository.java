package org.rippleosi.medical.practicitioners.doctor.repo;

import org.rippleosi.medical.practicitioners.doctor.model.GPEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPRepository extends JpaRepository<GPEntity, Long> {

}
