package me.iNotLazo.VenunPearls.Helper;

import org.bukkit.command.*;
import org.bukkit.*;
import me.iNotLazo.VenunPearls.*;
import me.iNotLazo.VenunPearls.Licenses.*;
import org.bukkit.entity.*;
import java.util.*;

public class CMD implements CommandExecutor, TabCompleter
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            this.sendUsage(sender);
        }
        else if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);
            if (args[0].equals("resetcooldown") && player != null) {
                if (sender.hasPermission("spearls.admin")) {
                    AntiPhase.getInstance().getTimer().cooldown.remove(player.getUniqueId());
                    sender.sendMessage("You have removed the cooldown of " + player.getName());
                    if (AntiPhase.getInstance().getConfig().getBoolean("Invalid-Pearl-Command")) {
                        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), AntiPhase.getInstance().getConfig().getString("Command").replace("%player%", player.getName()));
                    }
                }
                else {
                    sender.sendMessage(Helpful.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("resetcooldown") && player == null) {
                sender.sendMessage(Helpful.translate("&cInvalid Player"));
            }
        }
        else if (args.length == 1) {
            if (args[0].equals("slabs")) {
                if (sender.hasPermission("spearls.admin")) {
                    if (AntiPhase.getInstance().getConfig().getBoolean("TalibanPearl.Slabs")) {
                        sender.sendMessage(Helpful.translate("&eTalibanPearl on &lSlabs &eHas been toggled &cOff"));
                        AntiPhase.getInstance().getConfig().set("TalibanPearl.Slabs", (Object)false);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                    else if (!AntiPhase.getInstance().getConfig().getBoolean("TalibanPearl.Slabs")) {
                        sender.sendMessage(Helpful.translate("&eTalibanPearl on &lSlabs &eHas been toggled &aOn"));
                        AntiPhase.getInstance().getConfig().set("TalibanPearl.Slabs", (Object)true);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(Helpful.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("stairs")) {
                if (sender.hasPermission("spearls.admin")) {
                    if (AntiPhase.getInstance().getConfig().getBoolean("TalibanPearl.Stairs")) {
                        sender.sendMessage(Helpful.translate("&eTalibanPearl on &lStairs &eHas been toggled &cOff"));
                        AntiPhase.getInstance().getConfig().set("TalibanPearl.Stairs", (Object)false);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                    else if (!AntiPhase.getInstance().getConfig().getBoolean("TalibanPearl.Stairs")) {
                        sender.sendMessage(Helpful.translate("&eTalibanPearl on &lStairs &eHas been toggled &aOn"));
                        AntiPhase.getInstance().getConfig().set("TalibanPearl.Stairs", (Object)true);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(Helpful.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("cpearl")) {
                if (sender.hasPermission("spearls.admin")) {
                    if (AntiPhase.getInstance().getConfig().getBoolean("cPearl")) {
                        sender.sendMessage(Helpful.translate("&e&lcPearl &eHas been toggled &cOff"));
                        AntiPhase.getInstance().getConfig().set("cPearl", (Object)false);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                    else if (!AntiPhase.getInstance().getConfig().getBoolean("cPearl")) {
                        sender.sendMessage(Helpful.translate("&e&lcPearl &eHas been toggled &aOn"));
                        AntiPhase.getInstance().getConfig().set("cPearl", (Object)true);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(Helpful.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("reload")) {
                if (sender.hasPermission("spearls.admin")) {
                    AntiPhase.getInstance().reloadConfig();
                    AntiPhase.getInstance().getconfigfile().load();
                    sender.sendMessage(Helpful.translate("&aPlugin reloaded!"));
                }
                else {
                    sender.sendMessage(Helpful.translate("&cNo permission."));
                }
            }
            else if (args[0].equals("tp") || args[0].equals("teleport")) {
                if (sender.hasPermission("spearls.admin")) {
                    if (AntiPhase.getInstance().getConfig().getBoolean("tp-with-taliban-disabled")) {
                        sender.sendMessage(Helpful.translate("&eTalibanPearl Disabled &lTeleport &eHas been toggled &cOff"));
                        AntiPhase.getInstance().getConfig().set("tp-with-taliban-disabled", (Object)false);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                    else if (!AntiPhase.getInstance().getConfig().getBoolean("tp-with-taliban-disabled")) {
                        sender.sendMessage(Helpful.translate("&eTalibanPearl Disabled &lTeleport &eHas been toggled &aOn"));
                        AntiPhase.getInstance().getConfig().set("tp-with-taliban-disabled", (Object)true);
                        AntiPhase.getInstance().getconfigfile().save();
                        AntiPhase.getInstance().getconfigfile().load();
                    }
                }
                else {
                    sender.sendMessage(Helpful.translate("&cNo permission."));
                }
            }
        }
        else {
            this.sendUsage(sender);
        }
        return false;
    }
    
    public void sendUsage(final CommandSender sender) {
        sender.sendMessage(Helpful.translate("&4&lSensiblePearls &7- &4Help Commands"));
        sender.sendMessage(Helpful.translate("&c/spearls <reload,resetcooldown,slabs,stairs,cpearl,tp> <player>"));
    }
    
    @SuppressWarnings("unchecked")
	public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            return Arrays.asList("slabs", "stairs", "tp", "resetcooldown", "reload", "cpearl");
        }
        if (args.length == 2) {
            final Iterator<Player> iterator = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
            if (iterator.hasNext()) {
                final Player player = iterator.next();
                return Arrays.asList(player.getName());
            }
        }
        return Collections.emptyList();
    }
}
