package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import org.bukkit.Location;
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

    int movespeed = 15;
    int duration = 8;
    int spotduration = 3;
    double damage = 20;
    double apscale = 0.25;

    Map<Player, List<BlazeSpot>> blazeLocations;

    public static Map<Player, HashMap<LivingEntity, Integer>> ticks;

    public static Map<Player, HashMap<LivingEntity, Integer>> getTicks() {
        return ticks;
    }

    public Blaze() {
        super("Blaze", 100, 15 * 20, 0, 4, "%player% has shot a fireball!", "CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fFor &e" + duration + " &fseconds, leave a trail of flame"));
        desc.add(Main.color("&fin your wake and increase your"));
        desc.add(Main.color("&fmovement speed by &a" + movespeed + " &fpoints."));
        desc.add(Main.color("&fThe flame trail deals &b" + damage + " &fevery half a second."));
        setDescription(desc);
        blazeLocations = new HashMap<>();
        ticks = new HashMap<>();
    }

    public void cast(Player p) {
        super.cast(p);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), movespeed, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateStats();
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
                times++;
                if (times == duration * 20) {
                    blazeLocations.remove(p);
                    cancel();
                    return;
                }
                boolean fail = false;
                for (BlazeSpot b : blazeLocations.get(p)) {
                    if (b != null && b.getLifetime() > 0) {
                        if (b.getLoc().distance(p.getEyeLocation()) <= 0.75) {
                            fail = true;
                            break;
                        }
                    }
                }
                if (!fail) {
                    blazeLocations.get(p).add(new BlazeSpot(p, p.getEyeLocation().subtract(new Vector(0, p.getHeight() * 0.7, 0)), damage + main.getRP(p).getAP() * apscale, 0.75, spotduration));
                }

            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

}
