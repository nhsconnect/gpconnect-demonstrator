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
package org.rippleosi.patient.contacts.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.contacts.model.ContactDetails;

/**
 */
public class ContactDetailsTransformer implements Transformer<Map<String, Object>, ContactDetails> {

    @Override
    public ContactDetails transform(Map<String, Object> input) {

        Boolean nextOfKin = MapUtils.getBoolean(input, "next_of_kin");

        ContactDetails contact = new ContactDetails();
        contact.setSource("Marand");
        contact.setSourceId(MapUtils.getString(input, "uid"));
        contact.setName(MapUtils.getString(input, "name"));
        contact.setNextOfKin(nextOfKin != null && nextOfKin.booleanValue());
        contact.setRelationship(MapUtils.getString(input, "relationship"));
        contact.setRelationshipType(MapUtils.getString(input, "relationship_type"));
        contact.setRelationshipCode(MapUtils.getString(input, "relationship_code"));
        contact.setRelationshipTerminology(MapUtils.getString(input, "relationship_terminology"));
        contact.setContactInformation(MapUtils.getString(input, "contact_information"));
        contact.setNotes(MapUtils.getString(input, "notes"));
        contact.setAuthor(MapUtils.getString(input, "author"));

        String dateCreated = MapUtils.getString(input, "date_created");
        contact.setDateCreated(DateFormatter.toDate(dateCreated));

        return contact;
    }
}
