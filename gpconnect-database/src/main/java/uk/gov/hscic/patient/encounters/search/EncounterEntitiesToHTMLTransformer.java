package uk.gov.hscic.patient.encounters.search;

import java.util.List;
import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;

public class EncounterEntitiesToHTMLTransformer implements Transformer<List<EncounterEntity>, EncounterListHTML> {

    @Override
    public EncounterListHTML transform(final List<EncounterEntity> encounterEntity) {
        final EncounterListHTML encounterList = new EncounterListHTML();

        encounterList.setSourceId(String.valueOf(encounterEntity.get(0).getId()));
        encounterList.setSource(RepoSourceType.LEGACY.getSourceName());

        encounterList.setProvider(encounterEntity.get(0).getProvider());
        
        //Build HTML
        String html = "<div>"; // Opening tag
        for(EncounterEntity encounter : encounterEntity){
            html = html + encounter.getHtmlPart(); // Add content
        }
        html = html + "</div>"; // Closing tag
        encounterList.setHtml(html);

        encounterList.setLastUpdated(encounterEntity.get(0).getLastUpdated());
        return encounterList;
    }
}
