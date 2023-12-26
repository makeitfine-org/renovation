/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

// @Configuration
// @Profile("simple")
class KafkaConsumerConfig {
    @Bean
    fun consumerFactory(
        @Value("\${spring.kafka.bootstrap-servers}") kafkaServerUrl: String,
        @Value("\${spring.kafka.consumer.group-id}") groupId: String
    ): ConsumerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServerUrl
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>):
        ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        return factory
    }
}

@Component
@Profile("simple")
class MessageConsumer {
    final var lastObtainedMessage: String? = null
        private set

    @KafkaListener(topics = ["\${topic.simple}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listen(consumerRecord: ConsumerRecord<*, String>) {
        log.info("Received message: $consumerRecord")

        lastObtainedMessage = consumerRecord.value()
    }
}
