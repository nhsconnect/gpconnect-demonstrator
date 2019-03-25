package uk.gov.hscic.metadata;

import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider;

public class GpConnectServerConformanceProvider extends ServerConformanceProvider {
    
	// **Change this for upgrade version**
    public static final Version VERSION = new Version("0.5.0");

	public GpConnectServerConformanceProvider(RestfulServer theRestfulServer) {
		super(theRestfulServer);
	}
	
	@Override
	public Conformance getServerConformance(HttpServletRequest theRequest) {
		//Get the automatically generated statement
		Conformance conformance = super.getServerConformance(theRequest);
		//And add additional required information
		conformance.setVersion(VERSION.toString());
		conformance.setDescription("This server implements the GP Connect API version "+ VERSION);
		conformance.setName("GP Connect");
		conformance.setCopyright("Copyright NHS Digital 2018");
		DateTimeDt release = new DateTimeDt();
		release.setValue(Date.valueOf(LocalDate.parse("2016-12-20"))); //HAPI FHIR release date
		conformance.getSoftware().setReleaseDate(release);
		return conformance;
	}
	
}
