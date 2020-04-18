package com.java.rpg.player;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.rpg.classes.utility.StatusObject;
import com.java.rpg.classes.utility.StatusValue;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.TileEntityChest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomDeath implements Listener {

    private Main main = Main.getInstance();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void customDeath (PlayerDeathEvent e) {
        if (e.getEntity().isOnline() && !e.isCancelled()) {
            e.setShouldDropExperience(false);
            doDeath(e.getEntity(), e.getDrops());
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0));
            }
        }.runTaskLater(Main.getInstance(), 1L);
        //p.sendTitle(new Title(Main.color("&c&lRESPAWNED"), Main.color(""), 5, 80, 5));
        main.setMana(p, 0);
    }

    private int seconds = 60;

    @EventHandler
    public void chestClose (InventoryCloseEvent e) {
        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (e.getView().getTitle().contains("§cGravestone")) {
                BlockInventoryHolder chest = (BlockInventoryHolder) e.getInventory().getHolder();
                if (chest != null) {
                    for (ItemStack i : chest.getInventory().getContents()) {
                        chest.getBlock().getWorld().dropItem(chest.getBlock().getLocation(), i);
                    }
                    chest.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onGravestoneBreak (BlockBreakEvent e) {
        if (isGravestone(e.getBlock())) {
            e.setCancelled(true);
            Main.msg(e.getPlayer(), "&cOpen the Gravestone instead of breaking it.");
        }
    }

    @EventHandler
    public void noGravestoneMerge (BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.CHEST) {
            if (isGravestone(e.getBlock().getLocation().add(new Vector(1, 0,0)).getBlock()) || isGravestone(e.getBlock().getLocation().add(new Vector(-1, 0,0)).getBlock()) || isGravestone(e.getBlock().getLocation().add(new Vector(0, 0,1)).getBlock()) || isGravestone(e.getBlock().getLocation().add(new Vector(0, 0,-1)).getBlock())) {
                e.setCancelled(true);
                Main.msg(e.getPlayer(), "&cRemove nearby Gravestone to place Chest.");
            }
        }
    }

    public boolean isGravestone(Block b) {
        if (b.getType() == Material.CHEST) {
            Chest chest = (Chest) b.getState();
            TileEntityChest tec = ((CraftChest) chest).getTileEntity();
            if (tec.hasCustomName()) {
                return (tec.getCustomName().toString().contains("§cGravestone"));
            }
        }
        return false;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onGravestoneInteract (PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
                Location graveLoc = e.getClickedBlock().getLocation();
                if (graveLoc.getBlock().hasMetadata("Time")) {
                    long millis = graveLoc.getBlock().getMetadata("Time").get(0).asLong();
                    UUID dead = UUID.fromString(graveLoc.getBlock().getMetadata("Owner").get(0).asString());

                    if ((System.currentTimeMillis() - millis) * 0.001 <= seconds) {
                        if (!dead.equals(e.getPlayer().getUniqueId())) {
                            DecimalFormat df = new DecimalFormat("#.##");
                            Main.msg(e.getPlayer(), "&8» &4Gravestone &ccan only be opened by &f" + Bukkit.getOfflinePlayer(dead).getName() + " &cfor &f" + df.format(seconds - ((System.currentTimeMillis() - millis) * 0.001)) + " seconds&c.");
                            e.setCancelled(true);
                            return;
                        }
                    }

                    e.getClickedBlock().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 25);

                    graveLoc.getBlock().removeMetadata("Time", Main.getInstance());
                    graveLoc.getBlock().removeMetadata("Owner", Main.getInstance());
                }
            }
        }
    }

    public void doDeath(Player p, List<ItemStack> deathitems) {
        RPGPlayer rp = main.getRP(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0));
        for (StatusObject s : rp.getSo()) {
            for (StatusValue sv : s.getStatuses()) {
                if (!sv.getDurationless()) {
                    s.getCBT().add(sv);
                }
            }
            for (StatusValue rem : s.getCBT()) {
                rem.scrub();
                s.getStatuses().remove(rem);
            }
        }
        rp.giveExpFromSource(p, p.getLocation(), rp.getMaxExp() * -0.1 * (Math.random() * 0.1 + 1), "");

        if (deathitems.isEmpty())
            return;

        Location graveLoc = new Location(p.getLocation().getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        if ((graveLoc.getBlock().getType() == Material.AIR || graveLoc.getBlock().getType() == Material.CAVE_AIR)) {
            graveLoc.getBlock().setType(Material.CHEST);
            Chest chest = (Chest) graveLoc.getBlock().getState();
            TileEntityChest tec = ((CraftChest) chest).getTileEntity();
            tec.setCustomName(new ChatComponentText(Main.color("&c" + p.getName() + "'s &cGravestone")));
            chest.getBlockInventory().setContents(deathitems.toArray(new ItemStack[0]));
            graveLoc.getBlock().setMetadata("Time", new FixedMetadataValue(Main.getInstance(), System.currentTimeMillis()));
            graveLoc.getBlock().setMetadata("Owner", new FixedMetadataValue(Main.getInstance(), p.getUniqueId().toString()));
        } else {
            for (ItemStack i : deathitems) {
                graveLoc.getWorld().dropItem(graveLoc, i);
            }
            return;
        }
    }


}
