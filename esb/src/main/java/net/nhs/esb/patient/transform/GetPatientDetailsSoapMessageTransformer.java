package net.nhs.esb.patient.transform;

import javax.xml.xpath.XPathExpressionException;

import net.nhs.esb.patient.model.PatientDetails;
import net.nhs.esb.patient.model.PatientDetailsArray;
import net.nhs.esb.patient.model.PatientDetailsBuilder;
import net.nhs.esb.patient.model.PatientSearchCriteria;
import org.apache.camel.CamelContext;
import org.apache.camel.Converter;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.builder.xml.XPathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 */
@Component
@Converter
public class GetPatientDetailsSoapMessageTransformer {

    private static final String PAYLOAD_XPATH = "/soap:Envelope/soap:Body/itk:DistributionEnvelope/itk:payloads/itk:payload/*";
    private static final String PATIENT_XPATH = "/hl7:getPatientDetailsResponse-v1-0/hl7:subject/hl7:patient";

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private Namespaces namespaces;

    @Produce
    private ProducerTemplate producerTemplate;

    public String createRequest(PatientSearchCriteria searchCriteria) {
        return producerTemplate.requestBody("velocity:template/velocity/GetPatientDetailsBySearch.vm", searchCriteria, String.class);
    }

    @Converter
    public PatientDetailsArray convert(String soapResponse) throws XPathExpressionException {

        String payload = XPathBuilder.xpath(PAYLOAD_XPATH).namespaces(namespaces).evaluate(camelContext, soapResponse, String.class);

        NodeList patientList = XPathBuilder.xpath(PATIENT_XPATH).namespaces(namespaces).nodeSetResult().evaluate(camelContext, payload, NodeList.class);

        PatientDetailsBuilder patientDetailsBuilder = new PatientDetailsBuilder();

        int patientCount = patientList.getLength();

        PatientDetails[] patientDetails = new PatientDetails[patientCount];
        for (int i = 0; i < patientCount; i++) {

            patientDetailsBuilder.clear();

            Node patient = patientList.item(i);

            NodeList childNodes = patient.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node node = childNodes.item(j);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    patientDetailsBuilder.setDetails(node);
                }
            }

            patientDetails[i] = patientDetailsBuilder.build();
        }

        PatientDetailsArray patientDetailsArray = new PatientDetailsArray();
        patientDetailsArray.setPatientDetails(patientDetails);

        return patientDetailsArray;
    }
}
