package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlazeSpot {

    private double damage;
    private double radius;
    Map<LivingEntity, Integer> ticks;
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
        ticks = new HashMap<>();
        l = loc;
        new BukkitRunnable() {
            public void run() {
                lifetime--;
                if (lifetime <= 0) {
                    ticks = new HashMap<>();
                    cancel();
                    return;
                }
                for (LivingEntity ent : l.getNearbyLivingEntities(radius)) {
                    if (ent instanceof ArmorStand) {
                        continue;
                    }
                    if (ent instanceof Player) {
                        Player p = (Player) ent;
                        if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                            if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                                continue;
                            }
                        }
                        if (p.equals(caster)) {
                            continue;
                        }
                    }

                    if (ticks.containsKey(ent)) {
                        ticks.replace(ent, ticks.get(ent) - 1);
                        if (ticks.get(ent) <= 0) {
                            ticks.remove(ent);
                        }
                    } else {
                        ticks.put(ent, 10);
                        Skill.spellDamageStatic(caster, ent, damage);
                    }

                }
                l.getWorld().spawnParticle(Particle.FLAME, l, 60, 0.1, 0.04, 0.04, 0.04);
                makePowderCircle(caster, duration, 0, radius, 16);
                makePowderCircle(caster, duration, 1, 0.2, 4);
                makePowderCircle(caster, duration, -1, 0.2, 4);
                makePowderCircle(caster, duration, 0.5, 0.5, 8);
                makePowderCircle(caster, duration, -0.5, 0.5, 8);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

    }

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
