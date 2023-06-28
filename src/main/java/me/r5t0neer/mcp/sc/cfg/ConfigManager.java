package me.r5t0neer.mcp.sc.cfg;

import me.r5t0neer.mcp.sc.R5StaffChat;

import java.io.File;
import java.nio.file.Path;

public class ConfigManager
{
    private final R5StaffChat plugin;

    private PrimaryConfig primaryConfig;
    private MessagesConfig messagesConfig;

    public ConfigManager(R5StaffChat plugin) throws Exception
    {
        this.plugin = plugin;

        loadConfigs();
    }

    private void loadConfigs() throws Exception
    {
        File dir = plugin.getDataFolder();

        if(!dir.exists() && !dir.mkdir())
        {
            throw new Exception("Could not create plugin directory - got 'false' from mkdir()");
        }

        primaryConfig = new PrimaryConfig(
                Path.of("config.yml"),
                new File(dir, "config.yml").toPath(),
                plugin
        );

        messagesConfig = new MessagesConfig(
                Path.of("messages.yml"),
                new File(dir, "messages.yml").toPath(),
                plugin
        );
    }

    public PrimaryConfig getPrimaryConfig() { return this.primaryConfig; }
    public MessagesConfig getMessagesConfig() { return this.messagesConfig; }

    public void reload() throws Exception
    {
        loadConfigs();
    }
}
