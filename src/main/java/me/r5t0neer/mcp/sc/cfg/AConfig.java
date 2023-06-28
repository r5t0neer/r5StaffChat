package me.r5t0neer.mcp.sc.cfg;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class AConfig
{
    protected YamlConfiguration yaml;

    public AConfig(Path jarPath, Path fsPath, Plugin plugin) throws IOException, InvalidConfigurationException
    {
        File confFile = fsPath.toFile();

        if(!confFile.exists())
        {
            plugin.saveResource(jarPath.toString(), false);
        }

        yaml = new YamlConfiguration();
        yaml.load(confFile);
    }
}
