package net.nhs.esb.transfer.repository;

import net.nhs.esb.transfer.model.Site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteRepository  extends JpaRepository<Site, Long> {

	@Query("select s from Site s where s.patientId=:patientId")
	public Site findSiteByPatientId(@Param("patientId") Long patientId);
}
