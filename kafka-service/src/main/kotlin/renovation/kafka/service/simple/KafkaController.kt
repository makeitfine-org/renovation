/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    @Value("\${topic.secret}")
    val secretTopic: String,
    @Value("\${topic.note}")
    val noteTopic: String,
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

    @PostMapping("/send/secret")
    fun sendMessageToSecretTopic(@RequestParam("message") message: String): String {
        messageProducer.sendMessage(secretTopic, Instant.now().toString(), message)
        return "Message sent: $message to $secretTopic topic"
    }

    @PostMapping("/send/note")
    fun sendMessageToSecretTopic(@RequestBody note: Note): String {
        messageProducer.sendMessage(noteTopic, Instant.now().toString(), note)
        return "Message sent: $note to $secretTopic topic"
    }
}
