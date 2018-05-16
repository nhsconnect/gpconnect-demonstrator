package uk.gov.hscic.common.config;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupConfig implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(StartupConfig.class);

    @Value("${database.reset:true}")
    private boolean databaseReset;

    @Autowired
    private DatabaseRefresher databaseRefresher;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (databaseReset) {
                databaseRefresher.resetDatabase();
            }
        } catch (IOException ex) {
            LOG.error("Cannot refresh db!");
        }
    }
}
