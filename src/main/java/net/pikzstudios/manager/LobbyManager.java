package net.pikzstudios.manager;

import lombok.Getter;
import net.pikzstudios.utils.FileUtil;
import net.pikzstudios.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * MapManager
 *
 * @author Kai
 * @since 5/23/2026
 */
@Getter
public class LobbyManager {

    @Getter
    private static LobbyManager instance;
    private Location lobbyLocation;

    public LobbyManager() {
        instance = this;
        lobbyLocation = load("lobby");
    }

    private Location load(String path) {
        final FileConfiguration spawnConfig = FileUtil.get("lobby.yml");
        final String serialized = spawnConfig.getString(path);

        if (serialized == null || serialized.isEmpty()) return null;
        return LocationUtil.fromString(serialized);
    }

    public void saveLobby(Player player) {
        lobbyLocation = player.getLocation();
        save("lobby", lobbyLocation);
    }

    private void save(String path, Location loc) {
        final FileConfiguration spawnConfig = FileUtil.get("lobby.yml");
        spawnConfig.set(path, LocationUtil.toString(loc));
        FileUtil.save("lobby.yml");
    }
}