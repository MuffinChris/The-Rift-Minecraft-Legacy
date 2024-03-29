package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Bulwark extends Skill implements Listener {

    private Main main = Main.getInstance();

    private int duration = 8;

    public Bulwark() {
        super("Bulwark", 25, 20 * 20, 0, 4, SkillType.CAST, null, null);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fTake a defensive stance, slowing yourself but"));
        desc.add(Main.color("&falso block all non-magic projectiles and take reduced damage."));
        desc.add(Main.color("&fThe stance lasts for &e" + duration + " &fseconds"));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fTake a defensive stance, slowing yourself but"));
        desc.add(Main.color("&falso block all non-magic projectiles and take reduced damage."));
        desc.add(Main.color("&fThe stance lasts for &e" + duration + " &fseconds"));
        return desc;
    }

    public void cast(Player p) {
        super.cast(p);

        clearTasks(p);
        main.getRP(p).getWalkspeed().clearBasedTitle(getName(), p);
        main.getRP(p).updateStats();

        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue("Bulwark", -15, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateWS();

        if (main.getRP(p).getStatuses().contains("Bulwark")) {
            main.getRP(p).getStatuses().remove("Bulwark");
        }
        main.getRP(p).getStatuses().add("Bulwark");

        BukkitTask cancelBulwark = new BukkitRunnable() {
            public void run() {
                if (!p.isOnline()) {
                    cancel();
                    return;
                }
                if (main.getRP(p).getStatuses().contains("Bulwark")) {
                    main.getRP(p).getStatuses().remove("Bulwark");
                }
            }
        }.runTaskLater(Main.getInstance(), duration * 20);

        BukkitTask runBulwark = new BukkitRunnable() {
            int times = 0;
            public void run() {
                if (!p.isOnline()) {
                    cancel();
                    return;
                }
                if (p.isDead()) {
                    cancel();
                    return;
                }
                if (times <= duration * 20) {
                    p.getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation().subtract(new Vector(0, 0.5, 0)), 15, 0.2, 0.1, 0.2, 0.05,null, true);
                } else {
                    cancel();
                }
                times++;
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);

        addTask(p, cancelBulwark.getTaskId());
        addTask(p, runBulwark.getTaskId());

        p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0F, 1.0F);
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0F, 1.0F);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 1));

        p.getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation(), 50, 0.1, 0.1, 0.1, 0.1,null, true);

    }

}
