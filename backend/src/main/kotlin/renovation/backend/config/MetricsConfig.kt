/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.config

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfig {

    @Bean
    fun getAllWorksCounter(@Autowired meterRegistry: MeterRegistry) =
        Counter.builder("get_all_works_counter")
            .description("Count number of all works requests (it's used for main page loading)")
            .register(meterRegistry)
}
