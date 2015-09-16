package org.rippleosi.patient.contacts.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.contacts.model.ContactHeadline;

/**
 */
public class ContactHeadlineQueryStrategy extends AbstractListQueryStrategy<ContactHeadline> {

    ContactHeadlineQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
               "a_a/items/data[at0001]/items/items[openEHR-EHR-CLUSTER.person_name.v1]/items/value/value as name " +
               "from EHR e[ehr_id/value='" + ehrId + "'] " +
               "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
               "contains SECTION a_a[openEHR-EHR-SECTION.relevant_contacts_rcp.v1] " +
               "where a/name/value='Relevant contacts'";
    }

    @Override
    public List<ContactHeadline> transform(List<Map<String, Object>> resultSet) {

        List<ContactHeadline> contactList = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {

            ContactHeadline contact = new ContactHeadline();
            contact.setSource("openehr");
            contact.setSourceId(MapUtils.getString(data, "uid"));
            contact.setName(MapUtils.getString(data, "name"));

            contactList.add(contact);
        }

        return contactList;
    }
}
