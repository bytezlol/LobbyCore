package wtf.bytez.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import wtf.bytez.LobbyCore;
import wtf.bytez.utils.placeholder.Placeholder;
import wtf.bytez.utils.placeholder.PlaceholderHook;
import wtf.bytez.utils.placeholder.impl.Discord;

public final class PlaceholderUtil {

    private static final Map<String, Placeholder> REGISTRY = new ConcurrentHashMap<>();
    private static @Nullable PlaceholderHook hook;

    private PlaceholderUtil() {
        throw new AssertionError();
    }

    public static void bootstrap() {
        register(new Discord());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            LobbyCore.getInstance().getLogger().warning("PlaceholderAPI not found, placeholders disabled.");
            return;
        }

        hook = new PlaceholderHook();
        hook.register();
        LobbyCore.getInstance().getLogger().info("Hooked into PlaceholderAPI with " + REGISTRY.size() + " placeholders.");
    }

    public static void shutdown() {
        if (hook != null) {
            hook.unregister();
            hook = null;
        }
        REGISTRY.clear();
    }

    public static void register(final @NotNull Placeholder placeholder) {
        REGISTRY.put(placeholder.identifier().toLowerCase(), placeholder);
    }

    public static @Nullable Placeholder get(final @NotNull String identifier) {
        return REGISTRY.get(identifier.toLowerCase());
    }
}