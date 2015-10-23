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
package org.rippleosi.config;

import javax.sql.DataSource;

import com.mysql.jdbc.Driver;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LegacyDataConfig {

    @Value("${legacy.datasource.vendor:mysql}")
    private String vendor;

    @Value("${legacy.datasource.host:127.0.0.1}")
    private String host;

    @Value("${legacy.datasource.port:3306}")
    private String port;

    @Value("${legacy.datasource.schema:poc_legacy}")
    private String schema;

    @Value("${legacy.datasource.username:root}")
    private String username;

    @Value("${legacy.datasource.password:password}")
    private String password;

    @Bean(destroyMethod = "close")
    public DataSource legacyDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl("jdbc:" + vendor + "://" + host + ":" + port + "/" + schema);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setValidationQuery("select 1 as dbcp_connection_test");
        dataSource.setTestOnBorrow(true);

        return dataSource;
    }
}
