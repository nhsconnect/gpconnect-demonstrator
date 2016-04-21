package uk.gov.hscic.patient.allergies.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;

public class AllergyEntityToListTransformer implements Transformer<AllergyEntity, AllergyListHTML> {

    @Override
    public AllergyListHTML transform(AllergyEntity allergyEntity) {
        final AllergyListHTML allergyList = new AllergyListHTML();

        allergyList.setSourceId(String.valueOf(allergyEntity.getId()));
        allergyList.setSource(RepoSourceType.LEGACY.getSourceName());

        allergyList.setProvider(allergyEntity.getProvider());
        allergyList.setHtml(allergyEntity.getHtml());

        return allergyList;
    }
}
