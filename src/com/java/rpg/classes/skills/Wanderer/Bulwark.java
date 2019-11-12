package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Bulwark extends Skill implements Listener {

    private Main main = Main.getInstance();

    private int duration = 8;

    public Bulwark() {
        super("Bulwark", 25, 20 * 20, 0, 4, "%player% has shot a fireball!", "CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fTake a defensive stance, slowing yourself but"));
        desc.add(Main.color("&falso block all non-magic projectiles and take reduced damage."));
        desc.add(Main.color("&fThe stance lasts for &e" + duration + " &fseconds"));
        setDescription(desc);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
            Player p = (Player) e.getEntity();
            if (main.getRP(p).getStatuses().contains("Bulwark")) {
                e.setCancelled(true);
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F);
            }
        }
    }

    public void cast(Player p) {
        super.cast(p);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue("Bulwark", -15, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateWS();

        if (main.getRP(p).getStatuses().contains("Bulwark")) {
            main.getRP(p).getStatuses().remove("Bulwark");
        }
        main.getRP(p).getStatuses().add("Bulwark");

        new BukkitRunnable() {
            public void run() {
                if (main.getRP(p).getStatuses().contains("Bulwark")) {
                    main.getRP(p).getStatuses().remove("Bulwark");
                }
            }
        }.runTaskLater(Main.getInstance(), duration * 20);

        new BukkitRunnable() {
            int times = 0;
            public void run() {
                if (times * 20 <= duration * 20) {
                    p.getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation().subtract(new Vector(0, 0.3, 0)), 50, 0.2, 0.1, 0.1, 0.1);
                } else {
                    cancel();
                }
                times++;
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);

        p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0F, 1.0F);
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0F, 1.0F);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration * 20, 1));

        p.getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation(), 50, 0.2, 0.1, 0.1, 0.1);

    }

}
