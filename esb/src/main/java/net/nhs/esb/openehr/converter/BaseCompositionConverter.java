package net.nhs.esb.openehr.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class BaseCompositionConverter {

    private static final Logger log = LoggerFactory.getLogger(BaseCompositionConverter.class);

    protected BaseCompositionConverter() {
        // This class should be extended
    }

    /**
     * This method is needed because PropertyUtils.getProperty() cannot handle maps with lists.
     */
    protected <T> T getProperty(Object object, String property) {

        String[] propertyList = property.split("\\.");

        Object currentObject = object;
        for (String s : propertyList) {
            if (s.endsWith("]")) {
                int index = Integer.parseInt(StringUtils.substringBefore(StringUtils.substringAfter(s, "["), "]"));
                s = StringUtils.substringBefore(s, "[");

                currentObject = resolveIndexed(currentObject, s, index);
            } else {
                currentObject = resolve(currentObject, s);
            }
        }

        return (T)currentObject;
    }

    private <T> T resolve(Object object, String property) {

        try {
            return (T)PropertyUtils.getProperty(object, property);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    private <T> T resolveIndexed(Object object, String property, int index) {

        List<T> objectList = resolve(object, property);

        if (CollectionUtils.isEmpty(objectList)) {
            return null;
        }

        return objectList.get(index);
    }

    protected String extractCompositionId(Map<String, Object> composition) {
        String completeUid = getProperty(composition, "_uid[0]");

        return StringUtils.substringBefore(completeUid, "::");
    }

    protected <T> List<T> extractCompositionData(Map<String,Object> composition, String property, Transformer<Map<String,Object>, T> transformer) {

        List<Map<String,Object>> rawDataList = getProperty(composition, property);

        return org.apache.commons.collections4.CollectionUtils.collect(rawDataList, transformer, new ArrayList<T>());
    }
}
