architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    enableTransitiveAccessWideners.set(true)
}

dependencies {
    val fabricLoaderVersion: String by project
    val fabricApiVersion: String by project
    val cobblemonVersion: String by project

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modApi(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))

    compileOnly("com.teamresourceful:yabn:1.0.3")
    compileOnly("com.teamresourceful:bytecodecs:1.0.2")

    modImplementation(group = "com.cobblemon", name = "fabric", version = "$cobblemonVersion+1.20.1-SNAPSHOT")
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}
