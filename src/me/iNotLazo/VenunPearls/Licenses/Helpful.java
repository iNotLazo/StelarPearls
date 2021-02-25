package me.iNotLazo.VenunPearls.Licenses;

import org.bukkit.*;
import java.util.*;

public class Helpful
{
    public static String translate(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public List<String> translateFromArray(final List<String> text) {
        final List<String> messages = new ArrayList<String>();
        for (final String string : text) {
            messages.add(translate(string));
        }
        return messages;
    }
}
