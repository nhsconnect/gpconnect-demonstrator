package uk.gov.hscic.patient.clinicalitems.search;

import java.util.List;

import org.apache.commons.collections4.Transformer;

import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;

public class ClinicalItemEntityToHTMLTransformer implements Transformer<List<ClinicalItemEntity>, ClinicalItemListHTML>  {

    @Override
    public ClinicalItemListHTML transform(final List<ClinicalItemEntity> clinicalEntity) {
        final ClinicalItemListHTML clinicalList = new ClinicalItemListHTML();
        
        
        clinicalList.setSource(String.valueOf(clinicalEntity.get(0).getId()));
        clinicalList.setSource(RepoSourceType.LEGACY.getSourceName());
        
        clinicalList.setProvider(clinicalEntity.get(0).getProvider());
      //Build HTML
        String html = "<div>"; // Opening tag
        for(ClinicalItemEntity clinical : clinicalEntity){
            html = html + clinical.getHtmlPart(); // Add content
        }
        html = html + "</div>"; // Closing tag
        clinicalList.setHtml(html);

        clinicalList.setLastUpdated(clinicalEntity.get(0).getLastUpdated());
        return clinicalList;
    }
 
    
}
