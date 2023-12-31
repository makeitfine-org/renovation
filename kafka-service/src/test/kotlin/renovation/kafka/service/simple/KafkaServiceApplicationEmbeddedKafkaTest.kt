/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import org.junit.jupiter.api.Tag
import org.springframework.kafka.test.context.EmbeddedKafka

@Tag("integrationTest")
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:29192", "port=29192"])
class KafkaServiceApplicationEmbeddedKafkaTest : KafkaServiceApplicationTestAbstract()
