package me.r5t0neer.mcp.sc.cmd;

import me.r5t0neer.mcp.sc.R5StaffChat;
import me.r5t0neer.mcp.sc.cfg.ConfigManager;
import me.r5t0neer.mcp.sc.util.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class R5chatCommand implements CommandExecutor
{
    private final R5StaffChat plugin;
    private final ConfigManager configs;

    R5chatCommand(R5StaffChat plugin)
    {
        this.plugin = plugin;
        this.configs = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String cmdName, @NotNull String[] args)
    {
        if(!sender.hasPermission("r5chat.cmd.plugin"))
        {
            sender.sendMessage(configs.getMessagesConfig().noPermission);
            return true;
        }

        if(args.length > 0)
        {
            switch(args[0].toLowerCase())
            {
                case "reload":
                    try
                    {
                        plugin.reload();

                        sender.sendMessage(configs.getMessagesConfig().reloadSuccessful);
                        plugin.getLogger().info(configs.getMessagesConfig().reloadSuccessful);
                    }
                    catch(Exception e)
                    {
                        sender.sendMessage(configs.getMessagesConfig().reloadError);

                        e.printStackTrace();
                    }
                    break;

                default:
                    printSubcommands(sender);
                    break;
            }
        }
        else printSubcommands(sender);

        return true;
    }

    private void printSubcommands(CommandSender sender)
    {
        sender.sendMessage(configs.getMessagesConfig().availSubcommandsHeader);
        sender.sendMessage(ChatFormatter.colorize("&a/r5chat reload"));
    }
}
