package co.grubdice.scorekeeper.config

import com.jolbox.bonecp.BoneCPDataSource
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import sun.swing.StringUIClientPropertyKey

import javax.sql.DataSource

@Configuration
class DataSourceConfig {

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

    @Bean(name = 'dataSource')
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(boneDataSource());
    }

    @Bean(destroyMethod = "close")
    public BoneCPDataSource boneDataSource() {
        def source = new BoneCPDataSource();
        source.setDriverClass(driver)
        String fullUrl = System.getenv("DATABASE_URL")
        if(StringUtils.trimToEmpty(fullUrl).isEmpty()){
            source.setUsername(userName)
            source.setPassword(password)
            source.setJdbcUrl(jdbcUrl)
        } else {
            source.setJdbcUrl(fullUrl)
        }
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
}
