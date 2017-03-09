package uk.gov.hscic.patient.procedures.search;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.procedures.model.ProcedureEntity;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;
import uk.gov.hscic.patient.procedures.repo.ProcedureRepository;

@Service
public class ProcedureSearch {
    private final ProcedureEntityToListTransformer transformer = new ProcedureEntityToListTransformer();

    @Autowired
    private ProcedureRepository procedureRepository;

    public List<ProcedureListHTML> findAllProceduresHTMLTables(final String patientId) {
        final ProcedureEntity item = procedureRepository.findOne(Long.parseLong(patientId));

        return item == null
                ? null
                : Collections.singletonList(transformer.transform(item));
    }
}
