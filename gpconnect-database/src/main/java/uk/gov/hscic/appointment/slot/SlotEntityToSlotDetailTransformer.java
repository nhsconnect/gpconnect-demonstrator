package uk.gov.hscic.appointment.slot;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.model.appointment.SlotDetail;

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
        slotDetail.setDeliveryChannelCodes(item.getDeliveryChannelCodes());
        
        return slotDetail;
    }
}