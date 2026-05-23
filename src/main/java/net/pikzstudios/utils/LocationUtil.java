package net.pikzstudios.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * LocationUtil
 *
 * @author Kai
 * @since 5/23/2026
 */
@UtilityClass
public class LocationUtil {

    @NotNull
    public Location fromString(final @NotNull String input) {
        final String[] parts = input.split("/");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3])
        );
    }

    @NotNull
    public String toString(final @NotNull Location location) {
        return location.getWorld().getName() + "/" +
                (location.getBlockX() + 0.5) + "/" +
                location.getBlockY() + "/" +
                (location.getBlockZ() + 0.5);
    }

}
