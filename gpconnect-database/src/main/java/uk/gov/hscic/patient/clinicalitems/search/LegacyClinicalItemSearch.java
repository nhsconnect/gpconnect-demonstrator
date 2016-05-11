package uk.gov.hscic.patient.clinicalitems.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.repo.ClinicalItemRepository;

import java.util.Collections;
import java.util.List;

@Service
public class LegacyClinicalItemSearch extends AbstractLegacyService implements ClinicalItemSearch {

    @Autowired
    private ClinicalItemRepository clinicalItemRepository;

    private final ClinicalItemEntityToListTransformer transformer = new ClinicalItemEntityToListTransformer();

    @Override
    public List<ClinicalItemListHTML> findAllClinicalItemHTMLTables(final String patientId) {

        final ClinicalItemEntity item = clinicalItemRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
