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
package uk.gov.hscic.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import uk.gov.hscic.auth.CertificateValidator;
import uk.gov.hscic.auth.KeyStoreFactory;

@ServletComponentScan
@SpringBootApplication
@ComponentScan(basePackages = "uk.gov.hscic")
@PropertySource("file:${config.path}/gpconnect-demonstrator-api.properties")
public class RestConfig {

    @Value("${config.path}")
    private String configPath;

    @Value("${server.keystore.password}")
    private String keystorePassword;

    @Value("${server.keystore.name}")
    private String keystoreName;

    public static void main(String[] args) {
        SpringApplication.run(RestConfig.class, args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CertificateValidator certificateValidator() throws Exception {
        return new CertificateValidator(KeyStoreFactory.getKeyStore(configPath + keystoreName, keystorePassword));
    }
}
