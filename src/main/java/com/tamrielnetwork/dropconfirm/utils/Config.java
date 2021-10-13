package com.tamrielnetwork.dropconfirm.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    // Define currentConfig
    private final File currentConfig;
    // Define modifyConfig
    private FileConfiguration modifyConfig;

    // Initialise currentConfig with config.yml and initialise modifyConfig with loaded config.yml
    public Config(File file) {
        this.currentConfig = file;
        loadConfig();
    }

    // Save config
    public void save() {
        try {
            // Try to save currentConfig
            modifyConfig.save(currentConfig);
        } catch (IOException ioException) {
            // ERROR on IOException
            Bukkit.getLogger().severe("An error occurred while saving the file." + currentConfig.getPath());
        }
    }

    // Initialise modifyConfig with loaded config.yml
    public void loadConfig() {
        modifyConfig = YamlConfiguration.loadConfiguration(currentConfig);
    }

    public void set(String path, Object value) {
        // Set location if value is instance of location *@0*
        if (value instanceof Location location) {
            modifyConfig.set(path + ".x", location.getX());
            modifyConfig.set(path + ".y", location.getY());
            modifyConfig.set(path + ".z", location.getZ());
            modifyConfig.set(path + ".yaw", location.getYaw());
            modifyConfig.set(path + ".pitch", location.getPitch());
            modifyConfig.set(path + ".world", location.getWorld().getName());
        }
        // Otherwise, set path to value
        else {
            modifyConfig.set(path, value);
        }
        // Save config
        save();
    }

    // Get String from config by path
    public String getString(String path) {
        return getString(path, new Object[]{});
    }

    // Get String from config by path and Object lists
    public String getString(String path, Object... lists) {
        // Define name as path
        String name = modifyConfig.getString(path);
        // Replace {index} with lists[index] if name != null and lists != null
        if (name != null && lists != null) {
            for (int index = 0; index < lists.length; index++) {
                name = name.replace("{" + index + "}", lists[index].toString());
            }
        }
        // Replace Color Codes & with § in name if not null and return name
        return name == null ? null : name.replace("&", "§");
    }

    // Get int from config
    public int getInt(String path) {
        return modifyConfig.getInt(path);
    }

    // Get long from config
    public long getLong(String path) {
        return modifyConfig.getLong(path);
    }

    // Get boolean from config
    public boolean getBoolean(String path) {
        return modifyConfig.getBoolean(path);
    }

    // Get double from config
    public double getDouble(String path) {
        return modifyConfig.getDouble(path);
    }

    // Get StringList from config
    public List<String> getStringList(String path) {
        // Define text as ArrayList<>()
        List<String> name = new ArrayList<>();
        // Replace Color Codes & with §
        for (String replaceColor : modifyConfig.getStringList(path)) {
            name.add(replaceColor.replace("&", "§"));
        }
        return name;
    }

    // Get IntegerList from config
    public List<Integer> getIntegerList(String path) {
        return new ArrayList<>(modifyConfig.getIntegerList(path));
    }

    // Get Keys from config
    public List<String> getKeys(String path) {
        List<String> list = new ArrayList<>();
        // Add keys that contain only the keys of any direct children, and not their own children if "".equalsIgnoreCase(path)
        if ("".equalsIgnoreCase(path)) {
            list.addAll(modifyConfig.getKeys(false));
        }
        // Otherwise, define configurationSection as ConfigurationSection from path
        else {
            ConfigurationSection configurationSection = modifyConfig.getConfigurationSection(path);
            // If configurationSection is null return list
            if (configurationSection == null) {
                return list;
            }
            // Add Keys from ConfigurationSection from path
            list.addAll(configurationSection.getKeys(false));
        }
        return list;
    }

    // Get ItemStack from config
    public ItemStack getItem(String path) {
        return modifyConfig.getItemStack(path);
    }

    // Get location from config
    public Location getLocation(String path) {
        // Get values defined at *@0*
        double x = modifyConfig.getDouble(path + ".x");
        double y = modifyConfig.getDouble(path + ".y");
        double z = modifyConfig.getDouble(path + ".z");
        float yaw = (float) modifyConfig.getDouble(path + ".yaw");
        float pitch = (float) modifyConfig.getDouble(path + ".pitch");
        String world = modifyConfig.getString(path + ".world");
        // If world isn't defined return null
        if (world == null || "".equalsIgnoreCase(world)) {
            return null;
        }
        // Return new location with values from *@0*
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    // Check if String exists in config
    public boolean exist(String path) {
        return modifyConfig.contains(path);
    }

    // Reload config
    public void reload() {
        loadConfig();
    }

    // Get currentConfig
    public File getFile() {
        return currentConfig;
    }
}
