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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Transactional
public class DatabaseRefresher {
    private static final Logger LOG = Logger.getLogger(DatabaseRefresher.class);

    @Value("${config.path}")
    private String configPath;

    @Autowired
    private EntityManager entityManager;

    // Reset entire db on startup
    public void resetDatabase() throws IOException {
        runSql("create_tables.sql");

        Files.list(Paths.get(configPath + "sql/"))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(filename -> filename.startsWith("populate"))
                .filter(filename -> !filename.equals("populate_patients_table.sql"))
                .filter(filename -> !filename.equals("generate_uids.sql"))
                .forEach(this::runSql);

        runSql("generate_uids.sql");
        runSql("populate_patients_table.sql");

    }

    private void runSql(String filename) {
        File providerRoutingFile = new File(configPath + "sql/" + filename);

        if (providerRoutingFile.exists()) {
            try {
                String[] sqls = Files.readAllLines(providerRoutingFile.toPath())
                        .stream()
                        .map(line -> line.replaceAll(";$", ";END_OF_STATEMENT;"))
                        .collect(Collectors.joining(" "))
                        .split(";END_OF_STATEMENT;");

                LOG.info("Read " + filename);

                for (String sql : Arrays.asList(sqls)) {
                    try {
                        entityManager
                            .createNativeQuery(sql)
                            .executeUpdate();
                    } catch (Exception ex) {
                        LOG.error("Error executing " + filename);
                    }
                }

                LOG.info("Executed " + filename);
            } catch (IOException ex) {
                LOG.error("Error reading " + filename);
            }
        }
    }
}
