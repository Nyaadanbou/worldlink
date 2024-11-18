@file:Suppress("UnstableApiUsage")

package cc.mewcraft.worldlink

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import io.papermc.paper.math.FinePosition
import io.papermc.paper.math.Position
import java.util.Collections
import java.util.WeakHashMap

class PluginSettings {
    // overworld 的比例
    val normalScale: Double by ReloadableProperty {
        plugin.config.getDouble("scale.normal", 1.0)
    }

    // nether 的比例
    val netherScale: Double by ReloadableProperty {
        plugin.config.getDouble("scale.nether", 1.0)
    }

    // the_end 的比例
    val theEndScale: Double by ReloadableProperty {
        plugin.config.getDouble("scale.the_end", 1.0)
    }

    // 传送门连接的定义 (比如: x 世界的传送门指向 y 世界的传送门)
    val nameLinks: Multimap<String, String> by ReloadableProperty {
        val map = MultimapBuilder.hashKeys().hashSetValues().build<String, String>()
        val link = requireNotNull(plugin.config.getConfigurationSection("link")) { "link" }
        for (from in link.getKeys(false)) {
            val toList = link.getStringList(from)
            map.putAll(from, toList)
        }
        map
    }

    val theEndSpawn: FinePosition by ReloadableProperty {
        val section = plugin.config.getConfigurationSection("the_end_spawn")
        requireNotNull(section) { "the_end_spawn" }
        val x = section.getDouble("x", 100.5)
        val y = section.getDouble("y", 49.0)
        val z = section.getDouble("z", 0.5)
        Position.fine(x, y, z)
    }

    fun reload() {
        plugin.saveDefaultConfig()
        plugin.reloadConfig()

        ReloadableProperty.reloadAll()
    }
}

/**
 * 究极简单的实现. 不支持并发!
 */
class ReloadableProperty<T>(
    private val loader: () -> T,
) {
    companion object Shared {
        private val objects: MutableSet<ReloadableProperty<*>> = Collections.newSetFromMap(WeakHashMap())

        fun reloadAll() {
            objects.forEach { it.value = null }
        }
    }

    init {
        objects.add(this)
    }

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: Any?): T {
        if (value == null) {
            value = loader()
        }
        return value!!
    }
}