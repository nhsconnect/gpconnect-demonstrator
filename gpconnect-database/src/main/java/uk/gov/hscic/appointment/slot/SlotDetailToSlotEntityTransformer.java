package uk.gov.hscic.appointment.slot;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Transformer;

import uk.gov.hscic.model.appointment.SlotDetail;
import uk.gov.hscic.organization.OrganizationEntity;

public class SlotDetailToSlotEntityTransformer implements Transformer<SlotDetail, SlotEntity> {

    @Override
    public SlotEntity transform(SlotDetail item) {
        SlotEntity slotEntity = new SlotEntity();
        slotEntity.setId(item.getId());

//        if (item.getAppointmentId() != null) {
//            AppointmentEntity appointment = new AppointmentEntity();
//            appointment.setId(item.getAppointmentId());
//            slotEntity.setAppointmentId(appointment);
//        }

        slotEntity.setTypeCode(item.getTypeCode());
        slotEntity.setTypeDisply(item.getTypeDisply());
        slotEntity.setScheduleReference(item.getScheduleReference());
        slotEntity.setFreeBusyType(item.getFreeBusyType());
        slotEntity.setStartDateTime(item.getStartDateTime());
        slotEntity.setEndDateTime(item.getEndDateTime());
        slotEntity.setLastUpdated(item.getLastUpdated());
        slotEntity.setGpConnectBookable(item.isGpConnectBookable());
        slotEntity.setBookableOrgTypes(item.getOrganizationTypes());
        if(item.getOrganizationIds() != null) {
        	List<OrganizationEntity> bookableOrganizations = item.getOrganizationIds().stream().map(id -> createOrganizationEntityWithId(id)).collect(Collectors.toList());
        	slotEntity.setBookableOrganizations(bookableOrganizations);
        }
        
        return slotEntity;
    }
    
    private OrganizationEntity createOrganizationEntityWithId(Long id) {
    	OrganizationEntity orgEnt = new OrganizationEntity();
    	orgEnt.setId(id);
    	return orgEnt;
    }
}
