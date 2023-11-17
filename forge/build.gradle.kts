architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    enableTransitiveAccessWideners.set(true)
}

dependencies {
    val minecraftVersion: String by project
    val forgeVersion: String by project
    val cobblemonVersion: String by project

    forge(group = "net.minecraftforge", name = "forge", version = "$minecraftVersion-$forgeVersion")

    implementation(project(":common", configuration = "namedElements"))
    "developmentForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }

    compileOnly("com.teamresourceful:yabn:1.0.3")
    forgeRuntimeLibrary("com.teamresourceful:yabn:1.0.3")
    compileOnly("com.teamresourceful:bytecodecs:1.0.2")
    forgeRuntimeLibrary("com.teamresourceful:bytecodecs:1.0.2")

    modImplementation(group = "com.cobblemon", name = "forge", version = "$cobblemonVersion+1.20.1-SNAPSHOT")
    //runtimeOnly("thedarkcolour:kotlinforforge-4.5.0-all")
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to version)
    }
}
