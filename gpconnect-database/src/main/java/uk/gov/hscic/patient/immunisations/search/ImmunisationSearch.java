package uk.gov.hscic.patient.immunisations.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.immunisations.model.ImmunisationData;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;
import uk.gov.hscic.patient.immunisations.repo.ImmunisationRepository;

@Service
public class ImmunisationSearch {

    @Autowired
    private ImmunisationRepository immunisationRepository;

    public List<ImmunisationData> findAllImmunisationHTMLTables(final String patientId) {
        List<ImmunisationData> immunisationList = new ArrayList<>();

        for (ImmunisationEntity immunisationEntity : immunisationRepository.findBynhsNumber(patientId)) {
            ImmunisationData immunisationData = new ImmunisationData();
            immunisationData.setDateOfVac(immunisationEntity.getDateOfVac());
            immunisationData.setVaccination(immunisationEntity.getVaccination());
            immunisationData.setPart(immunisationEntity.getPart());
            immunisationData.setContents(immunisationEntity.getContents());
            immunisationData.setDetails(immunisationEntity.getDetails());
            immunisationList.add(immunisationData);
        }

        return immunisationList;
    }
}
