package wtf.bytez.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import wtf.bytez.LobbyCore;
import wtf.bytez.utils.ConfigUtil;

public class PlayerChatListener implements Listener {

    public PlayerChatListener() {
        LobbyCore.getInstance().getServer().getPluginManager().registerEvents(this, LobbyCore.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final @NotNull AsyncChatEvent event) {
        if (!ConfigUtil.chatEnabled()) return;

        final Player player = event.getPlayer();
        final String raw = LegacyComponentSerializer.legacySection().serialize(event.message());

        event.renderer((source, sourceDisplayName, msg, audience) -> LobbyCore.getInstance().getChatFormatManager().render(player, raw));
    }
}