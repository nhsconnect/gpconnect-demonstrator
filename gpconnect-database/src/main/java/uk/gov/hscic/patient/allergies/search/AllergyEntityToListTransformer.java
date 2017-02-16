package uk.gov.hscic.patient.allergies.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;

public class AllergyEntityToListTransformer implements Transformer<List<AllergyEntity>, AllergyListHTML> {

    @Override
    public AllergyListHTML transform(final List<AllergyEntity> items) {
        final AllergyListHTML allergyList = new AllergyListHTML();
      
        for (int i = 0 ; i< items.size() ; i++){
     
        allergyList.setSourceId(String.valueOf(items.get(i).getNhsNumber()));
        allergyList.setSource(RepoSourceType.LEGACY.getSourceName());

        allergyList.setProvider(items.get(i).getProvider());
        allergyList.setHtml(items.get(i).getHtml());
        allergyList.setCurrentOrHistoric(items.get(i).getCurrentOrHistoric());
        allergyList.setStartDate(items.get(i).getStartDate());
        allergyList.setEndDate(items.get(i).getEndDate());
        allergyList.setDetails(items.get(i).getDetails());

        allergyList.setLastUpdated(items.get(i).getLastUpdated());
       }
        
        return allergyList;
    }

}

