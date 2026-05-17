package net.pikzstudios.utils;

import net.pikzstudios.LobbyCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class FileUtil {

    private static final Map<String, FileConfiguration> LOADED = new ConcurrentHashMap<>();
    private static final Map<String, File> FILES = new ConcurrentHashMap<>();

    private FileUtil() {
    }

    public static void load(final @NotNull String name) {
        final File file = new File(LobbyCore.getInstance().getDataFolder(), name);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (final InputStream stream = LobbyCore.getInstance().getResource(name)) {
                if (stream != null) {
                    LobbyCore.getInstance().saveResource(name, false);
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                LobbyCore.getInstance().getLogger().log(Level.SEVERE, "Failed to create file " + name, e);
                return;
            }
        }

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try (final InputStream defaults = LobbyCore.getInstance().getResource(name)) {
            if (defaults != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaults, StandardCharsets.UTF_8)));
            }
        } catch (IOException ignored) {
        }

        FILES.put(name, file);
        LOADED.put(name, config);
    }

    public static @NotNull FileConfiguration get(final @NotNull String name) {
        final FileConfiguration config = LOADED.get(name);
        if (config != null) return config;

        load(name);
        return LOADED.get(name);
    }

    public static void save(final @NotNull String name) {
        final FileConfiguration config = LOADED.get(name);
        final File file = FILES.get(name);
        if (config == null || file == null) return;

        try {
            config.save(file);
        } catch (IOException e) {
            LobbyCore.getInstance().getLogger().log(Level.SEVERE, "Failed to save file " + name, e);
        }
    }

    public static void reload(final @NotNull String name) {
        FILES.remove(name);
        LOADED.remove(name);
        load(name);
    }

    public static void reloadAll() {
        for (final String name : LOADED.keySet().toArray(new String[0])) {
            reload(name);
        }
    }

    public static int loadedCount() {
        return LOADED.size();
    }
}