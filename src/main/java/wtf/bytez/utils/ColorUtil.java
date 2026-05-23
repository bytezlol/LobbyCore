package wtf.bytez.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class ColorUtil {

    private static final String[] MINI_TAGS = {
            "<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>",
            "<dark_red>", "<dark_purple>", "<gold>", "<gray>",
            "<dark_gray>", "<blue>", "<green>", "<aqua>",
            "<red>", "<light_purple>", "<yellow>", "<white>",
            "<obf>", "<b>", "<st>", "<u>", "<i>", "<reset>"
    };

    private ColorUtil() {
        throw new AssertionError();
    }

    public static @NotNull Component parse(final @NotNull String input) {
        return MiniMessage.miniMessage().deserialize(toMiniMessage(input)).decoration(TextDecoration.ITALIC, false);
    }

    public static @NotNull String toMiniMessage(final @NotNull String input) {
        return convertUrls(convertLegacy(convertHex(input)));
    }

    private static @NotNull String convertHex(final @NotNull String input) {
        final Matcher matcher = Pattern.compile("(?:&#|#|<#)([0-9A-Fa-f]{6})>?").matcher(input);
        final StringBuilder builder = new StringBuilder();
        while (matcher.find()) matcher.appendReplacement(builder, "<#" + matcher.group(1) + ">");
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static @NotNull String convertLegacy(final @NotNull String input) {
        final Matcher matcher = Pattern.compile("[§&]([0-9a-fk-orA-FK-OR])").matcher(input);
        final StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            final int index = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(matcher.group(1).charAt(0)));
            final String replacement = index >= 0 ? MINI_TAGS[index] : "";
            matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private static @NotNull String convertUrls(final @NotNull String input) {
        final Matcher matcher = Pattern.compile("(?<!['\"])(?:https?://)?([a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,})(/[^\\s<>\"']*)?(\\s|$)").matcher(input);
        final StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            final String full = matcher.group(0).stripTrailing();
            final String url = full.startsWith("http") ? full : "https://" + full;
            final String trailing = matcher.group(3) != null ? matcher.group(3) : "";
            matcher.appendReplacement(builder, Matcher.quoteReplacement("<click:open_url:'" + url + "'>" + full + "</click>" + trailing));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    public static @NotNull String stripLegacyCodes(final @NotNull String input) {
        return input.replaceAll("[&§][0-9a-fk-orA-FK-OR]", "");
    }

    public static @NotNull String stripHexCodes(final @NotNull String input) {
        return input.replaceAll("[&§]#[0-9a-fA-F]{6}", "").replaceAll("[&§]x([&§][0-9a-fA-F]){6}", "");
    }
}