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
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class RefreshData {

    private static final Logger LOG = Logger.getLogger(RefreshData.class);

    @Value("${config.path}")
    private String configPath;

    @Value("${datasource.refresh.slots.filename}")
    private String slotsFilename;

    @Value("${datasource.patient.nhsNumber}")
    private String nhsNumber;

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
                Date startDate = DateUtils.addDays(currentDate, Integer.parseInt(element[0]));
                Date endDate = DateUtils.addDays(currentDate, Integer.parseInt(element[0]));
                startDate.setHours(Integer.parseInt(element[1]));
                startDate.setMinutes(Integer.parseInt(element[2]));
                startDate.setSeconds(Integer.parseInt(element[3]));
                endDate.setHours(Integer.parseInt(element[4]));
                endDate.setMinutes(Integer.parseInt(element[5]));
                endDate.setSeconds(Integer.parseInt(element[6]));

                // handle trailing comma on last entry
                String deliveryChannelCodes = element.length >= 15 ? element[14] : "";
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
                slotStore.saveSlot(createSlot(Long.parseLong(element[7]), element[8], Long.parseLong(element[9]), element[10], startDate, endDate, currentDate,
                        Boolean.parseBoolean(element[11]), Collections.singletonList(Long.parseLong(element[12])), Collections.singletonList(element[13]), deliveryChannelCodes));
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
            boolean gpConnectBookable, List<Long> organizationIds, List<String> organizationTypes, String deliveryChannelCodes) {
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
        if (deliveryChannelCodes != null && !deliveryChannelCodes.trim().isEmpty()) {
            switch (deliveryChannelCodes) {
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
                    System.err.println("Unrecognised delivery channel codes "+deliveryChannelCodes);
            }
        }
        slot.setDeliveryChannelCodes(deliveryChannel);
        return slot;
    }

    private AppointmentDetail createAppointment(SlotDetail slot, String description) {
        AppointmentDetail appointment = new AppointmentDetail();
        appointment.setDescription(description);
        appointment.setStartDateTime(slot.getStartDateTime());
        appointment.setEndDateTime(slot.getEndDateTime());
        appointment.setStatus("booked");
        appointment.setTypeText(slot.getDeliveryChannelCodes());
        appointment.setTypeCode(new Long(408443003));
        appointment.setTypeDisplay("General medical practice");
        appointment.setPriority(0);
        appointment.setMinutesDuration(10);

        PatientEntity patient = patientStore.findByNhsNumber(nhsNumber);
        appointment.setPatientId(patient.getId());

        ScheduleDetail schedule = scheduleStore.findSchedule(slot.getScheduleReference());
        appointment.setPractitionerId(schedule.getPractitionerId());
        appointment.setLocationId(schedule.getLocationId());

        appointment.setLastUpdated(new Date());

        return appointment;
    }
}
