/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.minikube

import renovation.backend.api.test.Route

enum class MinikubeRoute(port: Int, path: String) : Route {
    BACKEND_ABOUT(30080,"about"),
    INFO_ABOUT(30090,"about"),
    FRONTEND_INFO_ABOUT(30081,"about");

    override val baseUrlFromEnvironmentVariable = "MINIKUBE_SERVER_URL"
    override val baseUrlDefault = "http://192.168.49.2"
    override val path = path

    private val port = port

    override val baseUrl: String
        get() = "${super.baseUrl}:$port"
}
