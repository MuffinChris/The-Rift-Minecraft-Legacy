package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blaze extends Skill {

    private Main main = Main.getInstance();

    int movespeed = 3;
    int duration = 6;
    int spotduration = 3;
    double damage = 5;
    double apscale = 0.025;

    public Blaze() {
        super("Blaze", 100, 15 * 20, 0, 4, "%player% has shot a fireball!", "CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fFor &a" + duration + " &fseconds, leave a trail of flame"));
        desc.add(Main.color("&fin your wake and increase your"));
        desc.add(Main.color("&fmovement speed by &a" + movespeed + " &fpoints."));
        desc.add(Main.color("&fThe flame trail deals &c" + damage + " &fevery half a second."));
        setDescription(desc);
    }

    public void cast(Player p) {
        super.cast(p);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), movespeed, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateStats();

        new BukkitRunnable() {
            int times = 0;
            public void run() {
                times++;
                if (times * 10 == duration * 20) {
                    cancel();
                }
                BlazeSpot bs = new BlazeSpot(p, p.getEyeLocation(), damage + main.getRP(p).getAP() * apscale, 1, spotduration);

            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }

}
