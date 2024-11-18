package cc.mewcraft.worldlink;

import de.themoep.utils.lang.bukkit.LanguageManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * A sophisticated builder of all the translated messages,
 * additionally providing convenience methods to quickly
 * send message to an audience.
 */
public class Translations {
    private final LanguageManager languageManager;

    public Translations(final Plugin plugin) {
        this(plugin, "lang", "zh");
    }

    public Translations(final Plugin plugin, String folder) {
        this(plugin, folder, "zh");
    }

    public Translations(final Plugin plugin, String folder, String defaultLocale) {
        this.languageManager = new LanguageManager(plugin, folder, defaultLocale);
        this.languageManager.setProvider(sender -> {
            if (sender instanceof Player)
                return ((Player) sender).locale().getLanguage();
            else
                return null;
        });
    }

    public TranslationBuilder of(String key) {
        return new TranslationBuilder(languageManager, key);
    }

    public void reload() {
        languageManager.loadConfigs();
    }
}
