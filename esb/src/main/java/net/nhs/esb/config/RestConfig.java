package net.nhs.esb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.nhs.esb.allergy.rest.AllergyController;
import net.nhs.esb.appointments.rest.AppointmentsController;
import net.nhs.esb.cancermdt.rest.CancerMDTController;
import net.nhs.esb.endoflife.rest.EndOfLifeController;
import net.nhs.esb.lab.orders.rest.LabOrderController;
import net.nhs.esb.lab.results.rest.LabResultController;
import net.nhs.esb.mdtreport.rest.MDTReportController;
import net.nhs.esb.medication.rest.MedicationController;
import net.nhs.esb.patient.rest.PatientSearchController;
import net.nhs.esb.procedures.rest.ProceduresController;
import net.nhs.esb.referrals.rest.ReferralsController;
import net.nhs.esb.rest.OpenEhr;
import net.nhs.esb.rest.Patients;
import net.nhs.esb.transfer.rest.TransferOfCareController;
import org.apache.camel.component.cxf.spring.SpringJAXRSClientFactoryBean;
import org.apache.camel.component.cxf.spring.SpringJAXRSServerFactoryBean;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig extends CamelConfiguration {

    @Value("${openehr.address}")
    private String openEhrAddress;

    @Autowired
    private AllergyController allergyController;

    @Autowired
    private CancerMDTController cancerMDTController;

    @Autowired
    private EndOfLifeController endOfLifeController;

    @Autowired
    private LabOrderController labOrderController;

    @Autowired
    private LabResultController labResultController;

    @Autowired
    private MDTReportController mdtReportController;

    @Autowired
    private MedicationController medicationController;

    @Autowired
    private Patients patients;

    @Autowired
    private ProceduresController proceduresController;

    @Autowired
    private TransferOfCareController transferOfCareController;

    @Autowired
    private ReferralsController referralsController;

    @Autowired
    private AppointmentsController appointmentsController;


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new ISO8601DateFormat());
        return objectMapper;
    }

    @Bean
    public JacksonJsonProvider jacksonJsonProvider() {
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
        jacksonJsonProvider.setMapper(objectMapper());
        return jacksonJsonProvider;
    }

    @Bean
    public SpringJAXRSServerFactoryBean rsPatients() {
        SpringJAXRSServerFactoryBean springJAXRSServerFactoryBean = new SpringJAXRSServerFactoryBean();
        springJAXRSServerFactoryBean.setServiceBeanObjects(patients,
                                                           allergyController,
                                                           cancerMDTController,
                                                           endOfLifeController,
                                                           labOrderController,
                                                           labResultController,
                                                           mdtReportController,
                                                           medicationController,
                                                           proceduresController,
                                                           transferOfCareController,
                                                           referralsController,
                                                           appointmentsController);
        springJAXRSServerFactoryBean.setAddress("/patients/");
        springJAXRSServerFactoryBean.setLoggingFeatureEnabled(true);
        springJAXRSServerFactoryBean.setProvider(jacksonJsonProvider());
        return springJAXRSServerFactoryBean;
    }

    @Bean
    public SpringJAXRSClientFactoryBean rsOpenEhr() {
        SpringJAXRSClientFactoryBean springJAXRSClientFactoryBean = new SpringJAXRSClientFactoryBean();
        springJAXRSClientFactoryBean.setServiceClass(OpenEhr.class);
        springJAXRSClientFactoryBean.setAddress(openEhrAddress);
        springJAXRSClientFactoryBean.setLoggingFeatureEnabled(true);
        springJAXRSClientFactoryBean.setProvider(jacksonJsonProvider());
        return springJAXRSClientFactoryBean;
    }

    @Bean
    public SpringJAXRSServerFactoryBean rsPatientSearch() {
        SpringJAXRSServerFactoryBean factoryBean = new SpringJAXRSServerFactoryBean();
        factoryBean.setServiceClass(PatientSearchController.class);
        factoryBean.setLoggingFeatureEnabled(true);
        factoryBean.setProvider(jacksonJsonProvider());

        return factoryBean;
    }
}
