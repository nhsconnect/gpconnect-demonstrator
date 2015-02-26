package net.nhs.esb.patient.model;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 */
public class PatientDetailsBuilder {

    private String firstName;
    private String lastName;
    private String[] address;
    private String postcode;
    private String nhsNumber;
    private String gender;
    private Date dateOfBirth;

    private String gpName;
    private String[] gpAddress;
    private String gpPostcode;

    public void clear() {
        firstName = null;
        lastName = null;
        address = new String[5];
        postcode = null;
        nhsNumber = null;
        gender = null;
        dateOfBirth = null;

        gpName = null;
        gpAddress = new String[5];
        gpPostcode = null;
    }

    public void setDetails(Node node) {

        String nodeName = node.getLocalName();

        if (nodeName.equals("id")) {
            nhsNumber = node.getAttributes().getNamedItem("extension").getNodeValue();
        } else if (nodeName.equals("name")) {
            setNameFields(node.getChildNodes());
        } else if (nodeName.equals("addr")) {
            setAddressFields(node.getChildNodes());
        } else if (nodeName.equals("patientPerson")) {
            setPatientFields(node.getChildNodes());
        }
    }

    private void setNameFields(NodeList nodeList) {
        firstName = findText(nodeList, "given");
        lastName = findText(nodeList, "family");
    }

    private void setAddressFields(NodeList nodeList) {
        postcode = findText(nodeList, "postalCode");
        extractAddress(address, nodeList);
    }

    private void extractAddress(String[] address, NodeList nodeList) {
        int index = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node addressNode = nodeList.item(i);
            if (addressNode.getNodeType() == Node.ELEMENT_NODE && addressNode.getLocalName().equals("streetAddressLine")) {
                address[index] = addressNode.getTextContent();
                index++;
            }
        }
    }

    private void setPatientFields(NodeList nodeList) {
        gender = parseGender(nodeList);
        dateOfBirth = parseDateOfBirth(nodeList);

        NodeList gpPracticeNodeList = findNode(nodeList, "gPPractice");
        NodeList gpAddressNodeList = findNode(gpPracticeNodeList, "addr");
        NodeList gpOrganisationNodeList = findNode(gpPracticeNodeList, "locationOrganization");

        extractAddress(gpAddress, gpAddressNodeList);
        gpName = findText(gpOrganisationNodeList, "name");
        gpPostcode = findText(gpAddressNodeList, "postalCode");
    }

    private String parseGender(NodeList nodeList) {
        String genderCode = findAttribute(nodeList, "administrativeGenderCode", "code");

        if (genderCode.equals("1")) {
            return "Male";
        }

        return null;
    }

    private Date parseDateOfBirth(NodeList nodeList) {
        String dobAsString = findAttribute(nodeList, "birthTime", "value");

        try {
            return DateUtils.parseDate(dobAsString, "yyyyMMdd");
        } catch (ParseException e) {
            return null;
        }
    }

    private NodeList findNode(NodeList nodeList, String nodeName) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (nodeName.equals(node.getLocalName())) {
                return node.getChildNodes();
            }
        }

        return null;
    }

    private String findText(NodeList nodeList, String nodeName) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (nodeName.equals(node.getLocalName())) {
                return StringUtils.trimToNull(node.getTextContent());
            }
        }

        return null;
    }

    private String findAttribute(NodeList nodeList, String nodeName, String attributeName) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (nodeName.equals(node.getLocalName())) {
                Node attribute = node.getAttributes().getNamedItem(attributeName);
                return StringUtils.trimToNull(attribute.getNodeValue());
            }
        }

        return null;
    }

    public PatientDetails build() {

        PracticeDetails practiceDetails = new PracticeDetails();
        practiceDetails.setName(gpName);
        practiceDetails.setAddress1(gpAddress[0]);
        practiceDetails.setAddress2(gpAddress[1]);
        practiceDetails.setAddress3(gpAddress[2]);
        practiceDetails.setAddress4(gpAddress[3]);
        practiceDetails.setAddress5(gpAddress[4]);
        practiceDetails.setPostcode(gpPostcode);

        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setFirstName(firstName);
        patientDetails.setLastName(lastName);
        patientDetails.setAddress1(address[0]);
        patientDetails.setAddress2(address[1]);
        patientDetails.setAddress3(address[2]);
        patientDetails.setAddress4(address[3]);
        patientDetails.setAddress5(address[4]);
        patientDetails.setPostcode(postcode);
        patientDetails.setGender(gender);
        patientDetails.setBorn(dateOfBirth);
        patientDetails.setNhsNumber(nhsNumber);
        patientDetails.setGpPractice(practiceDetails);

        return patientDetails;
    }
}
