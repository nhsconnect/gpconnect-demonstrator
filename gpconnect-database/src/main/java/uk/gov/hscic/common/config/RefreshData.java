package uk.gov.hscic.common.config;

import com.google.common.io.Files;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hscic.appointment.appointment.AppointmentStore;
import uk.gov.hscic.appointment.schedule.ScheduleStore;
import uk.gov.hscic.appointment.slot.SlotStore;
import uk.gov.hscic.model.appointment.AppointmentDetail;
import uk.gov.hscic.model.appointment.ScheduleDetail;
import uk.gov.hscic.model.appointment.SlotDetail;
import uk.gov.hscic.patient.details.PatientEntity;
import uk.gov.hscic.patient.details.PatientStore;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import uk.gov.hscic.model.appointment.BookingOrgDetail;

@Service
public class RefreshData {
    
    private static final int SLOT_FIELDS = 15;
    
    private static final int SLOT_INDEX_DAY_OFFSET = 0;
    private static final int SLOT_INDEX_START_H = 1;
    private static final int SLOT_INDEX_START_M = 2;
    private static final int SLOT_INDEX_START_S = 3;
    private static final int SLOT_INDEX_END_H = 4;
    private static final int SLOT_INDEX_END_M = 5;
    private static final int SLOT_INDEX_END_S = 6;
    private static final int SLOT_INDEX_SLOT_TYPE_CODE = 7;
    private static final int SLOT_INDEX_DESCRIPTION = 8;
    private static final int SLOT_INDEX_PRACTITIONER_ID = 9;
    private static final int SLOT_INDEX_FREE_BUSY = 10;
    private static final int SLOT_INDEX_BOOKABLE = 11;
    private static final int SLOT_INDEX_ORG_ID = 12;
    private static final int SLOT_INDEX_ORG_TYPE = 13;

    private static final Logger LOG = Logger.getLogger(RefreshData.class);

    @Value("${config.path}")
    private String configPath;

    @Value("${datasource.refresh.slots.filename}")
    private String slotsFilename;

    @Value("${datasource.patients:#{null}}")
    private String[] patients;

    @Autowired
    private AppointmentStore appointmentStore;

    @Autowired
    private SlotStore slotStore;

    @Autowired
    private PatientStore patientStore;

    @Autowired
    private ScheduleStore scheduleStore;

    // Overnight cleardown of test data
    @Scheduled(cron = "${datasource.cleardown.cron}")
    public void scheduledResetOfData() {
        resetAppointments();
    }

