extra {
    // to use extra.gradle.kts make `from("<path to file dir>/extra.gradle.kts")`
    //
    // can be used similar to "by properties" in all sub-modules
    // for example:
    // val extra_stub_property_name : String by extra
    mapOf(
        //at least one property should be preset
        "extra_stub_property_name" to "extra_stub_property_value",
    ).forEach { (name, version) ->
        project.extra.set(name, version)
    }
}