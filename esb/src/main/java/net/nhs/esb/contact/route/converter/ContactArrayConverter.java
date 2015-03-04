package net.nhs.esb.contact.route.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactArray;
import net.nhs.esb.rest.domain.QueryResponseData;
import org.apache.camel.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ContactArrayConverter {

    private static final Logger log = LoggerFactory.getLogger(ContactArrayConverter.class);

    @Converter
    public ContactArray convertAQLResponseToContactArray(QueryResponseData response) {

        ContactTransformer transformer = new ContactTransformer();
        List<Contact> contactList = CollectionUtils.collect(response.getResultSet(), transformer, new ArrayList<Contact>());

        ContactArray contactArray = new ContactArray();
        contactArray.setContacts(contactList);

        return contactArray;
    }

    private static class ContactTransformer implements Transformer<Map<String,Object>, Contact> {

        @Override
        public Contact transform(Map<String, Object> result) {
            Contact contact = new Contact();

            try {
                PropertyUtils.copyProperties(contact, result);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                log.error(ex.getMessage(), ex);
            }

            return contact;
        }
    }

}
