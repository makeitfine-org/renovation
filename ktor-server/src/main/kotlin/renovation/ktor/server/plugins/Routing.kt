package renovation.ktor.server.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import renovation.ktor.server.data.models.customerStorage
import renovation.ktor.server.routes.customerRouting
import renovation.ktor.server.routes.getOrderRoute
import renovation.ktor.server.routes.listOrdersRoute
import renovation.ktor.server.routes.totalizeOrderRoute

fun Application.configureRouting() {
    routing {
        staticResources("/static", "files")

        customerRouting()
        listOrdersRoute()
        getOrderRoute()
        totalizeOrderRoute()

        serviceRouting()
    }
}

fun Route.serviceRouting() {
    val port = environment?.config?.propertyOrNull("ktor.deployment.port")?.getString()
    route("/service") {
        get {
            call.respondText("Listening on port: $port")
        }
        get("/other") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                customerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }
    }
}
