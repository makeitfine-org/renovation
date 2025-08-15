/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.backend.config.database

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Profile("postgres-replication")
@Configuration
class ReplicationDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.writer")
    fun writerDataSourceProperties(): DataSourceProperties =
        DataSourceProperties()

    @Bean
    @ConfigurationProperties("spring.datasource.reader")
    fun readerDataSourceProperties(): DataSourceProperties =
        DataSourceProperties()

    @Bean("writerDataSource")
    @ConfigurationProperties("spring.datasource.writer.hikari")
    fun writerDataSource(): DataSource =
        writerDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()

    @Bean("readerDataSource")
    @ConfigurationProperties("spring.datasource.reader.hikari")
    fun readerDataSource(): DataSource =
        readerDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()

    @Primary
    @Bean
    fun dataSource(
        @Qualifier("writerDataSource") writer: DataSource,
        @Qualifier("readerDataSource") reader: DataSource
    ): DataSource {
        val routing = ReadOnlyRoutingDataSource()
        val targets = mutableMapOf<Any, Any>()

        targets[LookUpKey.WRITER] = writer
        targets[LookUpKey.READER] = reader

        routing.setTargetDataSources(targets)
        routing.setDefaultTargetDataSource(writer)

        return routing
    }
}
