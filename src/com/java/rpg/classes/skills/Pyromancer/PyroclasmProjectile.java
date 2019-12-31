package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class PyroclasmProjectile {

    private double damage = 100;
    private double empowered = 1.25;
    private int range = 8;
    private int duration = 20 * 15;
    private int maxbounces = 25;
    private double ratio = 0.9;
    private Map<UUID, LivingEntity> target;
    private Map<UUID, LivingEntity> lastTarget;
    private Map<UUID, Location> clasm;
    double travelspeed = 0.3;
    private Map<Location, Integer> fireblocks;

    private Main main = Main.getInstance();

    public PyroclasmProjectile(Player p, LivingEntity t, double damage, double empowered, int range, int duration, int maxbounces, double ratio, double travelspeed) {
        this.damage = damage;
        this.empowered = empowered;
        this.range = range;
        this.duration = duration;
        this.maxbounces = maxbounces;
        this.ratio = ratio;
        this.travelspeed = travelspeed;
        fireblocks = new HashMap<>();
        target = new HashMap<>();
        clasm = new HashMap<>();
        lastTarget = new HashMap<>();
        if (clasm.containsKey(p.getUniqueId())) {
            clasm.remove(p.getUniqueId());
        }
        if (target.containsKey(p.getUniqueId())) {
            target.remove(p.getUniqueId());
        }
        target.put(p.getUniqueId(), t);
        clasm.put(p.getUniqueId(), p.getEyeLocation());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        new BukkitRunnable() {
            int times = 0;
            int bounces = 0;
            int hangingticks = 0;
            public void run() {
                List<Location> removeFL = new ArrayList<>();
                for (Location fl : fireblocks.keySet()) {
                    if (fireblocks.get(fl) <= 50) {
                        fireblocks.replace(fl, fireblocks.get(fl) + 1);
                    } else {
                        if (fl.getBlock().getType() == Material.FIRE) {
                            fl.getBlock().setType(Material.AIR);
                            fl.getBlock().removeMetadata("noFire", Main.getInstance());
                        }
                        removeFL.add(fl);
                    }
                }
                for (Location fl : removeFL) {
                    fireblocks.remove(fl);
                }
                LivingEntity nadoEnt = null;
                EntityType e = EntityType.ZOMBIE;
                double dist = 999;
                Location locOf = clasm.get(p.getUniqueId());
                if (target.containsKey(p.getUniqueId())) {
                    if (target.get(p.getUniqueId()).isDead()) {
                        target.remove(p.getUniqueId());
                    }
                }
                if (lastTarget.containsKey(p.getUniqueId())) {
                    if (lastTarget.get(p.getUniqueId()).isDead()) {
                        lastTarget.remove(p.getUniqueId());
                    }
                }
                if (!target.containsKey(p.getUniqueId())) {
                    DecimalFormat dF = new DecimalFormat("#");
                    List<LivingEntity> nearbyEnts = getNearbyEnts(locOf, p);
                    boolean found = false;
                    if (nearbyEnts.size() > 0) {
                        LivingEntity randomEnt = (LivingEntity) nearbyEnts.toArray()[Integer.valueOf(dF.format(Math.floor(Math.random() * nearbyEnts.size())))];
                        if (!(lastTarget.containsKey(p.getUniqueId()) && lastTarget.get(p.getUniqueId()) == randomEnt)) {
                            nadoEnt = randomEnt;
                            found = true;
                        }
                    }
                    if (!found) {
                        hangingticks++;
                        if (hangingticks > 40) {
                            times = duration + 100;
                        }
                    }
                    if (nadoEnt instanceof LivingEntity) {
                        target.put(p.getUniqueId(), nadoEnt);
                        locOf = locOf.add(target.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, target.get(p.getUniqueId()).getHeight() * 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                        hangingticks = 0;
                    }
                    /*if (!(target.get(p.getUniqueId()) instanceof LivingEntity)) {
                        times = duration + 100;
                    }*/
                } else {
                    if (target.get(p.getUniqueId()).getWorld() != p.getWorld()) {
                        lastTarget.put(p.getUniqueId(), target.get(p.getUniqueId()));
                        target.remove(p.getUniqueId());
                        return;
                    }
                    locOf = locOf.add(target.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, target.get(p.getUniqueId()).getHeight() * 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    if (locOf.distance(target.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, target.get(p.getUniqueId()).getHeight() * 0.5, 0))) <= 0.5) {
                        if (target.get(p.getUniqueId()).getFireTicks() > 0) {
                            Skill.spellDamageStatic(p, target.get(p.getUniqueId()), damage * empowered * (Math.pow(ratio, bounces)));
                            target.get(p.getUniqueId()).setFireTicks(Math.min(80 + target.get(p.getUniqueId()).getFireTicks(), 100));
                            target.get(p.getUniqueId()).getWorld().playSound(target.get(p.getUniqueId()).getEyeLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                        } else {
                            target.get(p.getUniqueId()).getWorld().playSound(target.get(p.getUniqueId()).getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
                            if (target.get(p.getUniqueId()).getHealth() < damage * (Math.pow(ratio, bounces)) && !(target.get(p.getUniqueId()) instanceof Player)) {
                                target.get(p.getUniqueId()).setFireTicks(Math.min(80 + target.get(p.getUniqueId()).getFireTicks(), 100));
                            }
                            Skill.spellDamageStatic(p, target.get(p.getUniqueId()), damage * (Math.pow(ratio, bounces)));
                            target.get(p.getUniqueId()).setFireTicks(Math.min(80 + target.get(p.getUniqueId()).getFireTicks(), 100));
                        }
                        if (lastTarget.containsKey(p.getUniqueId())) {
                            lastTarget.remove(p.getUniqueId());
                        }
                        lastTarget.put(p.getUniqueId(), target.get(p.getUniqueId()));
                        target.remove(p.getUniqueId());
                        times-=20;
                        bounces++;
                        /*if (target.get(p.getUniqueId()) instanceof Player) {
                            bounces+=9;
                        }*/
                        if (bounces >= maxbounces) {
                            times = duration;
                        }
                    }
                }
                makeProjectile(locOf.clone(), p);
                clasm.remove(p.getUniqueId());
                clasm.put(p.getUniqueId(), locOf);
                times++;
                if (times >= duration) {
                    cancel();
                    if (lastTarget.containsKey(p.getUniqueId())) {
                        lastTarget.remove(p.getUniqueId());
                    }
                    if (clasm.containsKey(p.getUniqueId())) {
                        clasm.remove(p.getUniqueId());
                    }
                    if (target.containsKey(p.getUniqueId())) {
                        target.remove(p.getUniqueId());
                    }
                    new BukkitRunnable() {
                        public void run() {
                            List<Location> removeFL = new ArrayList<>();
                            for (Location fl : fireblocks.keySet()) {
                                if (fireblocks.get(fl) <= 50) {
                                    fireblocks.replace(fl, fireblocks.get(fl) + 1);
                                } else {
                                    if (fl.getBlock().getType() == Material.FIRE) {
                                        fl.getBlock().setType(Material.AIR);
                                        fl.getBlock().removeMetadata("noFire", Main.getInstance());
                                    }
                                    removeFL.add(fl);
                                }
                            }
                            for (Location fl : removeFL) {
                                fireblocks.remove(fl);
                            }
                            if (fireblocks.isEmpty()) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.getInstance(), 1, 1);
                    /*for (Location fl : fireblocks.keySet()) {
                        if (fl.getBlock().getType() == Material.FIRE) {
                            fl.getBlock().setType(Material.AIR);
                        }
                    }
                    fireblocks.clear();*/
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
    public void makeProjectile(Location loc, Player caster) {
        /*double radius = 0;
        double height2 = 0;
        while (radius <= 0.6) {
            for (double alpha = 0; alpha < Math.PI; alpha += Math.PI / 4) {
                Location firstLocation = loc.clone().add(radius * Math.cos(alpha), height2, radius * Math.sin(alpha));
                Location secondLocation = loc.clone().add(radius * Math.cos(alpha + Math.PI), height2, radius * Math.sin(alpha + Math.PI));
                loc.getWorld().spawnParticle(Particle.FLAME, firstLocation, 0, 0.001, 0.001, 0.001, 0.001, null, true);
                loc.getWorld().spawnParticle(Particle.FLAME, secondLocation,  0, 0.001, 0.001, 0.001, 0.001,null, true);
            }
            radius+=0.2;
            height2+=0.1;
        }

        radius = 0.6;
        height2 = 0.3;
        while (radius > 0) {
            for (double alpha = 0; alpha < Math.PI; alpha += Math.PI / 4) {
                Location firstLocation = loc.clone().add(radius * Math.cos(alpha), height2, radius * Math.sin(alpha));
                Location secondLocation = loc.clone().add(radius * Math.cos(alpha + Math.PI), height2, radius * Math.sin(alpha + Math.PI));
                loc.getWorld().spawnParticle(Particle.FLAME, firstLocation, 0, 0.001, 0.001, 0.001, 0.001,null, true);
                loc.getWorld().spawnParticle(Particle.FLAME, secondLocation,  0, 0.001, 0.001, 0.001, 0.001,null, true);
            }
            radius-=0.2;
            height2+=0.1;
        }*/
        loc.getWorld().spawnParticle(Particle.FLAME, loc,  10, 0.2, 0.03, 0.2, 0.05,null, true);
        if (loc.getBlock().getType() == Material.AIR) {
            fireblocks.put(loc, 0);
            loc.getBlock().setMetadata("noFire", new FixedMetadataValue(Main.getInstance(), "noFire"));
            loc.getBlock().setType(Material.FIRE);
        }
        //loc.getWorld().spawnParticle(Particle.LAVA, loc, 1, 0.0, 0.001, 0.001, 0.001);
    }

    public List<LivingEntity> getNearbyEnts(Location loc, Player p) {
        List<LivingEntity> list = new ArrayList<>();
        for (LivingEntity ent : loc.getNearbyLivingEntities(range)) {
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
            list.add(ent);
        }
        return list;
    }

}
