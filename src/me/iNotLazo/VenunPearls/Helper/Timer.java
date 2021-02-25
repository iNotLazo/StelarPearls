package me.iNotLazo.VenunPearls.Helper;

import java.util.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.VenunPearls.*;
import org.bukkit.plugin.*;
import org.bukkit.event.block.*;
import me.iNotLazo.VenunPearls.Licenses.*;
import org.bukkit.scheduler.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class Timer extends Handler implements Listener
{
    public HashMap<UUID, Long> cooldown;
    
    public Timer(final AntiPhase plugin) {
        super(plugin);
        this.cooldown = new HashMap<UUID, Long>();
    }
    
    public void enable() {
        AntiPhase.getInstance().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)AntiPhase.getInstance());
    }
    
    public void disable() {
        this.cooldown.clear();
    }
    
    public boolean isActive(final Player player) {
        return this.cooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis() < this.cooldown.get(player.getUniqueId());
    }
    
    public void quit(final Player player) {
        if (this.cooldown.containsKey(player.getUniqueId()) && this.isActive(player)) {
            this.cooldown.remove(player.getUniqueId());
        }
    }
    
    public long getMillisecondsLeft(final Player player) {
        if (this.cooldown.containsKey(player.getUniqueId())) {
            return Math.max(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
        }
        return 0L;
    }
    
    public HashMap<UUID, Long> getActiveCooldowns() {
        return this.cooldown;
    }
    
	@SuppressWarnings("deprecation")
	@EventHandler
    public void SensiblePearls(PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        final Action action = event.getAction();
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (event.hasItem() && (action.equals((Object)Action.RIGHT_CLICK_AIR) || action.equals((Object)Action.RIGHT_CLICK_BLOCK)) && event.getItem().getType().equals((Object)Material.ENDER_PEARL)) {
        	Faction factionat = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
    		if (!(factionat instanceof CitadelFaction)) {
	        	if (this.isActive(player)) {
	                event.setUseItemInHand(Event.Result.DENY);
	                if (AntiPhase.getInstance().getConfig().getBoolean("timer.send-message")) {
	                    player.sendMessage(Helpful.translate(AntiPhase.getInstance().getConfig().getString("timer.message").replace("%time%", Utils.getRemaining(this.getMillisecondsLeft(player), true))));
	                }
	            }
	            else if (event.getItem().getType() == Material.ENDER_PEARL && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equals(Helpful.translate(AntiPhase.getInstance().getConfig().getString("fast-pearl-name")))) {
	                new BukkitRunnable() {
	                    public void run() {
	                        AntiPhase.getInstance().getTimer().cooldown.put(player.getUniqueId(), System.currentTimeMillis() + AntiPhase.getInstance().getConfig().getInt("fast-pearl-cooldown") * 1000);
	                    }
	                }.runTaskLater((Plugin)AntiPhase.getInstance(), 1L);
	            }
	            else if (event.getItem().getType() == Material.ENDER_PEARL) {
	                new BukkitRunnable() {
	                    public void run() {
	                        AntiPhase.getInstance().getTimer().cooldown.put(player.getUniqueId(), System.currentTimeMillis() + AntiPhase.getInstance().getConfig().getInt("timer.time") * 1000);
	                    }
	                }.runTaskLater((Plugin)AntiPhase.getInstance(), 1L);
	            }
	        }
	
	    	if(player.getGameMode().equals(GameMode.CREATIVE)) return;
	
	    	if(AntiPhase.getInstance().getConfig().getBoolean("is-1dot7") 
	    			&& !player.getGameMode().equals(GameMode.CREATIVE)
	        		&& !AntiPhase.getInstance().getTimer().isActive(player)
	        		&& action == Action.RIGHT_CLICK_BLOCK
	        		&& block.getType().isSolid()
	        		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
	        	//ItemStack old = new ItemStack(event.getPlayer().getItemInHand().getTypeId(), event.getPlayer().getItemInHand().getAmount() - 1);
		        //event.getPlayer().setItemInHand(old);
	    		if (player.getItemInHand().getAmount() > 1) {
	    			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
				} 
				else {
					player.setItemInHand(new ItemStack(Material.AIR, 1));
				}
	    		player.launchProjectile(EnderPearl.class);
	        	for (Player online : Bukkit.getOnlinePlayers()) {
	        		online.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
	        	}
	            event.setCancelled(true);
	    	}
	            if(!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.DISPENSER 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.STONE_BUTTON 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.WOOD_BUTTON 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.LEVER 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.FIRE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.TRAP_DOOR 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.WOOD_DOOR 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.WOODEN_DOOR 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.BREWING_STAND 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.HOPPER 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.DROPPER 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.IRON_DOOR_BLOCK 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.FURNACE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.BURNING_FURNACE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.ANVIL 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.BEACON 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.ENCHANTMENT_TABLE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.BED 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.BED_BLOCK 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.CHEST 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.ENDER_CHEST 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		|| 
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.TRAPPED_CHEST 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.WORKBENCH 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.ACACIA_FENCE_GATE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.BIRCH_FENCE_GATE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.DARK_OAK_FENCE_GATE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.JUNGLE_FENCE_GATE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.SPRUCE_FENCE_GATE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
	            		||
	            		!player.getGameMode().equals(GameMode.CREATIVE)
	            		&& !AntiPhase.getInstance().getTimer().isActive(player)
	            		&& action == Action.RIGHT_CLICK_BLOCK 
	            		&& block.getType() == Material.FENCE_GATE 
	            		&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
	            	//ItemStack old = new ItemStack(event.getPlayer().getItemInHand().getTypeId(), event.getPlayer().getItemInHand().getAmount() - 1);
			        //event.getPlayer().setItemInHand(old);
	            	if (player.getItemInHand().getAmount() > 1) {
	        			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	    			} 
	    			else {
	    				player.getItemInHand().setType(Material.AIR);
	    			}
	        		player.launchProjectile(EnderPearl.class);
	            	for (Player online : Bukkit.getOnlinePlayers()) {
	            		online.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
	            	}
	                event.setCancelled(true);
	            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (this.cooldown.containsKey(player.getUniqueId())) {
            this.cooldown.remove(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.cooldown.containsKey(player.getUniqueId()) && !this.isActive(player)) {
            this.cooldown.remove(player.getUniqueId());
        }
    }
}
