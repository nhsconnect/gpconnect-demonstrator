package net.nhs.esb.appointments.rest;

import net.nhs.esb.appointments.model.Appointment;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RestController
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/{patientId}/appointments")
public class AppointmentsController {

    @GET
    public List<Appointment> findAllPatientAppointmentCompositions(@PathParam("patientId") Long patientId) {
        return null;
    }

    @POST
    public void createPatientAppointmentsComposition(@PathParam("patientId") Long patientId,
                                                     Appointment appointmentsComposition) {
    }

    @PUT
    public void updatePatientAppointmentsComposition(@PathParam("patientId") Long patientId,
                                                     Appointment appointmentsComposition) {
    }
}
