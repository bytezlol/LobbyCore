package net.pikzstudios.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pikzstudios.utils.ConfigUtil;
import net.pikzstudios.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import net.pikzstudios.LobbyCore;

import java.util.Map;

public class PlayerQuitListener implements Listener {

    public PlayerQuitListener() {
        LobbyCore.getInstance().getServer().getPluginManager().registerEvents(this, LobbyCore.getInstance());
    }

    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        final var player = event.getPlayer();

        if (ConfigUtil.leaveEnabled()) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                MessageUtil.send(onlinePlayer, "player-quit", Map.of("%player%", player.getName()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final @NotNull AsyncChatEvent event) {
        final var player = event.getPlayer();
    }
}
