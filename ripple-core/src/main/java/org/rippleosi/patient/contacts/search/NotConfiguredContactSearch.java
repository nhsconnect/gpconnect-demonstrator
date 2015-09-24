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

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.model.ContactSummary;

/**
 */
public class NotConfiguredContactSearch implements ContactSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ContactHeadline> findContactHeadlines(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ContactSearch.class);
    }

    @Override
    public List<ContactSummary> findAllContacts(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ContactSearch.class);
    }

    @Override
    public ContactDetails findContact(String patientId, String contactId) {
        throw ConfigurationException.unimplementedTransaction(ContactSearch.class);
    }
}
