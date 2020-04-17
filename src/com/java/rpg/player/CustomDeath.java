package com.java.rpg.player;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.rpg.classes.utility.StatusObject;
import com.java.rpg.classes.utility.StatusValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomDeath implements Listener {

    private Main main = Main.getInstance();

    //public List<Player> recents = new ArrayList<>();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void cancelPD (PlayerDeathEvent e) {
        /*if (!recents.contains(e.getEntity())) {
            Bukkit.broadcastMessage("You died lel");
            doDeath(e.getEntity());
            recents.add(e.getEntity());
            new BukkitRunnable() {
                public void run() {
                    recents.remove(e.getEntity());
                }
            }.runTaskLater(Main.getInstance(), 20);
        } else {
            e.setCancelled(true);
        }*/
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

    @EventHandler (priority = EventPriority.MONITOR)
    public void onRightClick (PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BEDROCK) {
                File pFile = new File("plugins/Rift/data/gravestones.yml");
                FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                Location graveLoc = e.getClickedBlock().getLocation();
                if (pData.contains("gravestones." + graveLoc.toString())) {
                    List<ItemStack> items = new ArrayList<>();
                    boolean go = true;
                    int index = 0;
                    long millis = pData.getLong("gravestones." + graveLoc.toString() + ".time");
                    if ((System.currentTimeMillis() - millis) * 0.001 <= 15) {
                        if (!pData.getString("gravestones." + graveLoc.toString() + ".uuid").equals(e.getPlayer().getUniqueId().toString())) {
                            DecimalFormat df = new DecimalFormat("#.##");
                            Main.msg(e.getPlayer(), "&cThis gravestone cannot be opened by anyone but &f" + pData.getString("gravestones." + graveLoc.toString() + ".player") + " &cfor &f" + df.format(15 - ((System.currentTimeMillis() - millis) * 0.001)) + " seconds&c.");
                            return;
                        }
                    }
                    while (go) {
                        if (pData.contains("gravestones." + graveLoc.toString() + "." + index)) {
                            items.add(ItemStack.deserialize(pData.getConfigurationSection("gravestones." + graveLoc.toString() + "." + index).getValues(false)));
                        } else {
                            go = false;
                        }
                        index++;
                    }
                    e.getClickedBlock().setType(Material.AIR);
                    for (ItemStack i : items) {
                        graveLoc.getWorld().dropItem(graveLoc, i);
                    }
                    try {
                        pData.set("gravestones." + graveLoc.toString(), null);
                        pData.save(pFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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

        Location graveLoc = new Location(p.getLocation().getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());

        if (graveLoc.getBlock().getType() == Material.AIR) {
            graveLoc.getBlock().setType(Material.BEDROCK);
        } else {
            for (ItemStack i : deathitems) {
                graveLoc.getWorld().dropItem(graveLoc, i);
            }
            return;
        }

        File pFile = new File("plugins/Rift/data/gravestones.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (pData.contains("gravestones." + graveLoc.toString())) {
                List<ItemStack> oldItems = new ArrayList<>();
                boolean go = true;
                int index = 0;
                while (go) {
                    if (pData.contains("gravestones." + graveLoc.toString() + "." + index)) {
                        oldItems.add(ItemStack.deserialize(pData.getConfigurationSection("gravestones." + graveLoc.toString() + "." + index).getValues(false)));
                    } else {
                        go = false;
                    }
                    index++;
                }
                oldItems.addAll(deathitems);
                index = 0;
                for (ItemStack i : oldItems) {
                    pData.set("gravestones." + graveLoc.toString() + "." + index, i.serialize());
                    index++;
                }
                pData.set("gravestones." + graveLoc.toString() + ".time", System.currentTimeMillis());
                pData.set("gravestones." + graveLoc.toString() + ".player", p.getName());
                pData.set("gravestones." + graveLoc.toString() + ".uuid", p.getUniqueId().toString());
                pData.save(pFile);
            } else {
                int index = 0;
                for (ItemStack i : deathitems) {
                    pData.set("gravestones." + graveLoc.toString() + "." + index, i.serialize());
                    index++;
                }
                pData.set("gravestones." + graveLoc.toString() + ".time", System.currentTimeMillis());
                pData.set("gravestones." + graveLoc.toString() + ".player", p.getName());
                pData.set("gravestones." + graveLoc.toString() + ".uuid", p.getUniqueId().toString());
                pData.save(pFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
