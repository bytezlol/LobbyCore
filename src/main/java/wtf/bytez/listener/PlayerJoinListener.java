package wtf.bytez.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import wtf.bytez.LobbyCore;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener() {
        LobbyCore.getInstance().getServer().getPluginManager().registerEvents(this, LobbyCore.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
    }
}
