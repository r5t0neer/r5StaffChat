package me.r5t0neer.mcp.sc.cfg;

import me.r5t0neer.mcp.sc.util.ChatFormatter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;

public class MessagesConfig extends AConfig
{
    public final String notAPlayer;
    public final String noPermission;
    public final String availChannels;
    public final String availSubcommandsHeader;
    public final String reloadSuccessful;
    public final String reloadError;
    public final String switchedToLocal;
    public final String switchedToGlobal;

    public MessagesConfig(Path jarPath, Path fsPath, Plugin plugin) throws
                                                                    IOException,
                                                                    InvalidConfigurationException
    {
        super(jarPath, fsPath, plugin);

        notAPlayer = ChatFormatter.colorize(yaml.getString("not-a-player", "&4You must be a player to use the command."));
        noPermission = ChatFormatter.colorize(yaml.getString("no-permission", "&cYou do not have permission to use this command."));
        availChannels = ChatFormatter.colorize(yaml.getString("available-channels", "&cYou need to choose between &elocal &cand &eglobal &cchannels."));
        availSubcommandsHeader = ChatFormatter.colorize(yaml.getString("available-subcommands-header", "&8[&er5StaffChat&8] &aAvailable commands:"));
        reloadSuccessful = ChatFormatter.colorize(yaml.getString("reload-successful", "&8[&er5StaffChat&8] &aReloaded successfully."));
        reloadError = ChatFormatter.colorize(yaml.getString("reload-error", "&8[&er5StaffChat&8] &cAn exception occurred while reloading, check console for details."));
        switchedToLocal = ChatFormatter.colorize(yaml.getString("switched-to-local", "&aSwitched to server (local) channel."));
        switchedToGlobal = ChatFormatter.colorize(yaml.getString("switched-to-global", "&aSwitched to server network (global) channel."));
    }
}
