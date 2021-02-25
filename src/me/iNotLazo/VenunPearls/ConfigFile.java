package me.iNotLazo.VenunPearls;

import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.*;
import java.util.*;

public class ConfigFile
{
    private File file;
    private YamlConfiguration configuration;
    
    public ConfigFile() {
        this.file = new File(AntiPhase.getInstance().getDataFolder(), "config.yml");
        if (!this.file.exists()) {
            AntiPhase.getInstance().saveResource("config.yml", false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public void load() {
        this.file = new File(AntiPhase.getInstance().getDataFolder(), "config.yml");
        if (!this.file.exists()) {
            AntiPhase.getInstance().saveResource("config.yml", false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public void save() {
        try {
            this.configuration.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public double getDouble(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }
    
    public int getInt(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }
    
    public boolean getBoolean(final String path) {
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
    }
    
    public String getString(final String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return "String at path: " + path + " not found!";
    }
    
    public long getLong(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getLong(path);
        }
        return 0L;
    }
    
    public List<String> getStringList(final String path) {
        if (this.configuration.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return Arrays.asList("String List at path: " + path + " not found!");
    }
}
