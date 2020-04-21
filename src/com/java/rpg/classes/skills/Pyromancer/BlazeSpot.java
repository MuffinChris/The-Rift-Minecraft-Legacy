package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BlazeSpot {

    int lifetime;
    private Location l;

    public Location getLoc() {
        return l;
    }

    public int getLifetime() {
        return lifetime;
    }

    public BlazeSpot(Location loc, int duration) {
        lifetime = duration * 20;
        l = loc;
        new BukkitRunnable() {
            public void run() {
                lifetime-=1;
                if (lifetime <= 0) {
                    cancel();
                    return;
                }
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 100, 0), 1);
                l.getWorld().spawnParticle(Particle.REDSTONE, l, 2, 0.35, 0.6, 0.35, 0.03, dust, true);
                l.getWorld().spawnParticle(Particle.FLAME, l, 1, 0.35, 0.25, 0.35, 0.03,  null, true);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

    }

    /*
    public void makePowderCircle(Player caster, int duration, double height, double radius, int cnt) {
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/cnt) {
            Location loc = caster.getLocation();
            Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), height, radius * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), height, radius * Math.sin( alpha + Math.PI ) );

            ItemStack i = new ItemStack(Material.BLAZE_POWDER, 1);

            Item it = l.getWorld().dropItem(firstLocation, i);
            it.setPickupDelay(duration * 100);
            it.setCanMobPickup(false);
            it.setInvulnerable(true);
            it.setFireTicks(duration * 20);
            it.setTicksLived(duration * 20);
            it.setGravity(false);

            it = l.getWorld().dropItem(secondLocation, i);
            it.setPickupDelay(duration * 100);
            it.setCanMobPickup(false);
            it.setInvulnerable(true);
            it.setFireTicks(duration * 20);
            it.setTicksLived(duration * 20);
            it.setGravity(false);
        }
    }*/

}
