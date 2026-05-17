<p align="center">
  <b><a>Welcome to LobbyCore's repository!</a></b><br/>
  <i>Lightweight, Folia-compatible lobby management plugin with Discord integration and PlaceholderAPI support.</i>
</p>

## Features
- **Discord Integration** - Automatically fetches live member and online counts from your Discord server via the invite API, cached and refreshed on a configurable interval.
- **PlaceholderAPI Support** - Exposes Discord stats, guild name, and invite code as `%lobbycore_discord_*%` placeholders for use in scoreboards, tab lists, and more.
- **Folia Support** - Built with FoliaLib for full compatibility with both Paper and Folia servers.
- **Discord & Store Commands** - Configurable `/discord` and `/store` commands that send players rich MiniMessage-formatted messages with clickable links.
- **MiniMessage Formatting** - All messages support the full MiniMessage spec, including hex colours, gradients, and click/hover events.
- **Hot Reload** - Reload all configs and reinitialise the Discord manager at runtime with `/lobbycore reload`.
- **Per-command Permissions** - Each command can be individually enabled or disabled, and assigned a custom permission node.

## Placeholders

Requires [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI).

| Placeholder | Description |
|---|---|
| `%lobbycore_discord_members%` | Raw total member count |
| `%lobbycore_discord_members_formatted%` | Formatted member count (e.g. `1.234`) |
| `%lobbycore_discord_online%` | Raw online member count |
| `%lobbycore_discord_online_formatted%` | Formatted online count |
| `%lobbycore_discord_name%` | Guild name |
| `%lobbycore_discord_invite%` | Invite code |

## Commands

| Command | Description | Permission |
|---|---|---|
| `/lobbycore reload` | Reload all configs and the Discord manager | `lobbycore.admin` |
| `/discord` | Show the Discord invite message | *(configurable)* |
| `/store` | Show the store link message | *(configurable)* |

## Permissions

| Permission | Description | Default |
|---|---|---|
| `lobbycore.admin` | Access to `/lobbycore` admin commands | op |

## Libraries
LobbyCore uses and is compiled with the following libraries:
- [FoliaLib](https://github.com/TechnicallyCoded/FoliaLib) (included) - Cross-platform scheduler library for Paper and Folia compatibility.
- [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) (soft dependency) - Placeholder expansion support.

## Build Instructions
```
./gradlew shadowJar
```
The output jar will be in `build/libs/`.

## Requirements
- Java 21+
- Paper or Folia 1.21+
- (Optional) PlaceholderAPI for placeholder support