package co.grubdice.scorekeeper.config
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.*
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
    @PropertySource("classpath:prod.properties")
    @Profile("prod")
    @Slf4j
    static class ProdDefaults {
        static {
            log.info("Creating profile: prod")
        }

    }

    @Configuration
    @PropertySource(value = "file:/opt/grubdice/config/local.properties", ignoreResourceNotFound = true)
    @Import([ TestDefaults.class, DevDefaults.class, ProdDefaults.class])
    @Slf4j
    static class OverrideDefaults {
        static {
            log.info("Loading defaults")
        }
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
