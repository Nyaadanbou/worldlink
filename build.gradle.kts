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
    compileOnly(project(":mewcore"))

    compileOnly(libs.server.paper)

    // plugin libs
    compileOnly(libs.helper) { isTransitive = false }

    // shaded libs
}

paper {
    main = "cc.mewcraft.worldlink.WorldLinkPlugin"
    name = project.ext.get("name") as String
    version = "${project.version}"
    description = project.description
    apiVersion = "1.19"
    authors = listOf("Nailm")

    serverDependencies {
        register("Kotlin") {
            required = true
            joinClasspath = true
            load = PaperPluginDescription.RelativeLoadOrder.OMIT
        }
        register("helper") {
            required = true
            joinClasspath = true
            load = PaperPluginDescription.RelativeLoadOrder.OMIT
        }
        register("MewCore") {
            required = true
            joinClasspath = true
            load = PaperPluginDescription.RelativeLoadOrder.OMIT
        }
    }
}