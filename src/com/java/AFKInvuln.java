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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class AFKInvuln implements Listener {


    Map<UUID, Location> hasMoved;
    Map<UUID, Boolean> packAccepted;

    public AFKInvuln() {
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
        if (hasMoved.containsKey(e.getPlayer())) {
            hasMoved.remove(e.getPlayer());
        }
        if (packAccepted.containsKey(e.getPlayer())) {
            packAccepted.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onClick (InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getView().getTitle().contains("§e§lJOIN MENU")) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains("ENTER")) {
                    e.setCurrentItem(new ItemStack(Material.AIR));
                    if (hasMoved.containsKey(p.getUniqueId())) {
                        Location l = hasMoved.get(p.getUniqueId());
                        hasMoved.remove(p.getUniqueId());
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
            if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                new BukkitRunnable() {
                    public void run() {
                        if (e.getPlayer().getOpenInventory().getTitle() != null && e.getPlayer().getOpenInventory().getTitle().contains("§e§lJOIN MENU")) {

                        } else {
                            Player p = (Player) e.getPlayer();
                            if (e.getPlayer().isDead()) {

                            } else {
                                Location l = hasMoved.get(e.getPlayer().getUniqueId());
                                p.teleport(l);
                            }
                            sendInv(p);
                            p.setGameMode(GameMode.SPECTATOR);
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
            e.setCancelled(true);
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
        }
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        if (hasMoved.containsKey(e.getPlayer())) {
            hasMoved.remove(e.getPlayer());
        }
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
        hasMoved.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        new BukkitRunnable() {
            public void run() {
                if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
                    //unsure why removed teleport, but it could cause errors!
                    //Main.msg(e.getPlayer(), "&a&lResource Pack loading...");
                    if (packAccepted.containsKey(e.getPlayer().getUniqueId())) {
                        if (e.getPlayer().getOpenInventory() != null) {
                            if (e.getPlayer().getOpenInventory().getTitle().contains("§e§lJOIN MENU")) {
                                return;
                            }
                        }
                        sendInv(e.getPlayer());
                    } else {
                        e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
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
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
        }
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            Main.msg(e.getPlayer(), "&c&lFailed to download resource pack. We recommend retrying with a reconnect!");
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
        }
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            Main.msg(e.getPlayer(), "&a&lResource Pack Enabled!");
            e.getPlayer().teleport(hasMoved.get(e.getPlayer().getUniqueId()));
            sendInv(e.getPlayer());
            packAccepted.put(e.getPlayer().getUniqueId(), true);
        }
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
