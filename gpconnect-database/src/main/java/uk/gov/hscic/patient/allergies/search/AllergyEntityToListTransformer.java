package uk.gov.hscic.patient.allergies.search;

import java.util.List;

import org.apache.commons.collections4.Transformer;

import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.model.AllergyData;

public class AllergyEntityToListTransformer implements Transformer<List<AllergyEntity>, AllergyData> {

    @Override
    public AllergyData transform(final List<AllergyEntity> items) {
        final AllergyData allergyList = new AllergyData();

        for (int i = 0; i < items.size(); i++) {

            allergyList.setSourceId(String.valueOf(items.get(i).getNhsNumber()));

            allergyList.setCurrentOrHistoric(items.get(i).getCurrentOrHistoric());
            allergyList.setStartDate(items.get(i).getStartDate());
            allergyList.setEndDate(items.get(i).getEndDate());
            allergyList.setDetails(items.get(i).getDetails());

        }

        return allergyList;
    }

}
