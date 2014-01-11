package co.grubdice.scorekeeper.config

import com.jolbox.bonecp.BoneCPDataSource
import groovy.util.logging.Slf4j
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@Slf4j
@EnableTransactionManagement
class HibernateConfig {

    @Value('${datasource.driver}')
    String driver
    @Value('${datasource.username}')
    String userName
    @Value('${datasource.password}')
    String password
    @Value('${datasource.jdbcUrl}')
    String jdbcUrl
    @Value('${datasource.idleConnectionTestPeriodInSeconds}')
    String idleConnections
    @Value('${datasource.idleMaxAgeInSeconds}')
    String maxIdleAge
    @Value('${datasource.maxConnectionsPerPartition}')
    String maxConnections
    @Value('${datasource.minConnectionsPerPartition}')
    String minConnections
    @Value('${datasource.partitionCount}')
    String partitionCount
    @Value('${datasource.acquireIncrement}')
    String acquireIncrement
    @Value('${datasource.statementsCacheSize}')
    String statementCacheSize

    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(boneDataSource());
    }

    @Bean(destroyMethod = "close")
    public BoneCPDataSource boneDataSource() {
        def source = new BoneCPDataSource();
        source.setDriverClass(driver)
        source.setUsername(userName)
        source.setPassword(password)
        source.setJdbcUrl(jdbcUrl)
        source.setIdleConnectionTestPeriodInSeconds(Integer.decode(idleConnections))
        source.setIdleMaxAgeInSeconds(Integer.decode(maxIdleAge))
        source.setMaxConnectionsPerPartition(Integer.decode(maxConnections))
        source.setMinConnectionsPerPartition(Integer.decode(minConnections))
        source.setPartitionCount(Integer.decode(partitionCount))
        source.setAcquireIncrement(Integer.decode(acquireIncrement))
        source.setStatementsCacheSize(Integer.decode(statementCacheSize))
        //source.setReleaseHelperThreads(3)
        return source
    }

    @Bean
    public SessionFactory sessionFactory() {
        def builder = new LocalSessionFactoryBuilder(dataSource())
        builder.setProperty("hibernate.hbm2ddl.auto", "update")
        builder.scanPackages("co.grubdice.scorekeeper")
        return builder.buildSessionFactory()
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
