package com.java.holograms;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.java.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

public class EntityHealthBars implements Listener {

    private Main main = Main.getInstance();

    /*
    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        LivingEntity ent = (LivingEntity) e.getPlayer();
        if (!main.getHpBars().containsKey(ent)) {
            DecimalFormat dF = new DecimalFormat("#");
            main.getHpBars().put(ent, new Hologram(ent, ent.getLocation().add(new Vector(0, ent.getHeight() + 0.1, 0)), "&f" + dF.format(ent.getHealth()) + "&c❤", Hologram.HologramType.HOLOGRAM));
        }
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        LivingEntity ent = (LivingEntity) e.getPlayer();
        if (main.getHpBars().containsKey(ent)) {
            main.getHpBars().get(ent).destroy();
            main.getHpBars().remove(ent);
        }
    }*/

    @EventHandler
    public void onSpawn (EntityAddToWorldEvent e) {
        if (e.getEntity().getType() == EntityType.ARMOR_STAND) {
            if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().contains("❤")) {
                for (Hologram h : main.getHpBars().values()) {
                    if (h.getStand() == e.getEntity()) {
                        return;
                    }
                }
                for (Hologram h : main.getHolos()) {
                    if (h.getStand() == e.getEntity()) {
                        return;
                    }
                }
                new BukkitRunnable() {
                    public void run() {
                        e.getEntity().remove();
                    }
                }.runTaskLater(Main.getInstance(), 1);
            }
            return;
        }
        /*if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
            LivingEntity ent = (LivingEntity) e.getEntity();
            if (!main.getHpBars().containsKey(ent)) {
                new BukkitRunnable() {
                    public void run() {
                        DecimalFormat dF = new DecimalFormat("#.##");
                        main.getHpBars().put(ent, new Hologram(ent, ent.getLocation().add(new Vector(0, ent.getHeight() - 0.2, 0)), "&f" + dF.format(ent.getHealth()) + "&c❤", Hologram.HologramType.HOLOGRAM));
                    }
                }.runTaskLater(Main.getInstance(), 1);
            }
        }*/
    }

    /*@EventHandler
    public void onMove (PlayerMoveEvent e) {
        if (main.getHpBars().containsKey((LivingEntity) e.getPlayer())) {
            main.getHpBars().get(e.getPlayer()).center();
        }
    }*/

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage (EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand)) {
            LivingEntity ent = (LivingEntity) e.getEntity();
            if (!main.getHpBars().containsKey(ent) && !(ent instanceof Player)) {
                DecimalFormat dF = new DecimalFormat("#.##");
                main.getHpBars().put(ent, new Hologram(ent, ent.getLocation().add(new Vector(0, ent.getHeight() - 0.2, 0)), "&f" + dF.format(ent.getHealth()) + "&c❤", Hologram.HologramType.HOLOGRAM));
                if (ent.getCustomName() != null) {
                    ent.setCustomNameVisible(true);
                }
            }
            if (main.getHpBars().containsKey(ent)) {
                if (ent.isDead() || (ent instanceof Player && !((Player)ent).isOnline())) {
                    if (main.getHpBars().containsKey(ent)) {
                        main.getHpBars().get(ent).destroy();
                        main.getHpBars().remove(ent);
                    }
                    return;
                }
                DecimalFormat dF = new DecimalFormat("#.##");
                double hp = ent.getHealth();
                hp-=e.getDamage();
                main.getHpBars().get(ent).setText("&f" + dF.format(hp) + "&c❤");
                main.getHpBars().get(ent).resetLifetime();
                new BukkitRunnable() {
                    public void run() {
                        if (ent.isDead() || (ent instanceof Player && !((Player)ent).isOnline())) {
                            if (main.getHpBars().containsKey(ent)) {
                                main.getHpBars().get(ent).destroy();
                                main.getHpBars().remove(ent);
                            }
                            return;
                        }
                        if (main.getHpBars().containsKey(ent)) {
                            main.getHpBars().get(ent).setText("&f" + dF.format(ent.getHealth()) + "&c❤");
                        }
                    }
                }.runTaskLater(Main.getInstance(), 1);
            }
        }
    }

}
