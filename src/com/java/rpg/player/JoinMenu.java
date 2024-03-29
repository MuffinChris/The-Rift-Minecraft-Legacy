package com.java.rpg.player;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import com.java.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class JoinMenu implements Listener {


    Map<UUID, Location> hasMoved;
    Map<UUID, Boolean> packAccepted;

    public JoinMenu() {
        hasMoved = new HashMap<>();
        packAccepted = new HashMap<>();
    }

    @EventHandler
    public void onRes (PlayerRespawnEvent e) {
        if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
            hasMoved.replace(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        }
    }

    @EventHandler
    public void leave (PlayerQuitEvent e) {
        if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
            hasMoved.remove(e.getPlayer().getUniqueId());
        }
        if (packAccepted.containsKey(e.getPlayer().getUniqueId())) {
            packAccepted.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onClick (InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getView().getTitle().contains("§5§lJOIN MENU")) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains("ENTER")) {
                    e.setCurrentItem(new ItemStack(Material.AIR));
                    if (hasMoved.containsKey(p.getUniqueId())) {
                        Location l = hasMoved.get(p.getUniqueId());
                        hasMoved.remove(p.getUniqueId());
                        p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        p.teleport(l);
                        p.setGameMode(GameMode.SURVIVAL);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            if (e.getReason() != InventoryCloseEvent.Reason.PLUGIN) {
                if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                    new BukkitRunnable() {
                        public void run() {
                            Player p = (Player) e.getPlayer();
                            if (e.getPlayer().isDead()) {

                            } else {
                                Location l = hasMoved.get(e.getPlayer().getUniqueId());
                                e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                                p.teleport(l);
                            }
                            sendInv(p);
                            p.setGameMode(GameMode.SPECTATOR);
                        }
                    }.runTaskLater(Main.getInstance(), 1L);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void death(PlayerDeathEvent e) {
        if (hasMoved.containsKey(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    public void sendInv(Player p) {
        Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&5&lJOIN MENU"));

        ItemStack i = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(Main.color("&5ENTER THE RIFT"));
        List<String> lore = new ArrayList<>();
        lore.add(Main.color("&fClick to enter &dThe Rift&f."));
        m.setLore(lore);
        i.setItemMeta(m);
        playerInv.setItem(13, i);
        p.openInventory(playerInv);
    }

    @EventHandler
    public void onSpec (PlayerStartSpectatingEntityEvent e) {
        if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
            Main.msg(e.getPlayer(), "&cYou cannot spectate while in the entry menu. If the GUI is not showing up, please reconnect!");
            e.setCancelled(true);
            e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onTP (PlayerTeleportEvent e) {
        if (!packAccepted.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
        if ((e.getPlayer().getOpenInventory() != null && e.getPlayer().getOpenInventory().getTitle().contains("§5§lJOIN MENU")) && hasMoved.containsKey(e.getPlayer().getUniqueId())) {
            if (e.getTo().getX() != hasMoved.get(e.getPlayer().getUniqueId()).getX() && e.getTo().getY() != hasMoved.get(e.getPlayer().getUniqueId()).getY() && e.getTo().getZ() != hasMoved.get(e.getPlayer().getUniqueId()).getZ()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
            hasMoved.remove(e.getPlayer().getUniqueId());
        }
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
        hasMoved.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        new BukkitRunnable() {
            public void run() {
                if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                    //e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                    //e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    //unsure why removed teleport, but it could cause errors!
                    //Main.msg(e.getPlayer(), "&a&lResource Pack loading...");
                    if (packAccepted.containsKey(e.getPlayer().getUniqueId())) {
                            if (e.getPlayer().getOpenInventory().getTitle().contains("§5§lJOIN MENU")) {
                                return;
                            }
                        sendInv(e.getPlayer());
                    } else {
                        //e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        //e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
                        //e.getPlayer().setResourcePack("https://www.dropbox.com/sh/oimjke2c6b1gqwy/AADouxTRdS0RF49dmqPWtBaWa?dl\\=1", "");
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }
    @EventHandler
    public void rp (PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            Main.msg(e.getPlayer(), "&c&lPlease use the resource pack! Select the Server and click Edit to enable resource packs.");
            e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
        } else
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            Main.msg(e.getPlayer(), "&c&lResource Pack not fully loaded, please wait. If it does not load within a few minutes, reconnect or contact an Admin.");
            e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
        } else
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            Main.msg(e.getPlayer(), "&a&lResource Pack Enabled!");
            e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
        } /*else {
            Main.msg(e.getPlayer(), "&c&lPlease use the resource pack! Select the Server and click Edit to enable resource packs.");
            e.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
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
