@file:Suppress("MemberVisibilityCanBePrivate")

package cc.mewcraft.worldlink

import cc.mewcraft.spatula.message.Translations
import me.lucko.helper.Helper
import me.lucko.helper.plugin.ExtendedJavaPlugin
import net.kyori.adventure.text.logger.slf4j.ComponentLogger

class WorldLinkPlugin : ExtendedJavaPlugin() {
    lateinit var settings: PluginSettings private set
    lateinit var languages: Translations private set
    lateinit var nameLinks: WorldNameLinks private set
    override fun enable() {
        settings = PluginSettings()
        languages = Translations(this)
        nameLinks = WorldNameLinks(settings)
        PortalListener(nameLinks).also { registerListener(it) }
    }
}

val logger: ComponentLogger by lazy { plugin.componentLogger }

val plugin: WorldLinkPlugin by lazy { Helper.plugins().getPlugin("WorldLink") as WorldLinkPlugin }