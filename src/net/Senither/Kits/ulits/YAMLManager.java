package net.Senither.Kits.ulits;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YAMLManager
{

    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;
    private final File folder;

    public YAMLManager(JavaPlugin plugin, String fileName)
    {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        if (!plugin.isInitialized()) {
            throw new IllegalArgumentException("plugin must be initiaized");
        }
        this.plugin = plugin;

        folder = new File(plugin.getDataFolder(), "playerData");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        configFile = new File(folder, fileName);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();

                Bukkit.getLogger().info("[Kits] Creating a new player config file (" + fileName + ")");

                fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

                fileConfiguration.set("kills", 0);
                fileConfiguration.set("deaths", 0);
                fileConfiguration.set("credits", 10);
                fileConfiguration.set("dualscore", 0);

                fileConfiguration.save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        } else {
            fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        }

    }

    public FileConfiguration getConfig()
    {
        return fileConfiguration;
    }

    public void saveConfig()
    {
        if (fileConfiguration == null || configFile == null) {
            return;
        } else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }
}