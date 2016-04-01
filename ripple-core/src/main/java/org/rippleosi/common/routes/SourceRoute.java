package org.rippleosi.common.routes;

import org.apache.camel.builder.RouteBuilder;;

public class SourceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {	
		// Allergy Routes
		from("activemq:topic:VirtualTopic.Ripple.Allergies.Create")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Allergies.Create")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Allergies.Create");
		from("activemq:topic:VirtualTopic.Ripple.Allergies.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Allergies.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Allergies.Update");
		
		// Appointment Routes
		from("activemq:topic:VirtualTopic.Ripple.Appointment.Create")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Appointment.Create")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Appointment.Create");
		from("activemq:topic:VirtualTopic.Ripple.Appointment.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Appointment.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Appointment.Update");
		
		// Care Plan Routes
		from("activemq:topic:VirtualTopic.Ripple.CarePlan.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.CarePlan.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.CarePlan.Create");
		from("activemq:topic:VirtualTopic.Ripple.CarePlan.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.CarePlan.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.CarePlan.Update");
	
		// Contact Routes
		from("activemq:topic:VirtualTopic.Ripple.Contacts.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Contacts.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.Contacts.Create");
		from("activemq:topic:VirtualTopic.Ripple.Contacts.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Contacts.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Contacts.Update");
		
		// Lab Order Routes
		from("activemq:topic:VirtualTopic.Ripple.LabOrder.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.LabOrder.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.LabOrder.Create");

		// MDT Report Routes
		from("activemq:topic:VirtualTopic.Ripple.MDTReport.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.MDTReport.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.MDTReport.Create");
		from("activemq:topic:VirtualTopic.Ripple.MDTReport.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.MDTReport.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.MDTReport.Update");
		
		// Medication Routes
		from("activemq:topic:VirtualTopic.Ripple.Medication.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Medication.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.Medication.Create");
		from("activemq:topic:VirtualTopic.Ripple.Medication.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Medication.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Medication.Update");
		
		// Problem Routes
		from("activemq:topic:VirtualTopic.Ripple.Problems.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Problems.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.Problems.Create");
		from("activemq:topic:VirtualTopic.Ripple.Problems.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Problems.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Problems.Update");
		
		// Procedure Routes
		from("activemq:topic:VirtualTopic.Ripple.Procedures.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Procedures.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.Procedures.Create");
		from("activemq:topic:VirtualTopic.Ripple.Procedures.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Procedures.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Procedures.Update");
		
		// Referral Routes
		from("activemq:topic:VirtualTopic.Ripple.Referrals.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Referrals.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.Referrals.Create");
		from("activemq:topic:VirtualTopic.Ripple.Referrals.Update")
			.choice()
				.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Referrals.Update")
			.otherwise()
				.to("activemq:topic:VirtualTopic.Marand.Referrals.Update");
		
		// Transfer Routes
		from("activemq:topic:VirtualTopic.Ripple.Transfers.Create")
		.choice()
			.when().simple("${body.args[1].source} == 'EtherCIS'").to("activemq:topic:VirtualTopic.EtherCIS.Transfers.Create")
		.otherwise()
			.to("activemq:topic:VirtualTopic.Marand.Transfers.Create");

		
		}

}
