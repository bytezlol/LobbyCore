package net.pikzstudios.listener;

import net.kyori.adventure.text.Component;
import net.pikzstudios.utils.ConfigUtil;
import net.pikzstudios.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import net.pikzstudios.LobbyCore;

import java.util.Map;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener() {
        LobbyCore.getInstance().getServer().getPluginManager().registerEvents(this, LobbyCore.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        final var player = event.getPlayer();

        event.joinMessage(Component.empty());

        if (ConfigUtil.joinEnabled()) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                MessageUtil.send(onlinePlayer, "player-join", Map.of("%player%", player.getName()));
            }
        }
    }
}
