package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Combust extends Skill implements Listener {

    private Main main = Main.getInstance();
    private double damage = 120;

    private double damagePerEntity = 20;

    private double apscale = 0.4;

    private int range = 12;

    public Combust() {
        super("Combust", 200, 30 * 20, 30, 8, "%player% has shot a fireball!", "CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fLaunch an explosive projectile."));
        desc.add(Main.color("&fIt explodes at the nearest target hit for &b" + damage + " &fdamage."));
        desc.add(Main.color("&fAfter a delay, a second explosion affects a huge radius of &e" + range + "&f."));
        desc.add(Main.color("&fThis second explosion deals &b" + damage + " &fdamage + &b" + damagePerEntity + " &fdamage per nearby entity."));
        setDescription(desc);
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public double getDmgPerEntity(Player p) {
        return damagePerEntity + main.getRP(p).getAP() * apscale;
    }

    public void cast(Player p) {
        super.cast(p);

    }

    public void explode(Player caster, Location loc) {
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 50, 0.04, 0.12, 0.12, 0.12);
        for (LivingEntity ent : loc.getNearbyLivingEntities(1.1)) {
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
            new BukkitRunnable() {
                public void run() {
                    if (ent.getHealth() < damage && !(ent instanceof Player)) {
                        ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
                    }
                    spellDamage(caster, ent, damage);
                    ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
                    ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
                    ent.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, ent.getLocation(), 1, 1, 0.12, 0.12, 0.12);
                }
            }.runTaskLater(Main.getInstance(), 1L);
        }
    }

}
