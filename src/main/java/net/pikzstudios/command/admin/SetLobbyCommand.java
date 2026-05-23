package net.pikzstudios.command.admin;

import net.pikzstudios.LobbyCore;
import net.pikzstudios.manager.LobbyManager;
import net.pikzstudios.utils.ConfigUtil;
import net.pikzstudios.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * SetLobbyCommand
 *
 * @author Kai
 * @since 5/23/2026
 */
public class SetLobbyCommand implements CommandExecutor {

    public SetLobbyCommand() {
        Objects.requireNonNull(LobbyCore.getInstance().getCommand("setlobby")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!ConfigUtil.commandEnabled("setlobby")) return true;
        if (!(sender instanceof final Player player)) return false;

        final String permission = ConfigUtil.commandPermission("setlobby");
        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            MessageUtil.send(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            LobbyManager.getInstance().saveLobby(player);
            MessageUtil.send(sender, "lobby-set-success");
            return true;
        }

        MessageUtil.send(sender, "setlobby-usage");
        return true;
    }
 }