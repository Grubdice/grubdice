package co.grubdice.scorekeeper.config
import com.googlecode.flyway.core.Flyway
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate4.HibernateExceptionTranslator
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.annotation.Resource
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import java.sql.SQLException

@Configuration
@Slf4j
@EnableTransactionManagement
@Import(DataSourceConfig.class)
@EnableJpaRepositories(basePackages = ['co.grubdice.scorekeeper.dao'] )
class HibernateConfig {

    @Autowired
    @Resource(name = 'dataSource')
    DataSource dataSource

    @Value('${datasource.update}')
    String hibernateUpdateDdl

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {
        flyway();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("co.grubdice.scorekeeper");
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    public void flyway() {

        if(['prod', 'dev'].contains(
                StringUtils.trimToEmpty(
                        System.getProperty("spring.profiles.active")).toLowerCase())){
            def flyway = new Flyway()
            flyway.setDataSource(dataSource)
            flyway.setOutOfOrder(true)
            flyway.migrate()
        }
    }
}
