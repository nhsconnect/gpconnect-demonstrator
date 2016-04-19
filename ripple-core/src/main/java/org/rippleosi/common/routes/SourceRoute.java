package org.rippleosi.common.routes;

import org.apache.camel.builder.RouteBuilder;

public class SourceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Allergy Routes
        from("activemq:topic:VirtualTopic.Ripple.Allergies.Create").to("activemq:topic:VirtualTopic.Marand.Allergies.Create");
        from("activemq:topic:VirtualTopic.Ripple.Allergies.Update").to("activemq:topic:VirtualTopic.Marand.Allergies.Update");

        // Appointment Routes
        from("activemq:topic:VirtualTopic.Ripple.Appointment.Create").to("activemq:topic:VirtualTopic.Marand.Appointment.Create");
        from("activemq:topic:VirtualTopic.Ripple.Appointment.Update").to("activemq:topic:VirtualTopic.Marand.Appointment.Update");

        // Care Plan Routes
        from("activemq:topic:VirtualTopic.Ripple.CarePlan.Create").to("activemq:topic:VirtualTopic.Marand.CarePlan.Create");
        from("activemq:topic:VirtualTopic.Ripple.CarePlan.Update").to("activemq:topic:VirtualTopic.Marand.CarePlan.Update");

        // Lab Order Routes
        from("activemq:topic:VirtualTopic.Ripple.LabOrder.Create").to("activemq:topic:VirtualTopic.Marand.LabOrder.Create");

        // MDT Report Routes
        from("activemq:topic:VirtualTopic.Ripple.MDTReport.Create").to("activemq:topic:VirtualTopic.Marand.MDTReport.Create");
        from("activemq:topic:VirtualTopic.Ripple.MDTReport.Update").to("activemq:topic:VirtualTopic.Marand.MDTReport.Update");

        // Medication Routes
        from("activemq:topic:VirtualTopic.Ripple.Medication.Create").to("activemq:topic:VirtualTopic.Marand.Medication.Create");
        from("activemq:topic:VirtualTopic.Ripple.Medication.Update").to("activemq:topic:VirtualTopic.Marand.Medication.Update");

        // Problem Routes
        from("activemq:topic:VirtualTopic.Ripple.Problems.Create").to("activemq:topic:VirtualTopic.Marand.Problems.Create");
        from("activemq:topic:VirtualTopic.Ripple.Problems.Update").to("activemq:topic:VirtualTopic.Marand.Problems.Update");

        // Procedure Routes
        from("activemq:topic:VirtualTopic.Ripple.Procedures.Create").to("activemq:topic:VirtualTopic.Marand.Procedures.Create");
        from("activemq:topic:VirtualTopic.Ripple.Procedures.Update").to("activemq:topic:VirtualTopic.Marand.Procedures.Update");

        // Referral Routes
        from("activemq:topic:VirtualTopic.Ripple.Referrals.Create").to("activemq:topic:VirtualTopic.Marand.Referrals.Create");
        from("activemq:topic:VirtualTopic.Ripple.Referrals.Update").to("activemq:topic:VirtualTopic.Marand.Referrals.Update");

        // Transfer Routes
        from("activemq:topic:VirtualTopic.Ripple.Transfers.Create").to("activemq:topic:VirtualTopic.Legacy.Transfers.Create");
    }
}
