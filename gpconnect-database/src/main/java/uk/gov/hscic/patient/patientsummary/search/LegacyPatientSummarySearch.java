package uk.gov.hscic.patient.patientsummary.search;

import java.util.Collections;
import java.util.List;

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

    private final PatientSummaryEntityToListTransformer transformer = new PatientSummaryEntityToListTransformer();

    @Override
    public List<PatientSummaryListHTML> findAllPatientSummaryHTMLTables(String patientId) {

        final PatientSummaryEntity item = patientSummaryRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }

}
