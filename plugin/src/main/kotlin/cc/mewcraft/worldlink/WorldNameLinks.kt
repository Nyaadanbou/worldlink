package cc.mewcraft.worldlink

import com.google.common.collect.Multimap
import me.lucko.helper.Helper
import org.bukkit.PortalType
import org.bukkit.World
import org.bukkit.World.Environment

private const val NETHER_SUFFIX = "_nether"
private const val ENDER_SUFFIX = "_the_end"

/**
 * Pure world name links. No actual worlds are handled by this class.
 */
class WorldNameLinks(
    settings: PluginSettings,
) {
    private val nameLinks: Multimap<String, String> by lazy { settings.portalLinks }

    /**
     * Finds the target world for [from] world by name links defined in the config file.
     *
     * The logic of this function is as following:
     * - If the name of world [from] only points to a world, the target world is just the world.
     * - If the name of world [from] points to multiple worlds:
     *   - For Nether Portals, the name suffixed with `_nether` is taken.
     *   - For End Portals, the name suffixed with `_the_end` is taken.
     *   - Note that only a name is taken if there are multiple matches.
     * - If the name of world [from] points to nothing, this function will return `null`.
     *
     * This function also internally checks if the found target world is _legal_, or an exception is thrown. By _legal_, we mean:
     * - For worlds of Normal environment, they must point to the worlds of either Nether or End environment, depending on [portalType].
     * - For worlds of Nether/End environment, they must point to the worlds of Normal environment.
     *
     * @param from current world (i.e. starting world)
     * @param portalType current portal type (i.e. starting portal type)
     * @return the target world of [from] world
     */
    fun findTo(from: World, portalType: PortalType): World? {
        val fromName = from.name
        val possibleTargets = nameLinks.get(fromName)

        // For reference, possible links between dimensions:
        //   overworld -> nether/end
        //   nether -> overworld
        //   end -> overworld

        return when (from.environment) {
            Environment.NORMAL -> {
                when (portalType) {
                    PortalType.NETHER -> possibleTargets
                        .firstOrNull { it.endsWith(NETHER_SUFFIX) }
                        ?.let { Helper.worldNullable(it) }
                        ?.also {
                            check(it.environment == Environment.NETHER) { "Target world environment must be ${Environment.NETHER}" }
                        }

                    PortalType.ENDER -> possibleTargets
                        .firstOrNull { it.endsWith(ENDER_SUFFIX) }
                        ?.let { Helper.worldNullable(it) }
                        ?.also {
                            check(it.environment == Environment.THE_END) { "Target world environment must be ${Environment.THE_END}" }
                        }

                    else -> null
                }
            }

            Environment.NETHER, Environment.THE_END -> {
                if (possibleTargets.size > 1) {
                    logger.warn("Amount of target worlds for `$fromName` is more than 1.")
                    logger.warn("Only the first target world found is taken as the target world.")
                    logger.warn("List of target worlds for `$fromName`: ${possibleTargets.reduce { acc, s -> "`$acc`, `$s`" }}")
                }
                possibleTargets
                    .firstOrNull()
                    ?.let { Helper.worldNullable(it) }
                    ?.also {
                        check(it.environment == Environment.NORMAL) { "Target world environment must be ${Environment.NORMAL}" }
                    }
            }

            else -> null
        }
    }
}