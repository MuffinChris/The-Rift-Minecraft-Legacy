package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class FlameTornado extends Skill {

    private Main main = Main.getInstance();

    private double dps = 50;
    private double dpt = 3;
    private double travelspeed = 0.1;
    private double height = 8;
    private int duration = 20 * 30;
    private int maxradius = 4; // actually diameter lol
    private Map<UUID, Location> tornados;
    private Map<UUID, Map<LivingEntity, Long>> damage;

    public FlameTornado() {
        super("FlameTornado", 150, 60 * 20, 30, 0,  SkillType.CAST, null,null);

        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fCreate a tornado that damages and follows enemies."));
        setDescription(desc);

        tornados = new HashMap<>();
        damage = new HashMap<>();
    }

    public void cast(Player p) {
        super.cast(p);
        if (damage.containsKey(p.getUniqueId())) {
            damage.remove(p.getUniqueId());
        }
        damage.put(p.getUniqueId(), new HashMap<>());

        if (tornados.containsKey(p.getUniqueId())) {
            tornados.remove(p.getUniqueId());
        }
        tornados.put(p.getUniqueId(), p.getLocation());
        /*
        if (!getTasks().isEmpty()) {
            for (int i : getTasks()) {
                Bukkit.getScheduler().cancelTask(i);
            }
            getTasks().clear();
        }*/
        new BukkitRunnable() {
            int times = 0;
            public void run() {
                Entity nadoEnt = null;
                EntityType e = EntityType.ZOMBIE;
                double dist = 999;
                Location locOf = tornados.get(p.getUniqueId());
                for (LivingEntity ent : locOf.getNearbyLivingEntities(32)) {
                    if (ent instanceof ArmorStand) {
                        continue;
                    }
                    if (ent instanceof Player) {
                        Player pl = (Player) ent;
                        if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                            if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                                continue;
                            }
                        }
                        if (pl.equals(p)) {
                            continue;
                        }
                    }
                    if ((dist > ent.getLocation().distance(locOf) && !(!(ent instanceof Player) && e == EntityType.PLAYER)) || (ent instanceof Player && !(e == EntityType.PLAYER))) {
                        dist = ent.getLocation().distance(locOf);
                        e = ent.getType();
                        nadoEnt = ent;
                    }
                }
                if (dist < Double.MAX_VALUE && nadoEnt instanceof Entity) {
                    if (!(dist <= 1)) {
                        locOf = locOf.add(nadoEnt.getLocation().toVector().subtract(new Vector(0, 0.5, 0)).subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    }
                }
                makeTornado(locOf.clone(), p);
                tornados.remove(p.getUniqueId());
                tornados.put(p.getUniqueId(), locOf);
                times++;
                if (times >= duration) {
                    cancel();
                    if (tornados.containsKey(p.getUniqueId())) {
                        tornados.remove(p.getUniqueId());
                    }
                    if (damage.containsKey(p.getUniqueId())) {
                        damage.remove(p.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    public void makeTornado(Location loc, Player caster) {
        double radius = 0.1;
        double height2 = 0.1;
        List<HashMap<Location, Double>> locs = new ArrayList<>();
        boolean maxr = false;
        boolean maxh = false;
        double random = Math.random();
        if (random <= 0.02) {
            loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_AMBIENT, 0.7F, 1.0F);
        }
        while(!(maxh &&maxr)) {
            if (radius < maxradius) {
                radius += 0.6;
            } else {
                maxr = true;
            }
            if (height2 < height) {
                height2 += 1.2;
            } else {
                maxh = true;
            }
            for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/16) {
                Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), height2, radius * Math.sin( alpha ) );
                Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), height2, radius * Math.sin( alpha + Math.PI ) );
                loc.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0, 0.01, 0.01, 0.01 );
                loc.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0, 0.01, 0.01, 0.01 );
            }
            HashMap map = new HashMap<>();
            Location addToLocs = loc.clone();
            addToLocs.add(new Vector(0, height2 * 1.25, 0));
            map.put(addToLocs, radius + 0.5);
            locs.add((HashMap) map.clone());
            for (LivingEntity e : loc.getNearbyLivingEntities(radius + 0.5)) {
                if (e instanceof ArmorStand) {
                    continue;
                }
                if (e instanceof Player) {
                    Player pl = (Player) e;
                    if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                        if (main.getPM().getParty(pl).getPlayers().contains(caster)) {
                            continue;
                        }
                    }
                    if (pl.equals(caster)) {
                        continue;
                    }
                }
                for (HashMap<Location, Double> locd : locs) {
                    if (e.getEyeLocation().distance((Location) locd.keySet().toArray()[0]) <= (Double) locd.values().toArray()[0]) {
                        if (damage.get(caster.getUniqueId()).containsKey(e)) {
                            if (Math.abs(damage.get(caster.getUniqueId()).get(e) - e.getWorld().getTime()) >= 10) {

                            } else {
                                break;
                            }
                        }
                        //spellDamage(caster, e, dps / 2.0, new ElementalStack(0, 0, 0, 50, 0));
                        Map h = damage.get(caster.getUniqueId());
                        h.put(e, e.getWorld().getTime());
                        damage.replace(caster.getUniqueId(), h);
                        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 0.25F, 1.0F);
                        break;
                    }
                }
            }
        }
    }

    public void warmup(Player p) {
        super.warmup(p);
        double radius = 1;
        for (double height2 = 0; height2 <= 3; height2+=1) {
            radius+=0.4;
            for (double alpha = 0; alpha < Math.PI; alpha += Math.PI / 8) {
                Location firstLocation = p.getLocation().clone().add(radius * Math.cos(alpha), height2, radius * Math.sin(alpha));
                Location secondLocation = p.getLocation().clone().add(radius * Math.cos(alpha + Math.PI), height2, radius * Math.sin(alpha + Math.PI));
                p.getWorld().spawnParticle(Particle.FLAME, firstLocation, 1, 0, 0.01, 0.01, 0.01);
                p.getWorld().spawnParticle(Particle.FLAME, secondLocation, 1, 0, 0.01, 0.01, 0.01);
            }
        }
    }

}
