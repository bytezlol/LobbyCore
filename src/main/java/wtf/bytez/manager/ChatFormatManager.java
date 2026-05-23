package wtf.bytez.manager;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.bytez.utils.ColorUtil;
import wtf.bytez.utils.ConfigUtil;

public class ChatFormatManager {

    private static final String DEFAULT_FORMAT = "{prefix}<gray>{name} <dark_gray>» <white>{message}";
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final boolean luckPermsAvailable;
    private final boolean placeholderApiAvailable;

    @Getter
    private @NotNull String format = DEFAULT_FORMAT;

    public ChatFormatManager() {
        this.luckPermsAvailable = Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
        this.placeholderApiAvailable = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        loadConfig();
    }

    public void loadConfig() {
        final String configured = ConfigUtil.chatFormat();
        this.format = configured.isBlank() ? DEFAULT_FORMAT : configured;
    }

    public void reload() {
        loadConfig();
    }

    public @NotNull Component render(final @NotNull Player player, final @NotNull String rawMessage) {
        final String message = processMessage(player, rawMessage);
        final String resolved = buildFormat(player).replace("{message}", message);
        return MINI.deserialize(resolved);
    }

    private @NotNull String buildFormat(final @NotNull Player player) {
        final String prefix = resolveLuckPermsMeta(player, true);
        final String suffix = resolveLuckPermsMeta(player, false);

        String result = format
                .replace("{prefix}", prefix)
                .replace("{suffix}", suffix)
                .replace("{name}", player.getName())
                .replace("{world}", player.getWorld().getName());

        result = ColorUtil.toMiniMessage(result);

        if (placeholderApiAvailable) {
            result = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, result);
        }

        return result;
    }

    private @NotNull String resolveLuckPermsMeta(final @NotNull Player player, final boolean prefix) {
        if (!luckPermsAvailable) return "";

        final LuckPerms luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
        if (luckPerms == null) return "";

        final CachedMetaData meta = luckPerms.getPlayerAdapter(Player.class).getMetaData(player);
        final String raw = prefix ? meta.getPrefix() : meta.getSuffix();
        return raw == null ? "" : ColorUtil.toMiniMessage(raw);
    }

    private @NotNull String processMessage(final @NotNull Player player, final @NotNull String message) {
        final boolean colors = player.hasPermission("lobbycore.chat.colors");
        final boolean rgb = player.hasPermission("lobbycore.chat.rgb");

        if (colors && rgb) return ColorUtil.toMiniMessage(message);
        if (colors) return ColorUtil.stripHexCodes(ColorUtil.toMiniMessage(message));
        if (rgb) return ColorUtil.stripLegacyCodes(message);
        return ColorUtil.stripLegacyCodes(ColorUtil.stripHexCodes(message));
    }
}