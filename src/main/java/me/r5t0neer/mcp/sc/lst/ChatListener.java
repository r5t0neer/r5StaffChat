package me.r5t0neer.mcp.sc.lst;

import me.r5t0neer.mcp.sc.R5StaffChat;
import me.r5t0neer.mcp.sc.cfg.ConfigManager;
import me.r5t0neer.mcp.sc.cfg.Rank;
import me.r5t0neer.mcp.sc.msg.Message;
import me.r5t0neer.mcp.sc.msg.MessageProducer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Set;

public class ChatListener implements Listener
{
    private Server server;
    private ConfigManager configs;
    private Set<Player> redirect;

    private boolean catchPlain, catchPrefixed, catchSwitch;
    private boolean passthroughPlain, passthroughSwitch;

    private MessageProducer producer;

    public ChatListener(R5StaffChat plugin)
    {
        this.server = plugin.getServer();
        this.configs = plugin.getConfigManager();
        this.redirect = plugin.getPlayersWithRedirectionEnabled();

        setStates();

        this.producer = new MessageProducer(plugin.getMessageBus());
    }

    // LOWEST = first executed but disallows for muting/etc. via event cancellation
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent evt)
    {
        if(!evt.getPlayer().hasPermission(configs.getPrimaryConfig().globalChatPermission))
            return;

        if(catchPrefixed)
        {
            String message = getPlainMessage(evt);
            if(message.startsWith(configs.getPrimaryConfig().chatCatchPrefix))
            {

                message = message.replaceFirst(configs.getPrimaryConfig().chatCatchPrefix, "");

                produceMessage(evt, message);
                evt.setCancelled(true);

                return;
            }
        }

        if(catchPlain)
        {
            produceMessage(evt, getPlainMessage(evt));

            if(!passthroughPlain)
                evt.setCancelled(true);
        }
        else if(catchSwitch)
        {
            if(redirect.contains(evt.getPlayer()))
            {
                produceMessage(evt, getPlainMessage(evt));

                if(!passthroughSwitch)
                    evt.setCancelled(true);
            }
        }
    }

    private String getPlainMessage(AsyncChatEvent evt)
    {
        return PlainTextComponentSerializer.plainText().serialize(evt.message());
    }

    private String getDisplayName(Player plr)
    {
        String displayNameFormat = "??? %p";

        if(plr.isOp() && configs.getPrimaryConfig().overrideIfOperator)
        {
            displayNameFormat = configs.getPrimaryConfig().operatorDisplayName;
        }
        else
        {
            for(Rank rank : configs.getPrimaryConfig().ranks)
            {
                if(plr.hasPermission(rank.permission()))
                {
                    displayNameFormat = rank.format();
                    break;
                }
            }
        }

        return displayNameFormat.replace("%p", plr.getName());
    }

    private void produceMessage(AsyncChatEvent evt, String message)
    {
        String serverName;

        if(configs.getPrimaryConfig().overrideServerName)
        {
            serverName = configs.getPrimaryConfig().serverName;
        }
        else serverName = server.getServerName();

        producer.produce(new Message(
                serverName,
                getDisplayName(evt.getPlayer()),
                message
        ));
    }

    private void setStates()
    {
        List<String> sources = configs.getPrimaryConfig().chatCatchSources;

        this.catchPlain = sources.contains("plain");
        this.catchPrefixed = sources.contains("prefix");
        this.catchSwitch = sources.contains("switch");

        List<String> passthrough = configs.getPrimaryConfig().chatCatchPassthrough;

        this.passthroughPlain = passthrough.contains("plain");
        this.passthroughSwitch = passthrough.contains("switch");
    }

    public void reload()
    {
        setStates();
    }
}
