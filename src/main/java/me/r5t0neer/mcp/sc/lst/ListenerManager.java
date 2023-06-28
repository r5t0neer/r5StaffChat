package me.r5t0neer.mcp.sc.lst;

import me.r5t0neer.mcp.sc.R5StaffChat;
import org.bukkit.plugin.PluginManager;

public class ListenerManager
{
    private final ChatListener chatListener;
    private final PlayerJoinListener joinListener;
    private final PlayerQuitListener quitListener;

    public ListenerManager(R5StaffChat plugin)
    {
        chatListener = new ChatListener(plugin);
        joinListener = new PlayerJoinListener(plugin);
        quitListener = new PlayerQuitListener(plugin);

        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(chatListener, plugin);
        pm.registerEvents(joinListener, plugin);
        pm.registerEvents(quitListener, plugin);
    }

    public void reload()
    {
        chatListener.reload();
    }
}
