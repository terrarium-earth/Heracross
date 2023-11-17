import dev.architectury.plugin.ArchitectPluginExtension
import groovy.json.StringEscapeUtils
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version("1.7.10")

    id("maven-publish")
    id("com.teamresourceful.resourcefulgradle") version "0.0.+"
    id("dev.architectury.loom") version "1.2-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT" apply false
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val minecraftVersion: String by project
    val modLoader = project.name
    val modId = rootProject.name
    val isCommon = modLoader == rootProject.projects.common.name

    base {
        archivesName.set("${rootProject.name}-$modLoader-$minecraftVersion")
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    repositories {
        mavenCentral()
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.msrandom.net/repository/root")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        maven(url = "https://maven.impactdev.net/repository/development/")
        maven("https://thedarkcolour.github.io/KotlinForForge/")
    }

    dependencies {
        val heraclesVersion: String by project
        val resourcefulLibVersion: String by project

        "minecraft"("::${minecraftVersion}")
        "mappings"(project.the<LoomGradleExtensionAPI>().officialMojangMappings())

        compileOnly(group = "org.jetbrains", name = "annotations", version = "24.0.1")
        "modImplementation"(group = "earth.terrarium.heracles", name = "heracles-$modLoader-1.20.1", version = heraclesVersion)
        "modCompileOnly"(group = "com.teamresourceful.resourcefullib", name = "resourcefullib-common-$minecraftVersion", version = resourcefulLibVersion)
    }

    java {
        withSourcesJar()
    }

    tasks.jar {
        archiveClassifier.set("dev")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveClassifier.set(null as String?)
    }

    if (!isCommon) {
        configure<ArchitectPluginExtension> {
            platformSetupLoomIde()
        }

        sourceSets.main {
            val main = this

            rootProject.projects.common.dependencyProject.sourceSets.main {
                main.java.source(java)
                main.resources.source(resources)
            }
        }

        dependencies {
            compileOnly(rootProject.projects.common)
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "$modId-$modLoader-$minecraftVersion"
                from(components["java"])

                pom {
                    name.set("Heracles $modLoader")
                    url.set("https://github.com/terrarium-earth/$modId")

                    scm {
                        connection.set("git:https://github.com/terrarium-earth/$modId.git")
                        developerConnection.set("git:https://github.com/terrarium-earth/$modId.git")
                        url.set("https://github.com/terrarium-earth/$modId")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                setUrl("https://maven.resourcefulbees.com/repository/terrarium/")
                credentials {
                    username = System.getenv("MAVEN_USER")
                    password = System.getenv("MAVEN_PASS")
                }
            }
        }
    }
}

resourcefulGradle {
    templates {
        register("embed") {
            val minecraftVersion: String by project
            val version: String by project
            val changelog: String = file("changelog.md").readText(Charsets.UTF_8)
            val fabricLink: String? = System.getenv("FABRIC_RELEASE_URL")
            val forgeLink: String? = System.getenv("FORGE_RELEASE_URL")

            source.set(file("templates/embed.json.template"))
            injectedValues.set(mapOf(
                    "minecraft" to minecraftVersion,
                    "version" to version,
                    "changelog" to StringEscapeUtils.escapeJava(changelog),
                    "fabric_link" to fabricLink,
                    "forge_link" to forgeLink,
            ))
        }
    }
}
