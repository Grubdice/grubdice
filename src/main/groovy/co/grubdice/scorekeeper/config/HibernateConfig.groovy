package co.grubdice.scorekeeper.config
import com.googlecode.flyway.core.Flyway
import groovy.util.logging.Slf4j
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.annotation.Resource
import javax.sql.DataSource

@Configuration
@Slf4j
@EnableTransactionManagement
@Import(DataSourceConfig.class)
class HibernateConfig {

    @Autowired
    @Resource(name = 'dataSource')
    DataSource dataSource

    @Value('${datasource.update}')
    String hibernateUpdateDdl

    @Bean
    public SessionFactory sessionFactory() {
        flyway();

        def builder = new LocalSessionFactoryBuilder(dataSource)
        builder.setProperty("hibernate.hbm2ddl.auto", hibernateUpdateDdl)
        builder.setProperty("show_sql", "true")
        builder.setProperty("format_sql", "true")
        builder.scanPackages("co.grubdice.scorekeeper")
        return builder.buildSessionFactory()
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    public void flyway() {

//        if(StringUtils.trimToEmpty(System.getProperty("spring.profiles.active")).equalsIgnoreCase("prod")) {
            def flyway = new Flyway()
            flyway.setDataSource(dataSource)
            flyway.migrate()
        }
//    }
}
