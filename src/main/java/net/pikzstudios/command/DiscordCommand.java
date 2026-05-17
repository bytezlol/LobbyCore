package net.pikzstudios.command;

import net.pikzstudios.LobbyCore;
import net.pikzstudios.utils.ConfigUtil;
import net.pikzstudios.utils.MessageUtil;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordCommand implements CommandExecutor {

    public DiscordCommand() {
        Objects.requireNonNull(LobbyCore.getInstance().getCommand("discord")).setExecutor(this);
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String[] args) {
        if (!ConfigUtil.commandEnabled("discord")) return true;
        if (!(sender instanceof final Player player)) return false;

        final String permission = ConfigUtil.commandPermission("discord");
        if (!permission.isEmpty() && !player.hasPermission(permission)) {
            MessageUtil.send(player, "no-permission");
            return true;
        }

        MessageUtil.sendList(player, "discord-message");
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        return true;
    }
}