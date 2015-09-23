package org.rippleosi.patient.terminology.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.terminology.model.Terminology;

public interface TerminologySearch extends Repository {

    List<Terminology> findTerms(String type);
}
