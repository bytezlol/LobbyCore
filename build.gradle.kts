plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

group = "wtf.bytez"
version = "1.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.tcoded.com/releases")
    maven("https://repo.extendedclip.com/releases/")
}

dependencies {
    compileOnly(libs.paper.api)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.folialib)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.luckperms)
}

tasks {
    jar {
        enabled = false
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        relocate("com.tcoded.folialib", "wtf.bytez.libs.folialib")

        manifest {
            attributes["Implementation-Version"] = rootProject.version
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    defaultTasks("build")

    val mcVersion = "1.21.4"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(mcVersion)
        runDirectory = rootDir.resolve("run/paper/$mcVersion")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            url("https://github.com/ViaVersion/ViaVersion/releases/download/5.7.1/ViaVersion-5.7.1.jar")
            url("https://github.com/ViaVersion/ViaBackwards/releases/download/5.7.1/ViaBackwards-5.7.1.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}