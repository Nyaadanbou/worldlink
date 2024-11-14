package cc.mewcraft.worldlink;

import de.themoep.utils.lang.bukkit.LanguageManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TranslationBuilder {
    private static final String PLACEHOLDER_PREFIX = "<";
    private static final String PLACEHOLDER_SUFFIX = ">";

    private final LanguageManager languageManager;
    private final String translationKey;
    private final Map<String, Object> stringReplacements;
    private final TagResolver.Builder tagResolvers;
    private Locale locale;

    TranslationBuilder(final LanguageManager languageManager, final String translationKey) {
        this.languageManager = languageManager;
        this.translationKey = translationKey;
        this.locale = Locale.CHINESE;
        this.stringReplacements = new HashMap<>();
        this.tagResolvers = TagResolver.builder();
    }

    /* Builder functions */

    public TranslationBuilder replace(String key, Object value) {
        this.stringReplacements.put(PLACEHOLDER_PREFIX + key + PLACEHOLDER_SUFFIX, value);
        return this;
    }

    public TranslationBuilder replace(String... replacements) {
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            if (replacements[i] == null)
                continue;
            this.stringReplacements.put(PLACEHOLDER_PREFIX + replacements[i] + PLACEHOLDER_SUFFIX, replacements[i + 1]);
        }
        return this;
    }

    public TranslationBuilder resolver(TagResolver resolver) {
        this.tagResolvers.resolver(resolver);
        return this;
    }

    public TranslationBuilder resolver(TagResolver... resolvers) {
        this.tagResolvers.resolvers(resolvers);
        return this;
    }

    public TranslationBuilder locale(Audience audience) {
        if (audience instanceof Player player)
            this.locale = player.locale();
        return this;
    }

    public TranslationBuilder locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    /* Terminal functions */

    /**
     * Gets the plain string of the translated message.
     * <p>
     * This is a terminal operation.
     *
     * @return the translated message with string replacements applied
     */
    public String plain() {
        String string = this.languageManager.getConfig(this.locale.getLanguage()).get(this.translationKey);
        for (Map.Entry<String, Object> entry : this.stringReplacements.entrySet()) {
            string = string.replace(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return string;
    }

    /**
     * Gets a {@link Component} of the translated message,
     * with all string replacements first applied, followed by passing the
     * <p>
     * This is a terminal operation.
     *
     * @return the translated message with string replacements and tags applied
     */
    public Component component() {
        return MiniMessage.miniMessage().deserialize(plain(), this.tagResolvers.build());
    }

    /**
     * Sends this translated message to the viewer through chat.
     * <p>
     * This is a terminal operation.
     */
    public void send(Audience audience) {
        locale(audience);
        audience.sendMessage(component());
    }

    /**
     * Sends this translated message to the viewer through title.
     * <p>
     * This is a terminal operation.
     */
    public void title(Audience audience) {
        locale(audience);
        audience.showTitle(Title.title(Component.empty(), component()));
    }

    /**
     * Sends this translated message to the viewer through action bar.
     * <p>
     * This is a terminal operation.
     */
    public void actionBar(Audience audience) {
        locale(audience);
        audience.sendActionBar(component());
    }
}