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
    private lateinit var simpleTopic: String

    @Value("\${topic.partitioned}")
    private lateinit var partitionedTopic: String

    @Test
    fun sendMessageToTopicSimple() {
        messageProducer.sendMessage(simpleTopic, "payload to topic simple")
        Thread.sleep(500)
        assertEquals("payload to topic simple", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)

        messageProducer.sendMessage(simpleTopic, "payload next to topic simple")
        Thread.sleep(500)
        assertEquals("payload next to topic simple", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)
    }

    @Test
    fun sendMessageToTopicPartitioned() {
//        messageProducer.sendMessage(partitionedTopic, 2, "payload to topic partitioned 2")
//        Thread.sleep(500)
//        assertEquals("payload to topic partitioned 2", messageConsumer.lastObtainedMessage)
//        assertEquals(2, messageConsumer.lastObtainedMessagePartition)

        messageProducer.sendMessage(partitionedTopic, 0, "payload to topic partitioned 0")
        Thread.sleep(500)
        assertEquals("payload to topic partitioned 0", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)
//
//        messageProducer.sendMessage(partitionedTopic, 1, "payload to topic partitioned 1")
//        Thread.sleep(500)
//        assertEquals("payload to topic partitioned 1", messageConsumer.lastObtainedMessage)
//        assertEquals(1, messageConsumer.lastObtainedMessagePartition)
    }
}