    private void resetAppointments() {

        appointmentStore.clearAppointments();
        slotStore.clearSlots();

        try {
            List<String> lines = Files.readLines(new File(configPath + slotsFilename), StandardCharsets.UTF_8);

            for (String line : lines) {
                String[] element = line.split(",");
                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(element[SLOT_INDEX_DAY_OFFSET]));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(element[SLOT_INDEX_START_H]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(element[SLOT_INDEX_START_M]));
                calendar.set(Calendar.SECOND, Integer.parseInt(element[SLOT_INDEX_START_S]));
                calendar.set(Calendar.MILLISECOND, 0);
                Date startDate = calendar.getTime();

                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(element[SLOT_INDEX_DAY_OFFSET]));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(element[SLOT_INDEX_END_H]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(element[SLOT_INDEX_END_M]));
                calendar.set(Calendar.SECOND, Integer.parseInt(element[SLOT_INDEX_END_S]));
                calendar.set(Calendar.MILLISECOND, 0);
                Date endDate = calendar.getTime();

                // handle trailing comma on last entry
                String deliveryChannelCode = element.length >= SLOT_FIELDS ? element[SLOT_FIELDS-1] : "";
                // 0 number of days to add to today
                // 1 start hours
                // 2 start minutes
                // 3 start seconds
                // 4 end hours
                // 5 end minutes
                // 6 end seconds
                // 7 Slot type code long eg 408443003
                // 8 Slot type description/display string eg General Medical Practice
                // 9 Practitioner internal id /schedule reference eg 2
                // 10 Slot Status FREE/BUSY string
                // 11 boolean gp connect bookable boolean
                // 12 organization id int 1 or 2
                // 13 organization type String eg Urgent care
                // 14 sequence of PVT delivery channel codes String P In-person, T Telephone, V Video eg TVP

                // The Collections.singletonList idiom is a way of converting a single item into a list containing one item
                // 1.2.7 allow null service type and code
                Long slotType = null;
                try {
                    slotType = Long.parseLong(element[SLOT_INDEX_SLOT_TYPE_CODE]);
                } catch (NumberFormatException ex) {
                }
                slotStore.saveSlot(createSlot(slotType,
                        element[SLOT_INDEX_DESCRIPTION],
                        Long.parseLong(element[SLOT_INDEX_PRACTITIONER_ID]),
                        element[SLOT_INDEX_FREE_BUSY],
                        startDate,
                        endDate,
                        currentDate,
                        Boolean.parseBoolean(element[SLOT_INDEX_BOOKABLE]),
                        element[SLOT_INDEX_ORG_ID].isEmpty() ? Collections.EMPTY_LIST : Collections.singletonList(Long.parseLong(element[SLOT_INDEX_ORG_ID])),
                        element[SLOT_INDEX_ORG_TYPE].trim().isEmpty() ? Collections.EMPTY_LIST : Collections.singletonList(element[SLOT_INDEX_ORG_TYPE].trim()),
                        deliveryChannelCode));
                
            }
        } catch (IOException e) {
            LOG.error("Error reading slots file", e);
        }

        try {
            List<SlotDetail> slots = slotStore.findAllSlots();
            if (slots.size() > 2) {
                AppointmentDetail appointment = createAppointment(slots.get(0), "A appointment to discuss test data");
                appointmentStore.saveAppointment(appointment, Collections.singletonList(slots.get(0)));

                AppointmentDetail appointment2 = createAppointment(slots.get(1), "A follow-up appointment for tests.");
                appointmentStore.saveAppointment(appointment2, Collections.singletonList(slots.get(1)));
            }
        } catch (Exception e) {
            LOG.error("Error booking test appointments", e);
        }
    }

    private SlotDetail createSlot(Long typeCode, String typeDisplay, long scheduleReference, String freeBusy, Date startDate, Date endDate, Date lastUpdated,
            boolean gpConnectBookable, List<Long> organizationIds, List<String> organizationTypes, String deliveryChannelCode) {
        SlotDetail slot = new SlotDetail();
        slot.setTypeCode(typeCode);
        slot.setTypeDisply(typeDisplay);
        slot.setScheduleReference(scheduleReference);
        slot.setFreeBusyType(freeBusy);
        slot.setStartDateTime(startDate);
        slot.setEndDateTime(endDate);
        slot.setLastUpdated(lastUpdated);
        slot.setGpConnectBookable(gpConnectBookable);
        slot.setOrganizationTypes(organizationTypes);
        slot.setOrganizationIds(organizationIds);
        String deliveryChannel = "";
        if (deliveryChannelCode != null && !deliveryChannelCode.trim().isEmpty()) {
            switch (deliveryChannelCode) {
                case "P":
                    deliveryChannel = "In-person";
                    break;
                case "T":
                    deliveryChannel = "Telephone";
                    break;
                case "V":
                    deliveryChannel = "Video";
                    break;
                default:
                    LOG.error("Unrecognised delivery channel code "+deliveryChannelCode);
            }
        }
        slot.setDeliveryChannelCode(deliveryChannel);
        return slot;
    }

    /**
     * only used to set up the first two appointments on start up
     * @param slot
     * @param description
     * @return 
     */
    private AppointmentDetail createAppointment(SlotDetail slot, String description) {
        AppointmentDetail appointment = new AppointmentDetail();
        appointment.setDescription(description);
        appointment.setStartDateTime(slot.getStartDateTime());
        appointment.setEndDateTime(slot.getEndDateTime());
        appointment.setStatus("booked");
        appointment.setPriority(0);
        appointment.setMinutesDuration(10);

        // beware spring does not strip trailing blanks from property values
        PatientEntity patient = patientStore.findByNhsNumber(patients[2].trim());
        appointment.setPatientId(patient.getId());

        ScheduleDetail schedule = scheduleStore.findSchedule(slot.getScheduleReference());
        appointment.setPractitionerId(schedule.getPractitionerId());
        appointment.setLocationId(schedule.getLocationId());

        appointment.setLastUpdated(new Date());
        
        BookingOrgDetail bookingOrgDetail = new BookingOrgDetail();
        bookingOrgDetail.setOrgCode("B82617");
        bookingOrgDetail.setName("COXWOLD Surgery");
        bookingOrgDetail.setTelephone("0300 303 5678");
        bookingOrgDetail.setSystem("PHONE");
        appointment.setBookingOrganization(bookingOrgDetail);

        return appointment;
    }
}
