package org.rippleosi.patient.allergies.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAllergySearchFactoryTest {

    private static final String SOURCE = "source ";

    private DefaultAllergySearchFactory searchFactory;

    @Mock
    private ApplicationContext mockApplicationContext;

    @Before
    public void setUp() throws Exception {
        searchFactory = new DefaultAllergySearchFactory();

        ReflectionTestUtils.setField(searchFactory, "applicationContext", mockApplicationContext);
    }

    @Test
    public void shouldReturnDefaultImplementationWhenNoneAreConfigured() {

        initContext(0);

        AllergySearch allergySearch = searchFactory.select(null);
        assertEquals("not configured", allergySearch.getSource());
    }

    @Test
    public void shouldReturnSameImplementationRegardlessOfSourceWhenOnlyOneIsConfigured() {

        initContext(1);

        AllergySearch nullSource = searchFactory.select(null);
        AllergySearch source1 = searchFactory.select(SOURCE + 1);
        AllergySearch source2 = searchFactory.select(SOURCE + 2);

        String expected = SOURCE + 1;
        assertEquals(expected, nullSource.getSource());
        assertEquals(expected, source1.getSource());
        assertEquals(expected, source2.getSource());
    }

    @Test
    public void shouldReturnCorrectImplementationWhenSourceIsSpecified() {

        initContext(4);

        AllergySearch source = searchFactory.select(SOURCE + 2);

        assertEquals(SOURCE + 2, source.getSource());
    }

    @Test
    public void shouldReturnLowestPriorityImplementationWhenInvalidSourceIsSpecified() {

        initContext(2);

        AllergySearch source = searchFactory.select("invalid");

        assertEquals(SOURCE + 1, source.getSource());
    }

    private void initContext(int count) {

        Map<String, AllergySearch> allergySearchMap = configureSearch(count);
        when(mockApplicationContext.getBeansOfType(AllergySearch.class)).thenReturn(allergySearchMap);

        searchFactory.postConstruct();
    }

    private Map<String,AllergySearch> configureSearch(int count) {

        Map<String,AllergySearch> allergySearchMap = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            AllergySearch allergySearch = Mockito.mock(AllergySearch.class);
            when(allergySearch.getSource()).thenReturn(SOURCE + i);
            when(allergySearch.getPriority()).thenReturn(i);

            allergySearchMap.put(SOURCE + i, allergySearch);
        }

        return allergySearchMap;
    }
}
