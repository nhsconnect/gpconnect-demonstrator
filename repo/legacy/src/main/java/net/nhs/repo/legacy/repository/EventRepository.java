package net.nhs.repo.legacy.repository;

import java.util.List;

import net.nhs.legacy.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "legacyTransactionManager", readOnly = true)
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByPatientId(Integer patientId);

    Event findByEventIdAndPatientId(Integer eventId, Integer patientId);
}
