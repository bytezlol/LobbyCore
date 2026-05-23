package net.pikzstudios;

import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import lombok.SneakyThrows;
import net.pikzstudios.command.DiscordCommand;
import net.pikzstudios.command.LobbyCommand;
import net.pikzstudios.command.LobbyCoreCommand;
import net.pikzstudios.command.StoreCommand;
import net.pikzstudios.command.admin.SetLobbyCommand;
import net.pikzstudios.manager.DiscordManager;
import net.pikzstudios.manager.LobbyManager;
import net.pikzstudios.utils.ConfigUtil;
import net.pikzstudios.utils.FileUtil;
import net.pikzstudios.utils.PlaceholderUtil;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LobbyCore extends JavaPlugin {

    @Getter
    private static LobbyCore instance;

    private FoliaLib foliaLib;
    private DiscordManager discordManager;

    public LobbyCore() {
        instance = this;
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        FileUtil.load("config.yml");
        FileUtil.load("discord.yml");
        FileUtil.load("lang.yml");
        FileUtil.load("lobby.yml");

        if (ConfigUtil.devMode()) {
            getLogger().info("Developer mode is enabled!");
        }

        new LobbyManager();

        this.foliaLib = new FoliaLib(this);
        this.discordManager = new DiscordManager();

        new LobbyCoreCommand();
        new SetLobbyCommand();
        new LobbyCommand();
        new DiscordCommand();
        new StoreCommand();
        
        PlaceholderUtil.bootstrap();
    }

    @Override
    public void onDisable() {
        PlaceholderUtil.shutdown();
        if (discordManager != null) discordManager.shutdown();
        if (foliaLib != null) foliaLib.getScheduler().cancelAllTasks();
    }
}