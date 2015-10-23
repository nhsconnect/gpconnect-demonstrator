/*
 *   Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.search.patient.reports.table;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.repo.AbstractRepositoryFactoryTest;
import org.rippleosi.search.reports.table.DefaultReportTableSearchFactory;
import org.rippleosi.search.reports.table.ReportTableSearch;
import org.rippleosi.search.reports.table.ReportTableSearchFactory;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReportTableSearchFactoryTest
    extends AbstractRepositoryFactoryTest<ReportTableSearchFactory, ReportTableSearch> {

    @Override
    protected ReportTableSearchFactory createRepositoryFactory() {
        return new DefaultReportTableSearchFactory();
    }

    @Override
    protected Class<ReportTableSearch> getRepositoryClass() {
        return ReportTableSearch.class;
    }
}
