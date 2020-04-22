package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blaze extends Skill {

    private Main main = Main.getInstance();

    int movespeed = 7;
    int duration = 8;
    int spotduration = 3;
    double magicDamage = 80;
    double apscale = 0.05;
    double radius = 0.75;

    private ElementalStack eDmg = new ElementalStack(0, 0, 0, 75, 0);

    public Map<Player, List<BlazeSpot>> blazeLocations;
    private static Map<Player, HashMap<LivingEntity, Integer>> ticks;

    public Blaze() {
        super("Blaze", 100, 25 * 20, 0, 15, SkillType.CAST, null, Material.BLAZE_POWDER);
        blazeLocations = new HashMap<>();
        ticks = new HashMap<>();
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7For &e" + duration + " &7seconds, leave a trail of flame"));
        desc.add(Main.color("&7in your wake and increase your"));
        desc.add(Main.color("&7movement speed by &a" + movespeed + " &7points."));
        desc.add(Main.color("&7The flame trail deals &b" + getMagicDmg(p) + " &7+ " + getEDmg(p).getFancyNumberFire() + " &7every half a second."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7For &e" + duration + " &7seconds, leave a trail of flame"));
        desc.add(Main.color("&7in your wake and increase your"));
        desc.add(Main.color("&7movement speed by &a" + movespeed + " &7points."));
        desc.add(Main.color("&7The flame trail deals &b" + magicDamage + " &7+ " + eDmg.getFancyNumberFire() + " &7every half a second."));
        return desc;
    }

    public double getMagicDmg(Player p) {
        return magicDamage + main.getRP(p).getAP() * apscale;
    }
    public ElementalStack getEDmg(Player p) {
        return eDmg;
    }

    public void cast(Player p) {
        super.cast(p);

        clearTasks(p);
        main.getRP(p).getWalkspeed().clearBasedTitle(getName(), p);
        main.getRP(p).updateStats();

        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), movespeed, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateStats();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BURN, 1.0F, 1.0F);

        blazeLocations.remove(p);
        blazeLocations.put(p, new ArrayList<>());

        ticks.remove(p);
        ticks.put(p, new HashMap<>());

        BukkitTask blaze = new BukkitRunnable() {
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
                    for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(b.getLoc(), p, radius)) {
                        if (!alreadyLooped.contains(ent)) {
                            if (!(ticks.containsKey(p) && ticks.get(p).containsKey(ent))) {
                                ticks.get(p).put(ent, 10);
                                ent.setFireTicks(Math.min(ent.getFireTicks() + 20, 60));
                                spellDamage(p, ent, new PhysicalStack(), getEDmg(p), getMagicDmg(p), 0);
                            }
                            alreadyLooped.add(ent);
                        }
                    }
                }
                if (!fail) {
                    if (!(times >= duration * 20)) {
                        blazeLocations.get(p).add(new BlazeSpot(p.getEyeLocation().subtract(new Vector(0, p.getHeight() * 0.7, 0)), spotduration));
                    }
                    if (times == duration * 20) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BURN, 1.0F, 1.0F);
                    }
                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 1);

        addTask(p, blaze.getTaskId());
    }

}
