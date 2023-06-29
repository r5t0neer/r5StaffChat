package me.r5t0neer.mcp.sc.lst;

import me.r5t0neer.mcp.sc.R5StaffChat;
import me.r5t0neer.mcp.sc.cfg.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

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
        Player plr = evt.getPlayer();
        List<Rank> ranks = plugin.getConfigManager().getPrimaryConfig().ranks;

        for(Rank rank : ranks)
        {
            if(plr.hasPermission(rank.permission()))
            {
                plugin.getPlayersOnGlobalChannel().add(evt.getPlayer());
                break;
            }
        }
    }
}
