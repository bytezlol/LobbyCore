package net.pikzstudios.utils.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pikzstudios.LobbyCore;
import net.pikzstudios.utils.PlaceholderUtil;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "lobbycore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "USpigot";
    }

    @Override
    public @NotNull String getVersion() {
        return LobbyCore.getInstance().getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onRequest(final @Nullable OfflinePlayer player, final @NotNull String params) {
        final int separator = params.indexOf('_');
        final String identifier;
        final String rest;

        if (separator == -1) {
            identifier = params.toLowerCase();
            rest = "";
        } else {
            identifier = params.substring(0, separator).toLowerCase();
            rest = params.substring(separator + 1);
        }

        final Placeholder placeholder = PlaceholderUtil.get(identifier);
        if (placeholder == null) return null;

        return placeholder.resolve(player, rest);
    }
}