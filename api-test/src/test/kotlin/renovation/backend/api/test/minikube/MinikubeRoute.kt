/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.minikube

enum class MinikubeRoute(route: String) {
    IP_BACKEND_ABOUT("http://192.168.49.2:30080/about"),
    IP_INFO_ABOUT("http://192.168.49.2:30090/about"),
    IP_FRONTEND_INFO_ABOUT("http://192.168.49.2:30081/about"),

    MMIB_ABOUT("http://mmib/about"),
    MMII_ABOUT("http://mmii/about");

    val route = route
}
