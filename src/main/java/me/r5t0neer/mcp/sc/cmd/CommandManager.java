package me.r5t0neer.mcp.sc.cmd;

import me.r5t0neer.mcp.sc.R5StaffChat;

public class CommandManager
{
    public CommandManager(R5StaffChat plugin)
    {
        plugin.getCommand("ch").setExecutor(new ChannelCommand(plugin));
        plugin.getCommand("r5chat").setExecutor(new R5chatCommand(plugin));
    }
}
