package me.r5t0neer.mcp.sc.lst;

import me.r5t0neer.mcp.sc.R5StaffChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener
{
    private final R5StaffChat plugin;

    PlayerJoinListener(R5StaffChat plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent evt)
    {
        plugin.getPlayersOnGlobalChannel().add(evt.getPlayer());
    }
}
