package uk.gov.hscic.patient.details.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.common.exception.DataNotFoundException;
import uk.gov.hscic.patient.details.model.PatientEntity;
import uk.gov.hscic.patient.problems.model.ProblemHeadline;
import uk.gov.hscic.patient.problems.search.ProblemSearch;
import uk.gov.hscic.patient.problems.search.ProblemSearchFactory;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.model.PatientHeadline;

@Component
public class PatientEntityToDetailsTransformer implements Transformer<PatientEntity, PatientDetails> {

    @Autowired
    private ProblemSearchFactory problemSearchFactory;

    @Override
    public PatientDetails transform(PatientEntity patientEntity) {
        PatientDetails patient = new PatientDetails();

        String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();

        Collection<String> addressList = Arrays.asList(StringUtils.trimToNull(patientEntity.getAddress1()),
                                                       StringUtils.trimToNull(patientEntity.getAddress2()),
                                                       StringUtils.trimToNull(patientEntity.getAddress3()),
                                                       StringUtils.trimToNull(patientEntity.getPostcode()));

        addressList = CollectionUtils.removeAll(addressList, Collections.singletonList(null));

        String address = StringUtils.join(addressList, ", ");

        String patientId = patientEntity.getNhsNumber();

        patient.setId(patientId);
        patient.setName(name);
        patient.setGender(patientEntity.getGender());
        patient.setDateOfBirth(patientEntity.getDateOfBirth());
        patient.setNhsNumber(patientId);
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setAddress(address);
        patient.setTelephone(patientEntity.getPhone());
        patient.setGpDetails(patientEntity.getGp().getName());
        patient.setPasNumber(patientEntity.getPasNumber());

        patient.setProblems(findProblems(patientId));

        return patient;
    }

    private List<PatientHeadline> findProblems(String patientId) {
        try {
            ProblemSearch problemSearch = problemSearchFactory.select(null);

            List<ProblemHeadline> problems = problemSearch.findProblemHeadlines(patientId);

            return CollectionUtils.collect(problems, new ProblemTransformer(), new ArrayList<>());
        } catch (DataNotFoundException ignore) {
            return Collections.emptyList();
        }
    }
}
