package wtf.bytez.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import wtf.bytez.LobbyCore;
import wtf.bytez.utils.ColorUtil;
import wtf.bytez.utils.ConfigUtil;
import wtf.bytez.utils.MessageUtil;

public class PlayerQuitListener implements Listener {

    public PlayerQuitListener() {
        LobbyCore.getInstance().getServer().getPluginManager().registerEvents(this, LobbyCore.getInstance());
    }

    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        if (!ConfigUtil.leaveEnabled()) {
            event.quitMessage(Component.empty());
            return;
        }

        final String raw = MessageUtil.getRaw("leave-message");
        final String formatted = raw.replace("%player%", event.getPlayer().getName());
        event.quitMessage(ColorUtil.parse(formatted));
    }
}