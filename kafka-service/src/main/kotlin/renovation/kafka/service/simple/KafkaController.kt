/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("simple")
class KafkaController(
    val messageProducer: MessageProducer,
    @Value("\${topic.simple}")
    val simpleTopic: String,
    @Value("\${topic.partitioned}")
    val partitionedTopic: String,
) {

    @PostMapping("/send")
    fun sendMessage(@RequestParam("message") message: String): String {
        messageProducer.sendMessage(simpleTopic, message)
        return "Message sent: $message"
    }

    @PostMapping("/send/{partition}")
    fun sendMessageWithPartition(@RequestParam("message") message: String, @PathVariable partition: Int): String {
        messageProducer.sendMessage(partitionedTopic, partition, message)
        return "Message sent: $message to partition: $partition"
    }
}
