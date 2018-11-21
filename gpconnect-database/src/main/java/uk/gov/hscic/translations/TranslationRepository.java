package uk.gov.hscic.translations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<TranslationEntity, Long> {

	TranslationEntity getTranslationById(Long id);
}
