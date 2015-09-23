package org.rippleosi.patient.terminology.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.rippleosi.patient.terminology.model.Terminology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PropertySource("classpath:terminology.properties")
public class LocalTerminologySearch implements TerminologySearch {

    @Value("${local.terminology.priority:1000}")
    private int priority;

    @Autowired
    private Environment localTerminologyResources;

    @Override
    public String getSource() {
        return "local-terminology";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public List<Terminology> findTerms(String type) {
        String[] keyList = StringUtils.split(localTerminologyResources.getProperty(type + ".type"), ",");

        List<Terminology> terminologyList = new ArrayList<>();

        for (String key : keyList) {
            String code = localTerminologyResources.getProperty(key + ".code");
            String text = localTerminologyResources.getProperty(key + ".text");

            terminologyList.add(new Terminology(code, text));
        }

        return terminologyList;
    }
}
