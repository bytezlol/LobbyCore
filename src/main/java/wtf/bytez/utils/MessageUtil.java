package wtf.bytez.utils;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class MessageUtil {

    private MessageUtil() {
        throw new AssertionError();
    }

    public static @NotNull String getPrefix() {
        return FileUtil.get("lang.yml").getString("prefix", "");
    }

    public static @NotNull String getRaw(final @NotNull String key) {
        return FileUtil.get("lang.yml").getString(key, "<red>Missing message: " + key);
    }

    public static @NotNull List<String> getRawList(final @NotNull String key) {
        return FileUtil.get("lang.yml").getStringList(key);
    }

    public static @NotNull String get(final @NotNull String key) {
        return getPrefix() + getRaw(key);
    }

    public static @NotNull String get(final @NotNull String key, final @NotNull Map<String, String> placeholders) {
        return apply(get(key), placeholders);
    }

    public static void send(final @NotNull CommandSender sender, final @NotNull String key) {
        sender.sendMessage(ColorUtil.parse(get(key)));
    }

    public static void send(final @NotNull CommandSender sender, final @NotNull String key, final @NotNull Map<String, String> placeholders) {
        sender.sendMessage(ColorUtil.parse(get(key, placeholders)));
    }

    public static void sendList(final @NotNull CommandSender sender, final @NotNull String key) {
        for (final String line : getRawList(key)) {
            sender.sendMessage(ColorUtil.parse(line));
        }
    }

    public static void sendList(final @NotNull CommandSender sender, final @NotNull String key, final @NotNull Map<String, String> placeholders) {
        for (final String line : getRawList(key)) {
            sender.sendMessage(ColorUtil.parse(apply(line, placeholders)));
        }
    }

    private static @NotNull String apply(final @NotNull String input, final @NotNull Map<String, String> placeholders) {
        String result = input;
        for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}