package co.grubdice.scorekeeper.config

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan("co.grubdice.scorekeeper")
@Slf4j
@Import([HibernateConfig.class, PropertyFileLoader.class, WebConfig.class])
class BaseConfig {

    static {
        log.info("Scanning beans")
    }
}
