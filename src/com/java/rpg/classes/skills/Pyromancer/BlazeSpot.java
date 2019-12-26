package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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

    private double damage;
    private double radius;
    //Map<LivingEntity, Integer> ticks;
    int lifetime;
    private Location l;

    private Main main = Main.getInstance();

    public Location getLoc() {
        return l;
    }

    public int getLifetime() {
        return lifetime;
    }

    public BlazeSpot(Player caster, Location loc, double dmg, double rad, int duration) {
        damage = dmg;
        radius = rad;
        lifetime = duration * 20;
        //ticks = Blaze.getTicks().get(caster);
        l = loc;
        new BukkitRunnable() {
            public void run() {
                lifetime--;
                if (lifetime <= 0) {
                    //ticks = new HashMap<>();
                    cancel();
                    return;
                }
                //l.getWorld().playEffect(l, Effect.MOBSPAWNER_FLAMES, 1);
                l.getWorld().spawnParticle(Particle.FLAME, l, 2, 0, 0.05, 0.01, 0.05);
                l.clone().add(new Vector(0, 0.5, 0)).getWorld().spawnParticle(Particle.FLAME, l, 2, 0, 0.05, 0.01, 0.05);
                /*makePowderCircle(caster, duration, 0, radius, 16);
                makePowderCircle(caster, duration, 1, 0.2, 4);
                makePowderCircle(caster, duration, -1, 0.2, 4);
                makePowderCircle(caster, duration, 0.5, 0.5, 8);
                makePowderCircle(caster, duration, -0.5, 0.5, 8);*/
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

    }

    // need to remove ittems
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
    }

}
