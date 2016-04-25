package uk.gov.hscic.patient.patientsummary.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hscic.common.service.AbstractLegacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryEntity;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;
import uk.gov.hscic.patient.patientsummary.repo.PatientSummaryRepository;

@Service
public class LegacyPatientSummarySearch extends AbstractLegacyService implements PatientSummarySearch {

    @Autowired
    private PatientSummaryRepository patientSummaryRepository;

    @Override
    public List<PatientSummaryListHTML> findAllPatientSummaryHTMLTables(String patientId) {
        List<PatientSummaryEntity> patientSummaryLists = patientSummaryRepository.findAll();

        return CollectionUtils.collect(patientSummaryLists, new PatientSummaryEntityToListTransformer(), new ArrayList<>());
    }

}
