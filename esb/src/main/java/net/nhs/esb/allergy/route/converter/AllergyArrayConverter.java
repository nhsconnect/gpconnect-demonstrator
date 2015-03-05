package net.nhs.esb.allergy.route.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.allergy.model.AllergyArray;
import net.nhs.esb.rest.domain.QueryResponseData;
import org.apache.camel.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class AllergyArrayConverter {

    private static final Logger log = LoggerFactory.getLogger(AllergyArrayConverter.class);

    @Converter
    public AllergyArray convertAQLResponseToAllergyArray(QueryResponseData response) {
        AllergyTransformer transformer = new AllergyTransformer();
        List<Allergy> allergyList = CollectionUtils.collect(response.getResultSet(), transformer, new ArrayList<Allergy>());

        AllergyArray allergyArray = new AllergyArray();
        allergyArray.setAllergies(allergyList);

        return allergyArray;
    }

    private static class AllergyTransformer implements Transformer<Map<String,Object>, Allergy> {

        @Override
        public Allergy transform(Map<String, Object> result) {
            Allergy allergy = new Allergy();

            try {
                PropertyUtils.copyProperties(allergy, result);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                log.error(ex.getMessage(), ex);
            }

            return allergy;
        }
    }
}
