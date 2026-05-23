package wtf.bytez;

import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import lombok.SneakyThrows;
import wtf.bytez.command.DiscordCommand;
import wtf.bytez.command.LobbyCommand;
import wtf.bytez.command.StoreCommand;
import wtf.bytez.manager.DiscordManager;
import wtf.bytez.utils.ConfigUtil;
import wtf.bytez.utils.FileUtil;
import wtf.bytez.utils.PlaceholderUtil;
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

        if (ConfigUtil.devMode()) {
            getLogger().info("Developer mode is enabled!");
        }

        this.foliaLib = new FoliaLib(this);
        this.discordManager = new DiscordManager();

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