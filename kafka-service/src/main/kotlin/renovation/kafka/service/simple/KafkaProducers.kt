/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import java.time.Instant
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

@Configuration
@Profile("simple")
class KafkaProducerConfig {
    @Bean
    fun producerFactory(@Value("\${spring.kafka.bootstrap-servers}") kafkaServerUrl: String):
        ProducerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServerUrl
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>) = KafkaTemplate(producerFactory)
}

data class Note(
    val id: Int,
    val message: String
)

@Configuration
@Profile("simple")
class KafkaProducerConfigMessage {

    @Bean
    fun producerFactoryMessage(@Value("\${spring.kafka.bootstrap-servers}") kafkaServerUrl: String):
        ProducerFactory<String, Note> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServerUrl
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplateMessage(producerFactoryNote: ProducerFactory<String, Note>): KafkaTemplate<String, Note> =
        KafkaTemplate(producerFactoryNote)
}

@Component
@Profile("simple")
class MessageProducer(
    val kafkaTemplate: KafkaTemplate<String, String>,
    val noteKafkaTemplate: KafkaTemplate<String, Note>,
) {

    fun sendMessage(topic: String, message: String?) {
        kafkaTemplate.send(topic, message).also {
            log.info("sending payload='$message' to topic='$topic'")
        }
    }

    fun sendMessage(topic: String, partition: Int, message: String?) {
        kafkaTemplate.send(topic, partition, Instant.now().toString(), message).also {
            log.info("sending payload='$message' to topic='$topic'")
        }
    }

    fun sendMessage(topic: String, key: String, message: String?) {
        kafkaTemplate.send(topic, key, message).also {
            log.info("sending payload='$message' to topic='$topic'")
        }
    }

    fun sendMessage(topic: String, key: String, note: Note) {
        noteKafkaTemplate.send(topic, key, note).also {
            log.info("sending payload='$note' to topic='$topic'")
        }
    }
}
