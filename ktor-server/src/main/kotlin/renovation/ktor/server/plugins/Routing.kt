package renovation.ktor.server.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import renovation.ktor.server.routes.customerRouting
import renovation.ktor.server.routes.getOrderRoute
import renovation.ktor.server.routes.listOrdersRoute
import renovation.ktor.server.routes.totalizeOrderRoute

fun Application.configureRouting() {
    routing {
        customerRouting()
        listOrdersRoute()
        getOrderRoute()
        totalizeOrderRoute()
    }
}
