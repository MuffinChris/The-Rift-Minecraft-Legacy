package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlazeSpot {

    private double damage;
    private double radius;
    Map<LivingEntity, Integer> ticks;
    int lifetime;

    private Main main = Main.getInstance();

    public BlazeSpot(Player caster, Location loc, double dmg, double rad, int duration) {
        damage = dmg;
        radius = rad;
        lifetime = duration * 20;
        ticks = new HashMap<>();

        new BukkitRunnable() {
            public void run() {
                lifetime--;
                if (lifetime == 0) {
                    ticks = new HashMap<>();
                    cancel();
                    return;
                }
                for (LivingEntity ent : loc.getNearbyLivingEntities(radius)) {
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
                loc.getWorld().spawnParticle(Particle.FLAME, loc, 60, 0.1, 0.04, 0.04, 0.04);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

    }

}
