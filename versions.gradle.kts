extra {
    mapOf(
        "restAssuredVersion" to "5.0.0",
    ).forEach { (name, version) ->
        project.extra.set(name, version)
    }
}