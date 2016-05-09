package uk.gov.hscic.patient.keyindicators.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.keyindicator.model.KeyIndicatorListHTML;
import uk.gov.hscic.patient.keyindicator.search.KeyIndicatorSearch;
import uk.gov.hscic.patient.keyindicators.model.KeyIndicatorEntity;
import uk.gov.hscic.patient.keyindicators.repo.KeyIndicatorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegacykeyIndicatorSearch extends AbstractLegacyService implements KeyIndicatorSearch {

    @Autowired
    private KeyIndicatorRepository keyIndicatorRepository;

    @Override
    public List<KeyIndicatorListHTML> findAllKeyIndicatorHTMLTables(final String patientId) {
        final List<KeyIndicatorEntity> keyIndicators = keyIndicatorRepository.findAll();

        return CollectionUtils.collect(keyIndicators, new KeyIndicatorEntityToListTransformer(), new ArrayList<>());
    }
}
