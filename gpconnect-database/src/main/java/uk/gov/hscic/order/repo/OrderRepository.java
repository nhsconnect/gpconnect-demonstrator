package uk.gov.hscic.order.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hscic.order.model.OrderEntity;

@Transactional
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findBySubjectPatientId(Long patientId);

}
