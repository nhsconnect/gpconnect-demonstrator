package uk.gov.hscic.patient.procedures.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;
import uk.gov.hscic.patient.procedures.model.ProcedureEntity;
import uk.gov.hscic.patient.procedures.repo.ProcedureRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegacyProcedureSearch extends AbstractLegacyService implements ProcedureSearch {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Override
    public List<ProcedureListHTML> findAllProceduresHTMLTables(final String patientId) {
        final List<ProcedureEntity> procedureLists = procedureRepository.findAll();

        return CollectionUtils.collect(procedureLists, new ProcedureEntityToListTransformer(), new ArrayList<>());
    }
}
