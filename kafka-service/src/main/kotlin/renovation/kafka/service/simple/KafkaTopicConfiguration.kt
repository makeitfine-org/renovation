/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.TopicBuilder

@Configuration
@Profile("simple")
class KafkaTopicConfiguration {

    @Bean
    @Suppress("detekt:MagicNumber")
    fun compactTopicExample(@Value("\${topic.partitioned}") topic: String): NewTopic {
        return TopicBuilder.name(topic)
            .partitions(3)
            .replicas(2)
            .compact()
            .build()
    }
}
