package org.rippleosi.medical.department.repo;

import org.rippleosi.medical.department.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

}
