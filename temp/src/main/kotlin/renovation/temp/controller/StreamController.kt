package renovation.temp.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.StreamService

@RestController
@RequestMapping("/stream")
class StreamController(
    private val serv: StreamService,
) {
    @GetMapping("all")
    fun all() = serv.data()

    @GetMapping("map")
    fun map() = serv.data().stream().map { e -> e.id }.toList()
}
