package wtf.bytez.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class ConfigUtil {

    private static final String FILE = "config.yml";

    private ConfigUtil() {
        throw new AssertionError();
    }

    public static @NotNull FileConfiguration config() {
        return FileUtil.get(FILE);
    }

    public static int configVersion() {
        return config().getInt("config-version", 1);
    }

    public static boolean devMode() {
        return config().getBoolean("settings.dev-mode", false);
    }

    public static boolean updateChecker() {
        return config().getBoolean("settings.update-checker", true);
    }

    public static boolean metrics() {
        return config().getBoolean("settings.metrics", true);
    }

    public static boolean discordEnabled() {
        return config().getBoolean("settings.discord-enabled", true);
    }

    public static boolean joinEnabled() {
        return config().getBoolean("settings.join-enabled", true);
    }

    public static boolean leaveEnabled() {
        return config().getBoolean("settings.leave-enabled", true);
    }

    public static boolean chatEnabled() {
        return config().getBoolean("settings.chat-enabled", true);
    }

    public static @NotNull String chatFormat() {
        return config().getString("settings.chat-format", "{prefix}<gray>{name} <dark_gray>» <white>{message}");
    }

    public static boolean commandEnabled(final @NotNull String command) {
        return config().getBoolean("commands." + command + ".enabled", true);
    }

    public static @NotNull String commandPermission(final @NotNull String command) {
        return config().getString("commands." + command + ".permission", "");
    }
}