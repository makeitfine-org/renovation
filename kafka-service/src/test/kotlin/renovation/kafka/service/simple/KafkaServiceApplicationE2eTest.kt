/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag

@Tag("e2eTest")
class KafkaServiceApplicationE2eTest : KafkaServiceApplicationTestAbstract() {

    @Test
    override fun sendMessageToTopicPartitioned() {
        messageProducer.sendMessage(partitionedTopic, 2, "payload to topic partitioned 2")
        assertWait()
        assertEquals("payload to topic partitioned 2", messageConsumer.lastObtainedMessage)
        assertEquals(2, messageConsumer.lastObtainedMessagePartition)
        resetLatch()

        messageProducer.sendMessage(partitionedTopic, 0, "payload to topic partitioned 0")
        assertWait()
        assertEquals("payload to topic partitioned 0", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)
        resetLatch()

        messageProducer.sendMessage(partitionedTopic, 1, "payload to topic partitioned 1")
        assertWait()
        assertEquals("payload to topic partitioned 1", messageConsumer.lastObtainedMessage)
        assertEquals(1, messageConsumer.lastObtainedMessagePartition)
        resetLatch()
    }
}
