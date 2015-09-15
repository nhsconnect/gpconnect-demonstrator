package org.rippleosi.common.search;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/**
 */
public abstract class AbstractRepositoryFactoryTest<F extends RepositoryFactory<S>,S extends Repository> {

    private static final String SOURCE = "source ";

    private F searchFactory;

    @Mock
    private ApplicationContext mockApplicationContext;

    protected abstract F createSearchFactory();
    protected abstract Class<S> getSearchClass();

    @Before
    public void setUp() throws Exception {
        searchFactory = createSearchFactory();

        ReflectionTestUtils.setField(searchFactory, "applicationContext", mockApplicationContext);
    }

    @Test
    public void shouldReturnDefaultImplementationWhenNoneAreConfigured() {

        initContext(0);

        S search = searchFactory.select(null);
        assertEquals("not configured", search.getSource());
    }

    @Test
    public void shouldReturnSameImplementationRegardlessOfSourceWhenOnlyOneIsConfigured() {

        initContext(1);

        S nullSource = searchFactory.select(null);
        S source1 = searchFactory.select(SOURCE + 1);
        S source2 = searchFactory.select(SOURCE + 2);

        String expected = SOURCE + 1;
        assertEquals(expected, nullSource.getSource());
        assertEquals(expected, source1.getSource());
        assertEquals(expected, source2.getSource());
    }

    @Test
    public void shouldReturnCorrectImplementationWhenSourceIsSpecified() {

        initContext(4);

        S source = searchFactory.select(SOURCE + 2);

        assertEquals(SOURCE + 2, source.getSource());
    }

    @Test
    public void shouldReturnLowestPriorityImplementationWhenInvalidSourceIsSpecified() {

        initContext(2);

        S source = searchFactory.select("invalid");

        assertEquals(SOURCE + 1, source.getSource());
    }

    private void initContext(int count) {

        Class<S> cls = getSearchClass();

        Map<String, S> patientSearchMap = configureSearch(cls, count);
        when(mockApplicationContext.getBeansOfType(cls)).thenReturn(patientSearchMap);

        ReflectionTestUtils.invokeMethod(searchFactory, "postConstruct");
    }

    private Map<String,S> configureSearch(Class<S> cls, int count) {

        Map<String,S> patientSearchMap = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            S patientSearch = Mockito.mock(cls);
            when(patientSearch.getSource()).thenReturn(SOURCE + i);
            when(patientSearch.getPriority()).thenReturn(i);

            patientSearchMap.put(SOURCE + i, patientSearch);
        }

        return patientSearchMap;
    }
}
