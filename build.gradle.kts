import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("cc.mewcraft.repo-conventions")
    id("cc.mewcraft.kotlin-conventions")
    id("cc.mewcraft.deploy-conventions")
    alias(libs.plugins.pluginyml.paper)
}

project.ext.set("name", "WorldLink")

group = "cc.mewcraft"
version = "1.0.0"
description = "Handles the Notch style nether portals across multiple worlds!"

dependencies {
    // server
    compileOnly(libs.server.paper)

    // helper
    compileOnly(libs.helper) { isTransitive = false }

    // internal
    implementation(project(":spatula:bukkit:message"))
}

paper {
    main = "cc.mewcraft.worldlink.WorldLinkPlugin"
    name = project.ext.get("name") as String
    version = "${project.version}"
    description = project.description
    apiVersion = "1.19"
    author = "Nailm"
    serverDependencies {
        register("Kotlin") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("helper") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}