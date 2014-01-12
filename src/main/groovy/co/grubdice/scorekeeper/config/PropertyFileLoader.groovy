package co.grubdice.scorekeeper.config

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@Configuration
@Slf4j
class PropertyFileLoader {

    @Configuration
    @PropertySource("classpath:test.properties")
    @Profile("test")
    @Slf4j
    static class TestDefaults {
        static {
            log.info("Creating profile: test")
        }

    }

    @Configuration
    @PropertySource("classpath:dev.properties")
    @Profile("dev")
    @Slf4j
    static class DevDefaults {
        static {
            log.info("Creating profile: dev")
        }

    }

    @Configuration
    @PropertySource("classpath:dev.properties")
    @Profile("prod")
    @Slf4j
    static class ProdDefaults {
        static {
            log.info("Creating profile: prod")
        }

    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
