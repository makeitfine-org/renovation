package renovation.kafka.service

import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import renovation.kafka.service.simple.MessageConsumer
import renovation.kafka.service.simple.MessageProducer

@Tag("integrationTest")
@ActiveProfiles("simple")
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:29192", "port=29192"])
class KafkaServiceEmbeddedKafkaApplicationTest {

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
