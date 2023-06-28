package me.r5t0neer.mcp.sc.cfg;

import me.r5t0neer.mcp.sc.cfg.exc.IncompleteConfigException;
import me.r5t0neer.mcp.sc.net.RedisCredentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PrimaryConfig extends AConfig
{
    public final RedisCredentials redisCredentials;
    public final String redisChannel;
    public final boolean overrideServerName;
    public final String serverName;
    public final String chatFormat;
    public final String chatCatchPrefix;
    public final List<String> chatCatchSources;
    public final List<String> chatCatchPassthrough;
    public final String globalChatPermission;
    public final List<Rank> ranks;
    public final boolean overrideIfOperator;
    public final String operatorDisplayName;

    public PrimaryConfig(Path jarPath, Path fsPath, Plugin plugin) throws IOException,
                                                                          InvalidConfigurationException,
                                                                          IncompleteConfigException
    {
        super(jarPath, fsPath, plugin);

        this.ranks = new ArrayList<>();

        ConfigurationSection redis = yaml.getConfigurationSection("redis");

        if(redis != null)
        {
            redisCredentials = new RedisCredentials(
                    redis.getString("host", "127.0.0.1"),
                    redis.getInt("port", 6379),
                    redis.getString("user", ""),
                    redis.getString("password", "")
            );

            redisChannel = redis.getString("channel", "r5_staff_chat");
        }
        else throw new IncompleteConfigException("Missing section 'redis', look for config.yml inside plugin's jar");

        ConfigurationSection srv = yaml.getConfigurationSection("server");

        if(srv != null)
        {
            overrideServerName = srv.getBoolean("override-name");
            serverName = srv.getString("name", "Unknown Server");
        }
        else throw new IncompleteConfigException("Missing section 'server', look for config.yml inside plugin's jar");

        ConfigurationSection chat = yaml.getConfigurationSection("chat");

        if(chat != null)
        {
            chatFormat = chat.getString("format", "&8[&<#ffd700>G&8][&7%s&8] %d&8: &f%m");

            ConfigurationSection catch_ = chat.getConfigurationSection("catch");

            if(catch_ != null)
            {
                chatCatchPrefix = catch_.getString("prefix", "!");
                chatCatchSources = catch_.getStringList("sources");
                chatCatchPassthrough = catch_.getStringList("passthrough");
            }
            else throw new IncompleteConfigException("Missing section 'chat.catch', look for config.yml inside plugin's jar");
        }
        else throw new IncompleteConfigException("Missing section 'chat', look for config.yml inside plugin's jar");

        globalChatPermission = yaml.getString("global-chat-permission", "r5chat.global");

        ConfigurationSection ranks = yaml.getConfigurationSection("ranks");

        if(ranks != null)
        {
            for(String key : ranks.getKeys(false))
            {
                ConfigurationSection rank = ranks.getConfigurationSection(key);

                this.ranks.add(new Rank(
                        rank.getString("display_name"),
                        rank.getString("permission")
                ));
            }
        }
        else throw new IncompleteConfigException("Missing section 'ranks', look for config.yml inside plugin's jar");

        ConfigurationSection op = yaml.getConfigurationSection("operator");

        if(op != null)
        {
            overrideIfOperator = op.getBoolean("override");
            operatorDisplayName = op.getString("display_name");
        }
        else throw new IncompleteConfigException("Missing section 'operator', look for config.yml inside plugin's jar");
    }
}
