package net.nhs.esb.openehr.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 */
public abstract class BaseCompositionConverter<T> {

    protected abstract T create(Map<String,Object> rawComposition, String prefix);
    protected abstract String dataDefinitionPrefix();

    protected BaseCompositionConverter() {
        // This class should be extended
    }

    protected List<T> extractCompositionData(Map<String,Object> rawComposition) {

        List<T> list = new ArrayList<>();

        int count = countDataEntries(rawComposition);
        for (int i = 0; i < count; i++) {

            String prefix = dataDefinitionPrefix() + i;

            T t = create(rawComposition, prefix);
            list.add(t);
        }

        return list;
    }

    private int countDataEntries(Map<String, Object> rawComposition) {

        String prefix = dataDefinitionPrefix();
        int maxEntry = -1;
        for (String key : rawComposition.keySet()) {
            if (StringUtils.startsWith(key, prefix)) {
                String index = StringUtils.substringAfter(key, prefix);
                index = StringUtils.substringBefore(index, "/");

                maxEntry = Math.max(maxEntry, Integer.parseInt(index));
            }
        }

        return maxEntry + 1;

    }
}
