package cc.mewcraft.worldlink

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder

class PluginSettings {
    init {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()
    }

    val normalScale: Double by lazy { plugin.config.getDouble("scale.normal") }
    val netherScale: Double by lazy { plugin.config.getDouble("scale.nether") }
    val endScale: Double by lazy { plugin.config.getDouble("scale.end") }

    val portalLinks: Multimap<String, String> by lazy {
        val map = MultimapBuilder.hashKeys().hashSetValues().build<String, String>()
        with(plugin.config.getConfigurationSection("link")) {
            if (this == null) error("link")
            for (from in getKeys(false)) {
                val toList = this.getStringList(from)
                map.putAll(from, toList)
            }
        }
        map
    }
}