package uk.gov.hscic.metadata;

import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import ca.uhn.fhir.rest.server.RestfulServer;
import uk.gov.hscic.SystemVariable;

public class GpConnectServerCapabilityStatementProvider extends ServerCapabilityStatementProvider {

	public GpConnectServerCapabilityStatementProvider(RestfulServer theRestfulServer) {
		super(theRestfulServer);
	}
	
	@Override
	public CapabilityStatement getServerConformance(HttpServletRequest theRequest) {
		//Get the automatically generated statement
		CapabilityStatement capabilityStatement = super.getServerConformance(theRequest);
		//And add additional required information
		capabilityStatement.setVersion(SystemVariable.VERSION);
		capabilityStatement.setDescription("This server implements the GP Connect API version " + SystemVariable.VERSION);
		capabilityStatement.setName("GP Connect");
		capabilityStatement.setCopyright("Copyright NHS Digital 2018");
		capabilityStatement.getSoftware().setReleaseDate(Date.valueOf(LocalDate.parse("2017-09-27")));
		return capabilityStatement;
	}
	
}
