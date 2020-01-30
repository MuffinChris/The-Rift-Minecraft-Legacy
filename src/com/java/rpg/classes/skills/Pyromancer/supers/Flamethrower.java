package com.java.rpg.classes.skills.Pyromancer.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Flamethrower extends Skill {

    private Main main = Main.getInstance();

    private double damage = 150;

    private double apscale = 0.5;

    private int range = 4;

    private int tickrate = 2;

    public Flamethrower() {
        super("Flamethrower", 0, 30, 0, 0, "%player% has shot a fireball!", "TOGGLE-CAST");
        setToggleMana(5);
        setToggleTicks(tickrate);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSpew flame from your hand traveling &e" + range + "&f blocks."));
        desc.add(Main.color("&fIt deals &b" + getDmg(p) + " &fdamage per second"));
        desc.add(Main.color("&fand ignites them for 1 second."));
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public void cast(Player p) {
        super.cast(p);
    }

    public boolean toggleCont(Player p) {
        if (!super.toggleCont(p)) {
            return false;
        }
        spewFlame(p, getDmg(p)/(1.0 * (20/tickrate)));
        return false;
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        main.getRP(p).getWalkspeed().clearBasedTitle(getName(), p);
        main.getRP(p).updateWS();
        p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
        p.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation().add(new Vector(0, 0.25, 0)), 25, 0.01, 0.01, 0.01, 0.01, null, true);
    }

    public int toggleInit(Player p) {
        p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1.0F, 1.0F);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), -10, 0, 0, true));
        main.getRP(p).updateWS();
        return super.toggleInit(p);
    }

    public void spewFlame(Player caster, double damage) {
        caster.getLocation().getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 0.25F, 1.0F);
        Location origin = caster.getEyeLocation().clone().subtract(new Vector(0, 0.35, 0));
        Vector direction = origin.getDirection();
        List<LivingEntity> alreadyHit = new ArrayList<>();
        for (double i = 0; i < (range* 1.0 )/3.0; i += 0.25) {
            Location loc = origin.add(direction);
            loc.getWorld().spawnParticle(Particle.FLAME, loc, 10 + (int) i * 5, 0.01 + i / 1.5, 0.01 + i / 2.0, 0.01 + i / 1.5, 0.0001, null, true);

            for (LivingEntity ent : loc.getNearbyLivingEntities(i + 0.25)) {
                if (ent instanceof ArmorStand) {
                    continue;
                }
                if (alreadyHit.contains(ent)) {
                    continue;
                }
                if (ent instanceof Player) {
                    Player p = (Player) ent;
                    if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                        if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                            continue;
                        }
                    }
                    if (p.equals(caster)) {
                        continue;
                    }
                }
                if (Math.abs(ent.getLocation().getY() - loc.getY()) > i / 1.5) {
                    continue;
                }
                alreadyHit.add(ent);
                if (ent.getHealth() < damage && !(ent instanceof Player)) {
                    ent.setFireTicks(Math.min(20 + ent.getFireTicks(), 200));
                }
                spellDamage(caster, ent, damage);
                ent.setFireTicks(Math.min(20 + ent.getFireTicks(), 200));
                ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);

            }
        }

    }
}

