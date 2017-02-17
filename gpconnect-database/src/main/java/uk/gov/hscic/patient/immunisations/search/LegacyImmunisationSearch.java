package uk.gov.hscic.patient.immunisations.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.immunisations.model.ImmunisationData;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;
import uk.gov.hscic.patient.immunisations.repo.ImmunisationRepository;

@Service
public class LegacyImmunisationSearch extends AbstractLegacyService implements ImmunisationSearch {

    @Autowired
    private ImmunisationRepository immunisationRepository;

    @Override
    public List<ImmunisationData> findAllImmunisationHTMLTables(final String patientId) {

        List<ImmunisationEntity> items = immunisationRepository.findBynhsNumber(patientId);

        List<ImmunisationData> immunisationList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            ImmunisationData immunisationData = new ImmunisationData();
            immunisationData.setDateOfVac(items.get(i).getDateOfVac());
            immunisationData.setVaccination(items.get(i).getVaccination());
            immunisationData.setPart(items.get(i).getPart());
            immunisationData.setContents(items.get(i).getContents());
            immunisationData.setDetails(items.get(i).getDetails());
            immunisationList.add(immunisationData);
        }

        return immunisationList;

    }
}
