@file:Suppress("MemberVisibilityCanBePrivate")

package cc.mewcraft.worldlink

import me.lucko.helper.Commands
import me.lucko.helper.plugin.ExtendedJavaPlugin
import net.kyori.adventure.text.logger.slf4j.ComponentLogger

val plugin: WorldLinkPlugin
    get() = WorldLinkPlugin.instance ?: error("plugin is not initialized")

val logger: ComponentLogger
    get() = plugin.componentLogger

val languages: Translations
    get() = plugin.languages

val nameLinks: WorldNameLinks
    get() = plugin.nameLinks

class WorldLinkPlugin : ExtendedJavaPlugin() {
    companion object Shared {
        var instance: WorldLinkPlugin? = null
    }

    lateinit var settings: PluginSettings
        private set
    lateinit var languages: Translations
        private set
    lateinit var nameLinks: WorldNameLinks
        private set

    override fun load() {
        instance = this
    }

    override fun enable() {
        settings = PluginSettings()
        languages = Translations(this)
        nameLinks = WorldNameLinks()

        PortalListener().also { registerTerminableListener(it).bindWith(this) }

        Commands.create()
            .assertConsole()
            .description("Reload the plugin settings.")
            .handler { ctx ->
                settings.reload()
                languages.reload()
                ctx.reply("Plugin settings reloaded.")
            }
            .registerAndBind(this, "worldlink")
    }

    override fun disable() {
        instance = null
    }
}
