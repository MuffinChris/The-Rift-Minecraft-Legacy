package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlazeSpot {

    private double damage = 0;
    private double radius = 0;
    Map<LivingEntity, Long> ticks;
    int lifetime = 0;

    public BlazeSpot(Player p, Location loc, double dmg, double rad, int duration) {
        damage = dmg;
        radius = rad;
        lifetime = duration;
        ticks = new HashMap<>();

        new BukkitRunnable() {
            public void run() {
                lifetime--;
                if (lifetime == 0) {
                    cancel();
                    ticks = new HashMap<>();
                }
                for (LivingEntity e : loc.getNearbyLivingEntities(3)) {

                }
                loc.getWorld().spawnParticle(Particle.FLAME, loc, 100, 0.1, 0.04, 0.04, 0.04);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

    }

}
