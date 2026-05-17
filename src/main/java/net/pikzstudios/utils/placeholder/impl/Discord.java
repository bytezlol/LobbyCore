package net.pikzstudios.utils.placeholder.impl;

import net.pikzstudios.LobbyCore;
import net.pikzstudios.manager.DiscordManager;
import net.pikzstudios.utils.placeholder.Placeholder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class Discord implements Placeholder {

    @Override
    public @NotNull String identifier() {
        return "discord";
    }

    @Override
    public @Nullable String resolve(final @Nullable OfflinePlayer player, final @NotNull String params) {
        final DiscordManager manager = LobbyCore.getInstance().getDiscordManager();
        if (manager == null) return "0";

        return switch (params.toLowerCase(Locale.ROOT)) {
            case "members", "member_count", "" -> String.valueOf(manager.getMembers());
            case "members_formatted" -> formatted(manager.getMembers());
            case "online", "online_count" -> String.valueOf(manager.getOnline());
            case "online_formatted" -> formatted(manager.getOnline());
            case "name", "guild", "guild_name" -> manager.getGuildName();
            case "invite", "invite_code" -> manager.getInviteCode();
            default -> "0";
        };
    }

    private @NotNull String formatted(final int amount) {
        return String.format(Locale.GERMANY, "%,d", amount);
    }
}