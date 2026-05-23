package wtf.bytez.utils.placeholder;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Placeholder {

    @NotNull String identifier();

    @Nullable String resolve(final @Nullable OfflinePlayer player, final @NotNull String params);
}