/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory

@Configuration
@Profile("simple")
class KafkaConfiguration {

    @Bean
    @Suppress("detekt:MagicNumber")
    fun createTopicPartitioned(@Value("\${topic.partitioned}") topic: String) = TopicBuilder.name(topic)
        .partitions(3)
        .replicas(2)
        .compact()
        .build()

    @Bean
    @Suppress("detekt:MagicNumber")
    fun createTopicSecret(@Value("\${topic.secret}") topic: String) = TopicBuilder.name(topic)
        .partitions(2)
        .replicas(2)
        .compact()
        .build()

    @Bean
    fun filterKafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>):
        ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory =
            ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.setRecordFilterStrategy { !it.value().contains("confident") }
        return factory
    }
}
