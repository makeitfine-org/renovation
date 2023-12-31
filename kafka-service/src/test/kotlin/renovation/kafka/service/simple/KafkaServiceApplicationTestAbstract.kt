/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("simple")
@DirtiesContext
@SpringBootTest
abstract class KafkaServiceApplicationTestAbstract {
    @Autowired
    protected lateinit var messageProducer: MessageProducer

    @Autowired
    protected lateinit var messageConsumer: MessageConsumer

    @Autowired
    protected lateinit var messageOtherConsumer: MessageOtherConsumer

    @Value("\${topic.simple}")
    protected lateinit var simpleTopic: String

    @Value("\${topic.partitioned}")
    protected lateinit var partitionedTopic: String

    @Value("\${topic.secret}")
    protected lateinit var secretTopic: String

    @Test
    fun sendMessageToTopicSimple() {
        messageProducer.sendMessage(simpleTopic, "payload to topic simple")
        assertWait()
        assertEquals("payload to topic simple", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)
        resetLatch()

        messageProducer.sendMessage(simpleTopic, "payload next to topic simple")
        assertWait()
        assertEquals("payload next to topic simple", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)
        resetLatch()
    }

    @Test
    fun sendMessageToTopicPartitioned() {
        messageProducer.sendMessage(partitionedTopic, 0, "payload to topic partitioned 0")
        assertWait()
        assertEquals("payload to topic partitioned 0", messageConsumer.lastObtainedMessage)
        assertEquals(0, messageConsumer.lastObtainedMessagePartition)
        resetLatch()
    }

    protected fun assertWait() = assertTrue(messageConsumer.latch.await(5, TimeUnit.SECONDS))

    protected fun resetLatch() = messageConsumer.resetLatch()

    @Test
    fun sendMessageToTopicSecret() {
        messageProducer.sendMessage(secretTopic, Instant.now().toString(), "payload to topic secret")
        assertFalse(messageOtherConsumer.latch.await(5, TimeUnit.SECONDS))
        assertNull(messageOtherConsumer.lastObtainedMessage)
        assertTrue(arrayOf(0, 1).contains(messageOtherConsumer.lastObtainedMessagePartition))
        messageOtherConsumer.resetLatch()

        messageProducer.sendMessage(secretTopic, Instant.now().toString(), "payload to topic secret (confident)")
        assertTrue(messageOtherConsumer.latch.await(5, TimeUnit.SECONDS))
        assertEquals("payload to topic secret (confident)", messageOtherConsumer.lastObtainedMessage)
        assertTrue(arrayOf(0, 1).contains(messageOtherConsumer.lastObtainedMessagePartition))
        messageOtherConsumer.resetLatch()
    }
}
