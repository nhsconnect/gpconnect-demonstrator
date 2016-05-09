package uk.gov.hscic.patient.clinicalitems.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.repo.ClinicalItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegacyClinicalItemSearch extends AbstractLegacyService implements ClinicalItemSearch {

    @Autowired
    private ClinicalItemRepository clinicalItemRepository;

    @Override
    public List<ClinicalItemListHTML> findAllClinicalItemHTMLTables(final String patientId) {
        final List<ClinicalItemEntity> clinicalItems = clinicalItemRepository.findAll();

        return CollectionUtils.collect(clinicalItems, new ClinicalItemEntityToListTransformer(), new ArrayList<>());
    }
}
