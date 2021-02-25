package me.iNotLazo.VenunPearls;

import org.bukkit.plugin.*;
import org.bukkit.*;
import java.net.*;
import java.util.*;
import java.io.*;
import me.iNotLazo.VenunPearls.Licenses.*;

public class License
{
    private String licenseKey;
    private Plugin plugin;
    private String validationServer;
    private LogType logType;
    private String securityKey;
    private boolean debug;
    
    public License(final String licenseKey, final String validationServer, final Plugin plugin) {
        this.logType = LogType.NORMAL;
        this.securityKey = "GwAhEgdDEok3eoFkEklDuW8iUhTdIUynjKTF";
        this.debug = false;
        this.licenseKey = licenseKey;
        this.plugin = plugin;
        this.validationServer = validationServer;
    }
    
    public License setSecurityKey(final String securityKey) {
        this.securityKey = securityKey;
        return this;
    }
    
    public License setConsoleLog(final LogType logType) {
        this.logType = logType;
        return this;
    }
    
    public License debug() {
        this.debug = true;
        return this;
    }
    
    public boolean register() {
        this.log(0, "&7&m-------------------------------------------");
        this.log(0, "&a&lConnecting to Stelar Licenses...");
        final ValidationType vt = this.isValid();
        if (vt == ValidationType.VALID) {
            this.log(1, "&4qPearls &7\u27a5 &aThanks you, for purchase the complement.");
            this.log(1, "&4qPearls &7\u27a5 &aIf you found a bug, report to iNotLazo#9831");
            this.log(0, "&7&m-------------------------------------------");
            return true;
        }
        this.log(1, "&4qPearls &7\u27a5 &cIncorrect LICENSE! Disabling plugin.");
        this.log(1, "&4qPearls &7\u27a5 &cIf you need an LICENSE, get one by contacting iNotLazo#9831");
        this.log(0, "&7&m-------------------------------------------");
        Bukkit.getScheduler().cancelTasks(this.plugin);
        Bukkit.getPluginManager().disablePlugin(this.plugin);
        return false;
    }
    
    public boolean isValidSimple() {
        return this.isValid() == ValidationType.VALID;
    }
    
    public ValidationType isValid() {
        final String rand = this.toBinary(UUID.randomUUID().toString());
        final String sKey = this.toBinary(this.securityKey);
        final String key = this.toBinary(this.licenseKey);
        try {
            final URL url = new URL(String.valueOf(this.validationServer) + "?v1=" + xor(rand, sKey) + "&v2=" + xor(rand, key) + "&pl=" + this.plugin.getName());
            if (this.debug) {
                System.out.println("RequestURL -> " + url.toString());
            }
            final Scanner s = new Scanner(url.openStream());
            if (s.hasNext()) {
                final String response = s.next();
                s.close();
                try {
                    return ValidationType.valueOf(response);
                }
                catch (IllegalArgumentException exc2) {
                    final String respRand = xor(xor(response, key), sKey);
                    if (rand.substring(0, respRand.length()).equals(respRand)) {
                        return ValidationType.VALID;
                    }
                    return ValidationType.WRONG_RESPONSE;
                }
            }
            s.close();
            return ValidationType.PAGE_ERROR;
        }
        catch (IOException exc) {
            if (this.debug) {
                exc.printStackTrace();
            }
            return ValidationType.URL_ERROR;
        }
    }
    
    private static String xor(final String s1, final String s2) {
        String s3 = "";
        for (int i = 0; i < ((s1.length() < s2.length()) ? s1.length() : s2.length()); ++i) {
            s3 = String.valueOf(s3) + (Byte.valueOf(new StringBuilder().append(s1.charAt(i)).toString()) ^ Byte.valueOf(new StringBuilder().append(s2.charAt(i)).toString()));
        }
        return s3;
    }
    
    private String toBinary(final String s) {
        final byte[] bytes = s.getBytes();
        final StringBuilder binary = new StringBuilder();
        byte[] array;
        for (int length = (array = bytes).length, j = 0; j < length; ++j) {
            int val;
            final byte b = (byte)(val = array[j]);
            for (int i = 0; i < 8; ++i) {
                binary.append(((val & 0x80) != 0x0) ? 1 : 0);
                val <<= 1;
            }
        }
        return binary.toString();
    }
    
    private void log(final int type, final String message) {
        if (this.logType == LogType.NONE || (this.logType == LogType.LOW && type == 0)) {
            return;
        }
        Bukkit.getConsoleSender().sendMessage(Helpful.translate(message));
    }
    
    public enum LogType
    {
        NORMAL("NORMAL", 0), 
        LOW("LOW", 1), 
        NONE("NONE", 2);
        
        private LogType(final String s, final int n) {
        }
    }
    
    public enum ValidationType
    {
        WRONG_RESPONSE("WRONG_RESPONSE", 0), 
        PAGE_ERROR("PAGE_ERROR", 1), 
        URL_ERROR("URL_ERROR", 2), 
        KEY_OUTDATED("KEY_OUTDATED", 3), 
        KEY_NOT_FOUND("KEY_NOT_FOUND", 4), 
        NOT_VALID_IP("NOT_VALID_IP", 5), 
        INVALID_PLUGIN("INVALID_PLUGIN", 6), 
        VALID("VALID", 7);
        
        private ValidationType(final String s, final int n) {
        }
    }
}
