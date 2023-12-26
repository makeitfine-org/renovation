/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@Tag("integrationTest")
@ActiveProfiles("simple")
@DirtiesContext
@SpringBootTest
abstract class KafkaServiceApplicationTestAbstract {
    @Autowired
    private lateinit var messageProducer: MessageProducer

    @Autowired
    private lateinit var messageConsumer: MessageConsumer

    @Value("\${topic.simple}")
    private lateinit var topic: String

    @Test
    fun sendMessage() {
        messageProducer.sendMessage(topic, "payload to topic")
        Thread.sleep(500)
        assertEquals("payload to topic", messageConsumer.lastObtainedMessage)

        messageProducer.sendMessage(topic, "payload next to topic")
        Thread.sleep(500)
        assertEquals("payload next to topic", messageConsumer.lastObtainedMessage)
    }
}
