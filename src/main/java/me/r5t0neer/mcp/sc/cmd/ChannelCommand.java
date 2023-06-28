package me.r5t0neer.mcp.sc.cmd;

import me.r5t0neer.mcp.sc.R5StaffChat;
import me.r5t0neer.mcp.sc.cfg.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChannelCommand implements CommandExecutor
{
    private R5StaffChat plugin;
    private ConfigManager configs;

    ChannelCommand(R5StaffChat plugin)
    {
        this.plugin = plugin;
        configs = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String cmdName, @NotNull String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(configs.getMessagesConfig().notAPlayer);
            return true;
        }

        if(!sender.hasPermission("r5chat.cmd.channel"))
        {
            sender.sendMessage(configs.getMessagesConfig().noPermission);
            return true;
        }

        if(args.length > 0)
        {
            switch(args[0].toLowerCase())
            {
                case "global":
                    plugin.getPlayersWithRedirectionEnabled().add((Player) sender);
                    sender.sendMessage(configs.getMessagesConfig().switchedToGlobal);
                    break;

                case "local":
                    plugin.getPlayersWithRedirectionEnabled().remove((Player) sender);
                    sender.sendMessage(configs.getMessagesConfig().switchedToLocal);
                    break;

                default:
                    sender.sendMessage(configs.getMessagesConfig().availChannels);
                    break;
            }
        }
        else sender.sendMessage(configs.getMessagesConfig().availChannels);

        return true;
    }
}
