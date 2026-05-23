package wtf.bytez.command;

import wtf.bytez.LobbyCore;
import wtf.bytez.utils.ConfigUtil;
import wtf.bytez.utils.FileUtil;
import wtf.bytez.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LobbyCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("reload");

    public LobbyCommand() {
        Objects.requireNonNull(LobbyCore.getInstance().getCommand("lobbycore")).setExecutor(this);
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String[] args) {
        if (!ConfigUtil.commandEnabled("lobbycore")) return true;

        final String permission = ConfigUtil.commandPermission("lobbycore");
        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            MessageUtil.send(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.send(sender, "usage");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            handleReload(sender);
            return true;
        }

        MessageUtil.send(sender, "usage");
        return true;
    }

    private void handleReload(final @NotNull CommandSender sender) {
        final long start = System.currentTimeMillis();

        try {
            FileUtil.reloadAll();
            LobbyCore.getInstance().getDiscordManager().reload();
            LobbyCore.getInstance().getChatFormatManager().reload();
        } catch (Exception e) {
            MessageUtil.send(sender, "reload-failed", Map.of("%error%", String.valueOf(e.getMessage())));
            return;
        }

        final long took = System.currentTimeMillis() - start;
        MessageUtil.send(sender, "reload-success", Map.of("%time%", String.valueOf(took)));
    }

    @Override
    public @NotNull List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String[] args) {
        final String permission = ConfigUtil.commandPermission("lobbycore");
        if (!permission.isEmpty() && !sender.hasPermission(permission)) return List.of();

        if (args.length == 1) {
            final String partial = args[0].toLowerCase(Locale.ROOT);
            final List<String> result = new ArrayList<>();
            for (final String sub : SUBCOMMANDS) {
                if (sub.startsWith(partial)) result.add(sub);
            }
            return result;
        }

        return List.of();
    }
}