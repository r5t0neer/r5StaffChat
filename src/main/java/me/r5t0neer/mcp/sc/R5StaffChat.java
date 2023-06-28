package me.r5t0neer.mcp.sc;

import me.r5t0neer.mcp.sc.cfg.ConfigManager;
import me.r5t0neer.mcp.sc.cmd.CommandManager;
import me.r5t0neer.mcp.sc.lst.ListenerManager;
import me.r5t0neer.mcp.sc.msg.MessageBus;
import me.r5t0neer.mcp.sc.util.ChatFormatter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class R5StaffChat extends JavaPlugin
{
    private ConfigManager configManager;
    private MessageBus messageBus;
    private ListenerManager listenerManager;
    private CommandManager commandManager;

    // independent from global reload

    public Set<Player> playersWithRedirectionEnabled = new HashSet<>();
    public Set<Player> playersOnGlobalChannel = new HashSet<>();

    @Override
    public void onDisable()
    {
        // in case of server /reload
        if(messageBus != null)
        {
            messageBus.onDisable();
        }
    }

    @Override
    public void onEnable()
    {
        try
        {
            configManager = new ConfigManager(this);

            processForPresentPlayers();

            messageBus = new MessageBus(configManager);
            messageBus.registerConsumer((msg) -> {

                String format = configManager.getPrimaryConfig().chatFormat;
                String message = format
                        .replace("%s", msg.serverName())
                        .replace("%d", msg.displayName())
                        .replace("%m", msg.message());

                message = ChatFormatter.colorize(message);

                getServer().getLogger().info("[Server Network Chat]: " + ChatFormatter.trimColors(message));

                for(Player plr : playersOnGlobalChannel)
                {
                    plr.sendMessage(message);
                }

            });

            listenerManager = new ListenerManager(this);
            commandManager = new CommandManager(this);
        }
        catch(Exception e)
        {
            getLogger().log(Level.SEVERE, "Disabling plugin, got exception:", e);
            disablePlugin();
        }
    }

    private void disablePlugin()
    {
        getServer().getPluginManager().disablePlugin(this);
    }

    public ConfigManager getConfigManager()
    {
        return this.configManager;
    }

    public MessageBus getMessageBus()
    {
        return this.messageBus;
    }

    public Set<Player> getPlayersWithRedirectionEnabled()
    {
        return this.playersWithRedirectionEnabled;
    }

    public Set<Player> getPlayersOnGlobalChannel()
    {
        return this.playersOnGlobalChannel;
    }

    public void reload() throws Exception
    {
        this.configManager.reload();
        this.messageBus.reload(configManager.getPrimaryConfig().redisCredentials);
        this.listenerManager.reload();
    }

    private void processForPresentPlayers()
    {
        for(Player plr : getServer().getOnlinePlayers())
        {
            if(plr.hasPermission(configManager.getPrimaryConfig().globalChatPermission))
            {
                this.playersOnGlobalChannel.add(plr);
            }
        }
    }
}
