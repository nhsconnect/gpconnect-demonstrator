package net.nhs.esb.transfer.repository;

import net.nhs.esb.transfer.model.TransferOfCareComposition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferOfCareCompositionRepository extends JpaRepository<TransferOfCareComposition, Long> {

	@Query("select toc from TransferOfCareComposition toc where toc.compositionId=:compositionId")
	public TransferOfCareComposition findByCompositionId(@Param("compositionId") String compositionId);
}
