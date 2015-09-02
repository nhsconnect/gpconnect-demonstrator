package org.rippleosi.patient.procedures.search;

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
public class DefaultProcedureSearchFactoryTest {

    private static final String SOURCE = "source ";

    private DefaultProcedureSearchFactory searchFactory;

    @Mock
    private ApplicationContext mockApplicationContext;

    @Before
    public void setUp() throws Exception {
        searchFactory = new DefaultProcedureSearchFactory();

        ReflectionTestUtils.setField(searchFactory, "applicationContext", mockApplicationContext);
    }

    @Test
    public void shouldReturnDefaultImplementationWhenNoneAreConfigured() {

        initContext(0);

        ProcedureSearch procedureSearch = searchFactory.select(null);
        assertEquals("not configured", procedureSearch.getSource());
    }

    @Test
    public void shouldReturnSameImplementationRegardlessOfSourceWhenOnlyOneIsConfigured() {

        initContext(1);

        ProcedureSearch nullSource = searchFactory.select(null);
        ProcedureSearch source1 = searchFactory.select(SOURCE + 1);
        ProcedureSearch source2 = searchFactory.select(SOURCE + 2);

        String expected = SOURCE + 1;
        assertEquals(expected, nullSource.getSource());
        assertEquals(expected, source1.getSource());
        assertEquals(expected, source2.getSource());
    }

    @Test
    public void shouldReturnCorrectImplementationWhenSourceIsSpecified() {

        initContext(4);

        ProcedureSearch source = searchFactory.select(SOURCE + 2);

        assertEquals(SOURCE + 2, source.getSource());
    }

    @Test
    public void shouldReturnLowestPriorityImplementationWhenInvalidSourceIsSpecified() {

        initContext(2);

        ProcedureSearch source = searchFactory.select("invalid");

        assertEquals(SOURCE + 1, source.getSource());
    }

    private void initContext(int count) {

        Map<String, ProcedureSearch> procedureSearchMap = configureSearch(count);
        when(mockApplicationContext.getBeansOfType(ProcedureSearch.class)).thenReturn(procedureSearchMap);

        searchFactory.postConstruct();
    }

    private Map<String,ProcedureSearch> configureSearch(int count) {

        Map<String,ProcedureSearch> procedureSearchMap = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            ProcedureSearch procedureSearch = Mockito.mock(ProcedureSearch.class);
            when(procedureSearch.getSource()).thenReturn(SOURCE + i);
            when(procedureSearch.getPriority()).thenReturn(i);

            procedureSearchMap.put(SOURCE + i, procedureSearch);
        }

        return procedureSearchMap;
    }
}
