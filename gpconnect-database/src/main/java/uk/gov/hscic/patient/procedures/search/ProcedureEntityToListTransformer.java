package uk.gov.hscic.patient.procedures.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;
import uk.gov.hscic.patient.procedures.model.ProcedureEntity;

public class ProcedureEntityToListTransformer implements Transformer<ProcedureEntity, ProcedureListHTML> {

    @Override
    public ProcedureListHTML transform(final ProcedureEntity procedureEntity) {
        final ProcedureListHTML procedureList = new ProcedureListHTML();

        procedureList.setSourceId(String.valueOf(procedureEntity.getId()));
        procedureList.setSource(RepoSourceType.LEGACY.getSourceName());

        procedureList.setProvider(procedureEntity.getProvider());
        procedureList.setHtml(procedureEntity.getHtml());

        return procedureList;
    }
}
