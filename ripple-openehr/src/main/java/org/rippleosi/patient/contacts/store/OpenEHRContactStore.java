/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.contacts.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRContactStore extends AbstractOpenEhrService implements ContactStore {

    @Value("${c4hOpenEHR.contactsTemplate}")
    private String contactsTemplate;

    private static final String CONTACT_PREFIX = "relevant_contacts/relevant_contacts:0/relevant_contact:0";

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Contacts.Create")
    public void create(String patientId, ContactDetails contact) {

        Map<String,Object> content = createFlatJsonContent(contact);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, contactsTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Contacts.Update")
    public void update(String patientId, ContactDetails contact) {

        Map<String,Object> content = createFlatJsonContent(contact);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(contact.getSourceId(), patientId, contactsTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String,Object> createFlatJsonContent(ContactDetails contact) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/composer_name", contact.getAuthor());

        Boolean nextOfKin = contact.isNextOfKin() ? Boolean.TRUE : null;

        content.put(CONTACT_PREFIX + "/personal_details/person_name/unstructured_name", contact.getName());
        content.put(CONTACT_PREFIX + "/personal_details/telecom_details/unstuctured_telcoms", contact.getContactInformation());
        content.put(CONTACT_PREFIX + "/relationship", contact.getRelationship());
        content.put(CONTACT_PREFIX + "/relationship_category|" + contact.getRelationshipCode(), Boolean.TRUE);
        content.put(CONTACT_PREFIX + "/relationship_category|value", contact.getRelationshipType());
        content.put(CONTACT_PREFIX + "/relationship_category|code", contact.getRelationshipCode());
        content.put(CONTACT_PREFIX + "/relationship_category|terminology", contact.getRelationshipTerminology());
        content.put(CONTACT_PREFIX + "/is_next_of_kin", nextOfKin);
        content.put(CONTACT_PREFIX + "/note", contact.getNotes());

        return content;
    }
}
