package uk.gov.hscic.appointment.slot;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.model.appointment.SlotDetail;
import uk.gov.hscic.organization.OrganizationEntity;

public class SlotEntityToSlotDetailTransformer implements Transformer<SlotEntity, SlotDetail> {

    @Override
    public SlotDetail transform(SlotEntity item) {
        SlotDetail slotDetail = new SlotDetail();
        slotDetail.setId(item.getId());

        slotDetail.setTypeCode(item.getTypeCode());
        slotDetail.setTypeDisply(item.getTypeDisply());
        slotDetail.setScheduleReference(item.getScheduleReference());
        slotDetail.setFreeBusyType(item.getFreeBusyType());
        slotDetail.setStartDateTime(item.getStartDateTime());
        slotDetail.setEndDateTime(item.getEndDateTime());
        slotDetail.setLastUpdated(item.getLastUpdated());
        slotDetail.setGpConnectBookable(item.isGpConnectBookable());
        slotDetail.setDeliveryChannelCode(item.getDeliveryChannelCode());
        
        List<OrganizationEntity> organizations = item.getBookableOrganizations();
        ArrayList<Long> al = new ArrayList<>();
        for (OrganizationEntity organizationEntity : organizations) {
            al.add(organizationEntity.getId());
        }
        slotDetail.setOrganizationIds(al);
        slotDetail.setOrganizationTypes(item.getBookableOrgTypes());
        return slotDetail;
    }
}