package net.nhs.esb.medication.route.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.medication.model.MedicationArray;
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
public class MedicationArrayConverter {

    private static final Logger log = LoggerFactory.getLogger(MedicationArrayConverter.class);

    @Converter
    public MedicationArray convertAQLResponseToMedicationArray(QueryResponseData response) {
        MedicationTransformer transformer = new MedicationTransformer();
        List<Medication> medicationList = CollectionUtils.collect(response.getResultSet(), transformer, new ArrayList<Medication>());

        MedicationArray medicationArray = new MedicationArray();
        medicationArray.setMedications(medicationList);

        return medicationArray;
    }

    private static class MedicationTransformer implements Transformer<Map<String, Object>, Medication> {

        @Override
        public Medication transform(Map<String, Object> result) {
            Medication medication = new Medication();

            try {
                PropertyUtils.copyProperties(medication, result);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                log.error(ex.getMessage(), ex);
            }

            return medication;
        }
    }
}
