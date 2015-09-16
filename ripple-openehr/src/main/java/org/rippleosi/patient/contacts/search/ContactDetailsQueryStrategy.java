package org.rippleosi.patient.contacts.search;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.patient.contacts.model.ContactDetails;

/**
 */
public class ContactDetailsQueryStrategy extends AbstractQueryStrategy<ContactDetails> {

    private final String contactId;

    ContactDetailsQueryStrategy(String patientId, String contactId) {
        super(patientId);
        this.contactId = contactId;
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
               "a/composer/name as author, " +
               "a_a/items/data[at0001]/items/items[openEHR-EHR-CLUSTER.person_name.v1]/items/value/value as name, " +
               "a_a/items/data[at0001]/items/items[openEHR-EHR-CLUSTER.telecom_uk.v1]/items/value/value as contact_information, " +
               "a_a/items/data[at0001]/items[at0035]/value/value as relationship_type, " +
               "a_a/items/data[at0001]/items[at0035]/value/defining_code/terminology_id/value as relationship_terminology, " +
               "a_a/items/data[at0001]/items[at0035]/value/defining_code/code_string as relationship_code, " +
               "a_a/items/data[at0001]/items[at0030]/value/value as relationship, " +
               "a_a/items/data[at0001]/items[at0017]/value/value as notes, " +
               "a_a/items/data[at0001]/items[at0025]/value/value as next_of_kin " +
               "from EHR e[ehr_id/value='" + ehrId + "'] " +
               "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
               "contains SECTION a_a[openEHR-EHR-SECTION.relevant_contacts_rcp.v1] " +
               "where a/name/value='Relevant contacts' " +
               "and a/uid/value='" + contactId + "' ";
    }

    @Override
    public ContactDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String,Object> data = resultSet.get(0);

        Boolean nextOfKin = MapUtils.getBoolean(data, "next_of_kin");

        ContactDetails contact = new ContactDetails();
        contact.setSource("openehr");
        contact.setSourceId(MapUtils.getString(data, "uid"));
        contact.setName(MapUtils.getString(data, "name"));
        contact.setNextOfKin(nextOfKin != null && nextOfKin.booleanValue());
        contact.setRelationship(MapUtils.getString(data, "relationship"));
        contact.setRelationshipType(MapUtils.getString(data, "relationship_type"));
        contact.setRelationshipCode(MapUtils.getString(data, "relationship_code"));
        contact.setRelationshipTerminology(MapUtils.getString(data, "relationship_terminology"));
        contact.setContactInformation(MapUtils.getString(data, "contact_information"));
        contact.setNotes(MapUtils.getString(data, "notes"));

        return contact;
    }
}
