package wtf.bytez.manager;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import lombok.Getter;
import wtf.bytez.LobbyCore;
import wtf.bytez.utils.ConfigUtil;
import wtf.bytez.utils.FileUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class DiscordManager {

    private final @NotNull AtomicReference<Snapshot> snapshot = new AtomicReference<>(new Snapshot(0, 0, "Unknown", 0L));
    private @Nullable WrappedTask refreshTask;

    @Getter
    private String inviteCode = "";
    private long refreshIntervalSeconds = 300L;
    @Getter
    private boolean enabled = false;

    public DiscordManager() {
        loadConfig();
        if (enabled) start();
    }

    private void loadConfig() {
        if (!ConfigUtil.discordEnabled()) {
            this.enabled = false;
            return;
        }

        final FileConfiguration config = FileUtil.get("discord.yml");

        this.enabled = config.getBoolean("discord.enabled", true);
        this.refreshIntervalSeconds = Math.max(60L, config.getLong("discord.refresh-interval", 300L));

        final String invite = config.getString("discord.invite", "").trim();
        this.inviteCode = extractCode(invite);

        if (inviteCode.isEmpty()) {
            this.enabled = false;
            LobbyCore.getInstance().getLogger().warning("Discord invite is empty — placeholders disabled.");
        }
    }

    private @NotNull String extractCode(final @NotNull String raw) {
        if (raw.isEmpty()) return "";
        String code = raw;
        final int slash = code.lastIndexOf('/');
        if (slash >= 0) code = code.substring(slash + 1);
        final int question = code.indexOf('?');
        if (question >= 0) code = code.substring(0, question);
        return code;
    }

    private void start() {
        refreshTask = LobbyCore.getInstance().getFoliaLib().getScheduler()
                .runTimerAsync(this::refresh, 0L, refreshIntervalSeconds * 20L);
    }

    public void refresh() {
        if (inviteCode.isEmpty() || !enabled) return;

        try {
            final URI uri = URI.create("https://discord.com/api/v10/invites/" + inviteCode + "?with_counts=true");
            final HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "LobbyCore-DiscordChecker/1.0");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            final int responseCode = conn.getResponseCode();

            if (responseCode == 404) {
                LobbyCore.getInstance().getLogger().warning("Discord invite '" + inviteCode + "' does not exist — disabling checks. Update discord.yml and run /lobbycore reload.");
                this.enabled = false;
                shutdown();
                return;
            }

            if (responseCode != 200) {
                LobbyCore.getInstance().getLogger().warning("Discord invite check failed: HTTP " + responseCode + " (code: " + inviteCode + ")");
                return;
            }

            final StringBuilder body = new StringBuilder();
            try (final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) body.append(line);
            }

            final String json = body.toString();
            final int members = parseInt(json, "approximate_member_count");
            final int online = parseInt(json, "approximate_presence_count");
            final String guildName = parseString(json, "name");

            snapshot.set(new Snapshot(members, online, guildName, System.currentTimeMillis()));
        } catch (Exception e) {
            LobbyCore.getInstance().getLogger().log(Level.WARNING,
                    "Failed to fetch Discord invite data: " + e.getMessage());
        }
    }

    private int parseInt(final @NotNull String json, final @NotNull String key) {
        final String search = "\"" + key + "\":";
        final int idx = json.indexOf(search);
        if (idx < 0) return 0;

        int start = idx + search.length();
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;

        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;

        try {
            return Integer.parseInt(json.substring(start, end));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private @NotNull String parseString(final @NotNull String json, final @NotNull String key) {
        final String search = "\"" + key + "\":\"";
        final int idx = json.indexOf(search);
        if (idx < 0) return "Unknown";

        final int start = idx + search.length();
        int end = start;
        while (end < json.length() && json.charAt(end) != '"') {
            if (json.charAt(end) == '\\') end++;
            end++;
        }

        if (end >= json.length()) return "Unknown";
        return json.substring(start, end);
    }

    public int getMembers() {
        return snapshot.get().members();
    }

    public int getOnline() {
        return snapshot.get().online();
    }

    public @NotNull String getGuildName() {
        return snapshot.get().guildName();
    }

    public long getLastFetch() {
        return snapshot.get().lastFetch();
    }

    public void reload() {
        shutdown();
        loadConfig();
        if (enabled) start();
    }

    public void shutdown() {
        if (refreshTask != null) {
            refreshTask.cancel();
            refreshTask = null;
        }
    }

    private record Snapshot(int members, int online, String guildName, long lastFetch) {}
}