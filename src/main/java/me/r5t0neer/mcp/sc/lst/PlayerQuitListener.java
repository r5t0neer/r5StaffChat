package me.r5t0neer.mcp.sc.lst;

import me.r5t0neer.mcp.sc.R5StaffChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{
    private final R5StaffChat plugin;

    PlayerQuitListener(R5StaffChat plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        plugin.getPlayersWithRedirectionEnabled().remove(evt.getPlayer());
        plugin.getPlayersOnGlobalChannel().remove(evt.getPlayer());
    }
}
