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
    private final File fichierConfig;
    private FileConfiguration fconfig;

    public Config(File file) {
        this.fichierConfig = file;
        loadConfig();
    }

    public void save() {
        try {
            fconfig.save(fichierConfig);
        } catch (IOException ex) {
            Bukkit.getLogger().severe("An error occurred while saving the file." + fichierConfig.getPath());
        }
    }

    public void loadConfig() {
        fconfig = YamlConfiguration.loadConfiguration(fichierConfig);
    }

    public void set(String path, Object obj) {
        if (obj instanceof Location loc) {
            fconfig.set(path + ".x", loc.getX());
            fconfig.set(path + ".y", loc.getY());
            fconfig.set(path + ".z", loc.getZ());
            fconfig.set(path + ".yaw", loc.getYaw());
            fconfig.set(path + ".pitch", loc.getPitch());
            fconfig.set(path + ".world", loc.getWorld().getName());
        } else {
            fconfig.set(path, obj);
        }
        save();
    }

    public String getString(String path) {
        return getString(path, new Object[]{});
    }

    public String getString(String path, Object... lists) {
        String name = fconfig.getString(path);
        if (name != null && lists != null) {
            for (int i = 0; i < lists.length; i++) {
                name = name.replace("{" + i + "}", lists[i].toString());
            }
        }
        return name == null ? null : name.replace("&", "§");
    }

    public int getInt(String path) {
        return fconfig.getInt(path);
    }

    public long getLong(String path) {
        return fconfig.getLong(path);
    }

    public boolean getBoolean(String path) {
        return fconfig.getBoolean(path);
    }

    public double getDouble(String path) {
        return fconfig.getDouble(path);
    }

    public List<String> getStringList(String path) {
        List<String> name = new ArrayList<>();
        for (String nom : fconfig.getStringList(path))
            name.add(nom.replace("&", "§"));
        return name;
    }

    public List<Integer> getIntegerList(String path) {
        return new ArrayList<>(fconfig.getIntegerList(path));
    }

    public List<String> getKeys(String path) {
        List<String> list = new ArrayList<>();
        if ("".equalsIgnoreCase(path)) {
            list.addAll(fconfig.getKeys(false));
        } else {
            ConfigurationSection cs = fconfig.getConfigurationSection(path);
            if (cs == null)
                return list;
            list.addAll(cs.getKeys(false));
        }
        return list;
    }

    public ItemStack getItem(String path) {
        return fconfig.getItemStack(path);
    }

    public Location getLocation(String path) {
        double x = fconfig.getDouble(path + ".x");
        double y = fconfig.getDouble(path + ".y");
        double z = fconfig.getDouble(path + ".z");
        float yaw = (float) fconfig.getDouble(path + ".yaw");
        float pitch = (float) fconfig.getDouble(path + ".pitch");
        String world = fconfig.getString(path + ".world");
        if (world == null || "".equalsIgnoreCase(world)) {
            return null;
        }
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }


    public boolean exist(String path) {
        return fconfig.contains(path);
    }

    public void reload() {
        loadConfig();
    }

    public File getFile() {
        return fichierConfig;
    }
}