package com.java.rpg.player;

import com.destroystokyo.paper.Title;
import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import com.java.rpg.classes.StatusObject;
import com.java.rpg.classes.StatusValue;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
            doDeath(e.getEntity());
        }
    }

    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 20 * 30, 0));
            }
        }.runTaskLater(Main.getInstance(), 1L);
        //p.sendTitle(new Title(Main.color("&c&lRESPAWNED"), Main.color(""), 5, 80, 5));
        main.setMana(p, 0);
    }

    public void doDeath(Player p) {
        //if (!recents.contains(p)) {
            RPGPlayer rp = main.getRP(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0));
        /*p.setFireTicks(0);
        for (PotionEffect po : p.getActivePotionEffects()) {
            p.removePotionEffect(po.getType());
        }*/
        /*p.setFoodLevel(20);
        p.setSaturation(10);
        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());*/
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
        /*new BukkitRunnable() {
            public void run() {
                p.setVelocity(new Vector(0, 0, 0));
                if (p.getBedSpawnLocation() != null) {
                    p.teleport(p.getBedSpawnLocation());
                } else {
                    p.teleport(main.getSpawn());
                }
            }
        }.runTaskLater(Main.getInstance(), 1L);*/
        //}
    }


}
