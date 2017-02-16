package uk.gov.hscic.patient.adminitems.search;

import java.util.List;

import org.apache.commons.collections4.Transformer;

import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;


public class AdminItemEntityToHTMLTransformer implements Transformer<List<AdminItemEntity>,AdminItemListHTML > {

    @Override
    public AdminItemListHTML transform(final List<AdminItemEntity> adminEntity) {
        final AdminItemListHTML adminList = new AdminItemListHTML();
        
        adminList.setSource(String.valueOf(adminEntity.get(0).getId()));
        adminList.setSource(RepoSourceType.LEGACY.getSourceName());
        
        
        adminList.setProvider(adminEntity.get(0).getProvider());
        //Build HTML
        String html = "<div>"; // Opening tag
        for(AdminItemEntity admin : adminEntity){
            html = html + admin.getHtmlPart(); // Add content
        }
        html = html + "</div>"; // Closing tag
        adminList.setHtml(html);
        
        adminList.setLastUpdated(adminEntity.get(0).getLastUpdated());
        
        return adminList;
    }

}
