@file:Suppress("MemberVisibilityCanBePrivate")

package cc.mewcraft.worldlink

import cc.mewcraft.mewcore.message.Translations
import cc.mewcraft.mewcore.plugin.MeowJavaPlugin
import me.lucko.helper.Helper
import net.kyori.adventure.text.logger.slf4j.ComponentLogger

class WorldLinkPlugin : MeowJavaPlugin() {
    lateinit var settings: PluginSettings private set
    lateinit var languages: Translations private set
    lateinit var nameLinks: WorldNameLinks private set
    override fun enable() {
        settings = PluginSettings()
        languages = Translations(this)
        nameLinks = WorldNameLinks(settings)
        PortalListener(nameLinks).also { registerListener(it).bindWith(this) }
    }
}

val logger: ComponentLogger by lazy { plugin.componentLogger }

val plugin: WorldLinkPlugin by lazy { Helper.plugins().getPlugin("WorldLink") as WorldLinkPlugin }