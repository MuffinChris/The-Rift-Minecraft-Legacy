package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blaze extends Skill {

    private Main main = Main.getInstance();

    int movespeed = 8;
    int duration = 8;
    int spotduration = 3;
    double damage = 80;
    double apscale = 0.05;
    double radius = 0.75;

    Map<Player, List<BlazeSpot>> blazeLocations;

    public static Map<Player, HashMap<LivingEntity, Integer>> ticks;

    public static Map<Player, HashMap<LivingEntity, Integer>> getTicks() {
        return ticks;
    }

    public Blaze() {
        super("Blaze", 100, 25 * 20, 0, 10, "%player% has shot a fireball!", "CAST");
        blazeLocations = new HashMap<>();
        ticks = new HashMap<>();
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fFor &e" + duration + " &fseconds, leave a trail of flame"));
        desc.add(Main.color("&fin your wake and increase your"));
        desc.add(Main.color("&fmovement speed by &a" + movespeed + " &fpoints."));
        desc.add(Main.color("&fThe flame trail deals &b" + getDmg(p) + " &fevery half a second."));
        return desc;
    }

    public void cast(Player p) {
        super.cast(p);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), movespeed, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateStats();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BURN, 1.0F, 1.0F);

        if (blazeLocations.containsKey(p)) {
            blazeLocations.remove(p);
        }

        blazeLocations.put(p, new ArrayList<>());

        if (ticks.containsKey(p)) {
            ticks.remove(p);
        }

        ticks.put(p, new HashMap<>());
        new BukkitRunnable() {
            int times = 0;
            public void run() {
                if (!p.isOnline()) {
                    blazeLocations.remove(p);
                    ticks.remove(p);
                    cancel();
                    return;
                }
                if (p.isDead()) {
                    blazeLocations.remove(p);
                    ticks.remove(p);
                    cancel();
                    return;
                }
                times++;
                if (times >= duration * 20 + spotduration * 20) {
                    blazeLocations.remove(p);
                    ticks.remove(p);
                    cancel();
                    return;
                }
                boolean fail = false;
                List<BlazeSpot> remB = new ArrayList<>();
                for (BlazeSpot b : blazeLocations.get(p)) {
                    if (b != null && b.getLifetime() > 0) {
                        if (b.getLoc().getWorld() == p.getWorld() && b.getLoc().distance(p.getEyeLocation()) <= 0.75) {
                            fail = true;
                            break;
                        }
                    }
                    if (b != null && b.getLifetime() <= 0) {
                        remB.add(b);
                    }
                }

                for (BlazeSpot b : remB) {
                    blazeLocations.get(p).remove(b);
                }
                for (Player p : ticks.keySet()) {
                    List<LivingEntity> rem = new ArrayList<>();
                    for (LivingEntity ent : ticks.get(p).keySet()) {
                        ticks.get(p).replace(ent, ticks.get(p).get(ent) - 1);
                        if (ticks.get(p).get(ent) <= 0) {
                            rem.add(ent);
                        }
                    }
                    for (LivingEntity ent : rem) {
                        ticks.get(p).remove(ent);
                    }
                }
                List<LivingEntity> alreadyLooped = new ArrayList<>();
                for (BlazeSpot b : blazeLocations.get(p)) {
                    for (LivingEntity ent : b.getLoc().getNearbyLivingEntities(radius)) {
                        if (!alreadyLooped.contains(ent)) {
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

                            if (ticks.containsKey(p) && ticks.get(p).containsKey(ent)) {
                            } else {
                                ticks.get(p).put(ent, 10);
                                if (ent.getHealth() < getDmg(p) && !(ent instanceof Player)) {
                                    ent.setFireTicks(Math.min(20 + ent.getFireTicks(), 60));
                                }
                                Skill.spellDamageStatic(p, ent, getDmg(p));
                                ent.setFireTicks(Math.min(ent.getFireTicks() + 20, 60));
                            }
                            alreadyLooped.add(ent);
                        }
                    }
                }
                if (!fail) {
                    if (!(times >= duration * 20)) {
                        blazeLocations.get(p).add(new BlazeSpot(p, p.getEyeLocation().subtract(new Vector(0, p.getHeight() * 0.7, 0)), getDmg(p), 0.75, spotduration));
                    }
                    if (times == duration * 20) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BURN, 1.0F, 1.0F);
                    }
                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

}
