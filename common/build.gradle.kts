architectury {
    val enabledPlatforms: String by rootProject
    common(enabledPlatforms.split(","))
}

dependencies {
    val cobblemonVersion: String by project

    modCompileOnly("com.cobblemon:mod:$cobblemonVersion+1.20.1-SNAPSHOT") {
        isTransitive = false
    }
}