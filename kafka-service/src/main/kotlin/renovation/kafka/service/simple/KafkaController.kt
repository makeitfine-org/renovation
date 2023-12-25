/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.kafka.service.simple

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("simple")
class KafkaController(
    val messageProducer: MessageProducer,
    @Value("\${topic.simple}")
    val topic: String
) {

    @PostMapping("/send")
    fun sendMessage(@RequestParam("message") message: String): String {
        messageProducer.sendMessage(topic, message)
        return "Message sent: $message"
    }
}
