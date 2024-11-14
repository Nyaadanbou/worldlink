import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("nyaadanbou-conventions.repositories")
    id("nyaadanbou-conventions.copy-jar")
    id("worldlink-conventions.commons")
    alias(libs.plugins.pluginyml.paper)
}

group = "cc.mewcraft.worldlink"
version = "0.0.1-SNAPSHOT"
description = "Handles the Notch style nether portals across multiple worlds!"

dependencies {
    implementation(local.lang.bukkit)

    compileOnly(local.paper)
    compileOnly(local.helper)
}

tasks {
    copyJar {
        environment = "paper"
        jarFileName = "worldlink-${project.version}.jar"
    }
}

paper {
    main = "cc.mewcraft.worldlink.WorldLinkPlugin"
    name = "WorldLink"
    version = "${project.version}"
    description = project.description
    apiVersion = "1.19"
    author = "Nailm"
    serverDependencies {
        register("helper") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}