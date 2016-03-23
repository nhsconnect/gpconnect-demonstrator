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
import org.rippleosi.patient.contacts.model.ContactHeadline;

/**
 */
public class ContactHeadlineTransformer implements Transformer<Map<String, Object>, ContactHeadline> {

    @Override
    public ContactHeadline transform(Map<String, Object> input) {

        ContactHeadline contact = new ContactHeadline();
        contact.setSource("Marand");
        contact.setSourceId(MapUtils.getString(input, "uid"));
        contact.setName(MapUtils.getString(input, "name"));

        return contact;
    }
}
