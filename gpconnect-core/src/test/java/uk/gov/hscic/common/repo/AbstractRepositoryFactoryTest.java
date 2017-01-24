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
package uk.gov.hscic.common.repo;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.common.types.TestSourceType;

public abstract class AbstractRepositoryFactoryTest<F extends RepositoryFactory<R>, R extends Repository> {
    private F factory;

    @Mock
    private ApplicationContext mockApplicationContext;

    protected abstract F createRepositoryFactory();
    protected abstract Class<R> getRepositoryClass();

    @Before
    public void setUp() throws Exception {
        factory = createRepositoryFactory();

        ReflectionTestUtils.setField(factory, "applicationContext", mockApplicationContext);
    }

    @Test
    public void shouldReturnDefaultImplementationWhenNoneAreConfigured() {
        initContext(0);

        assertEquals(RepoSourceType.NONE, factory.select(null).getSource());
    }

    @Test
    public void shouldReturnSameImplementationRegardlessOfSourceWhenOnlyOneIsConfigured() {
        initContext(1);

        assertEquals(TestSourceType.SOURCE1, factory.select(null).getSource());
        assertEquals(TestSourceType.SOURCE1, factory.select(TestSourceType.SOURCE1).getSource());
        assertEquals(TestSourceType.SOURCE1, factory.select(TestSourceType.SOURCE2).getSource());
    }

    @Test
    public void shouldReturnCorrectImplementationWhenSourceIsSpecified() {
        initContext(4);

        assertEquals(TestSourceType.SOURCE2, factory.select(TestSourceType.SOURCE2).getSource());
    }

    @Test
    public void shouldReturnLowestPriorityImplementationWhenInvalidSourceIsSpecified() {
        initContext(2);

        assertEquals(TestSourceType.SOURCE1, factory.select(null).getSource());
    }

    private void initContext(int count) {
        final Class<R> cls = getRepositoryClass();
        final Map<String, R> repositoryMap = configureRepository(cls, count);

        when(mockApplicationContext.getBeansOfType(cls)).thenReturn(repositoryMap);

        ReflectionTestUtils.invokeMethod(factory, "postConstruct");
    }

    private Map<String, R> configureRepository(Class<R> cls, int count) {
        final Map<String, R> repositoryMap = new HashMap<>();

        for (int i = 1; i <= count; i++) {
            final R repository = Mockito.mock(cls);

            when(repository.getSource()).thenReturn(TestSourceType.fromString("Source " + i));
            when(repository.getPriority()).thenReturn(i);

            repositoryMap.put("Source " + i, repository);
        }

        return repositoryMap;
    }
}