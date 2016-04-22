package uk.gov.hscic.patient.problems.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.problems.model.ProblemEntity;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;

public class ProblemEntityToListTransformer implements Transformer<ProblemEntity, ProblemListHTML> {

    @Override
    public ProblemListHTML transform(ProblemEntity problemEntity) {
        final ProblemListHTML problemList = new ProblemListHTML();

        problemList.setSourceId(String.valueOf(problemEntity.getId()));
        problemList.setSource(RepoSourceType.LEGACY.getSourceName());

        problemList.setProvider(problemEntity.getProvider());
        problemList.setHtml(problemEntity.getHtml());

        return problemList;
    }
}
