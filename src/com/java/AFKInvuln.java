package com.java;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AFKInvuln implements Listener {


    Map<UUID, Location> hasMoved = new HashMap<>();

    @EventHandler
    public void onClick (InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getView().getTitle().contains("JOIN MENU") && e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains("ENTER")) {
                if (hasMoved.containsKey(p.getUniqueId())) {
                    p.teleport(hasMoved.get(p.getUniqueId()));
                    p.setGameMode(GameMode.SURVIVAL);
                    hasMoved.remove(p.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                new BukkitRunnable() {
                    public void run() {
                        if (e.getPlayer().getOpenInventory().getTitle() != null && e.getPlayer().getOpenInventory().getTitle().contains("JOIN MENU")) {

                        } else {
                            ((Player) e.getPlayer()).teleport(hasMoved.get(e.getPlayer().getUniqueId()));
                            sendInv((Player) e.getPlayer());
                            e.getPlayer().setGameMode(GameMode.SPECTATOR);
                        }
                    }
                }.runTaskLater(Main.getInstance(), 1L);
            }
        }
    }

    public void sendInv(Player p) {
        Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&e&lJOIN MENU"));

        ItemStack i = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(Main.color("&aENTER THE RIFT"));
        List<String> lore = new ArrayList<>();
        lore.add(Main.color("&fClick to enter the rift."));
        m.setLore(lore);
        i.setItemMeta(m);
        playerInv.setItem(13, i);
        p.openInventory(playerInv);
    }

    @EventHandler
    public void onSpec (PlayerStartSpectatingEntityEvent e) {
        if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
            Main.msg(e.getPlayer(), "&cYou cannot spectate while in the entry menu. If the GUI is not showing up, please reconnect!");
        }
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {


        if (hasMoved.containsKey(e.getPlayer())) {
            hasMoved.remove(e.getPlayer());
        }
        hasMoved.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        new BukkitRunnable() {
            public void run() {
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }.runTaskLater(Main.getInstance(), 1L);
        sendInv(e.getPlayer());

        /*
        e.getPlayer().setCollidable(false);
        new BukkitRunnable() {
            public void run() {
                if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                    if (haveTheyMoved(e.getPlayer())) {
                        hasMoved.remove(e.getPlayer().getUniqueId());
                        e.getPlayer().setCollidable(true);
                        cancel();
                    } else {
                        e.getPlayer().getWorld().spawnParticle(Particle.CRIT_MAGIC, e.getPlayer().getEyeLocation().subtract(new Vector(0, 0.25, 0)), 40, 0.1, 0.1, 0.1, 0.1);
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 5);*/
    }
    /*
    @EventHandler (priority = EventPriority.LOWEST)
    public void invuln (EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (hasMoved.containsKey(p.getUniqueId())) {
                if (haveTheyMoved(p)) {
                    hasMoved.remove(p.getUniqueId());
                    p.setCollidable(true);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
    */
    @EventHandler
    public void rp (PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            Main.msg(e.getPlayer(), "&c&lPlease use the resource pack! Edit this server's settings to enable resource packs.");
        }
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            Main.msg(e.getPlayer(), "&c&lFailed to download resource pack.");
        }
        /*if (e.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            Main.msg(e.getPlayer(), "&a&lResource Pack Enabled!");
        }*/
    }

    /*
    public boolean haveTheyMoved(Player p) {
        if (hasMoved.containsKey(p.getUniqueId())) {
            Location loc = hasMoved.get(p.getUniqueId());
            Location lo = p.getLocation();
            return !(loc.getX() == lo.getX() && loc.getZ() == lo.getZ() && loc.getY() == lo.getY() && loc.getPitch() == lo.getPitch() && loc.getYaw() == lo.getYaw());
        }
        return true;
    }*/

}
