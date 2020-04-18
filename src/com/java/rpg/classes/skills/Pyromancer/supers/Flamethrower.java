package com.java.rpg.classes.skills.Pyromancer.supers;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Flamethrower extends Skill {

    private Main main = Main.getInstance();

    private double damage = 50;
    private double apscale = 0.3;

    private double fireDmg = 150;
    private double fireScale = 0.25;

    private int range = 4;

    private int tickrate = 2;

    public Flamethrower() {
        super("Flamethrower", 0, 10, 0, 0, "%player% has shot a fireball!", "TOGGLE-CAST", Material.FIRE_CHARGE);
        setToggleMana(7);
        setToggleTicks(tickrate);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Spew flame from your hand traveling &e" + range + "&7 blocks."));
        desc.add(Main.color("&7It deals &b" + getDmg(p) + " &7damage per second"));
        desc.add(Main.color("&7and ignites them for 1 second. You are slowed while casting."));
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }
    public double getFireDmg(Player p) {
        return fireDmg + main.getRP(p).getAP() * fireScale;
    }

    public void cast(Player p) {
        super.cast(p);
    }

    public boolean toggleCont(Player p) {
        if (!super.toggleCont(p)) {
            return false;
        }
        spewFlame(p, getDmg(p)/(1.0 * (20/(tickrate * 1.0))), getFireDmg(p)/(1.0 * (20/(tickrate * 1.0))));
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
        p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), -12, 0, 0, true));
        main.getRP(p).updateWS();
        return super.toggleInit(p);
    }

    public void spewFlame(Player caster, double damage, double firedmg) {
        caster.getLocation().getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 0.15F, 1.0F);
        Location origin = caster.getEyeLocation().clone().subtract(new Vector(0, 0.35, 0));
        Vector direction = origin.getDirection();
        List<LivingEntity> alreadyHit = new ArrayList<>();
        for (double i = 0; i < (range* 1.0 )/3.0; i += 0.25) {
            Location loc = origin.add(direction);
            loc.getWorld().spawnParticle(Particle.FLAME, loc, 10 + (int) i * 5, 0.01 + i / 1.5, 0.01 + i / 2.0, 0.01 + i / 1.5, 0.0, null, true);

            for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(loc, caster, i + 2)) {
                if (alreadyHit.contains(ent)) {
                    continue;
                }
                Location dist = new Location(loc.getWorld(), loc.getX() - ent.getLocation().getX(), loc.getY() - ent.getLocation().getY(), loc.getZ() - ent.getLocation().getZ());
                if(Math.sqrt(dist.getX() * dist.getX() + dist.getZ() * dist.getZ()) <= (i * 1.05) + 0.45 && Math.abs(dist.getY()) < ent.getHeight()){
                    alreadyHit.add(ent);
                    ent.setFireTicks(Math.min(200, ent.getFireTicks() + 10));
                    spellDamage(caster, ent, damage, new ElementalStack(0, 0, 0, firedmg, 0));
                    ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 0.75F, 1.0F);
                }
                /*if(Math.sqrt(dist.getX() * dist.getX() + dist.getZ() * dist.getZ()) < 0.4 && Math.abs(dist.getY()) < ent.getHeight()){
                    alreadyHit.add(ent);
                    ent.setFireTicks(Math.min(20 + ent.getFireTicks(), 200));
                    spellDamage(caster, ent, damage, new ElementalStack(0, 0, 0, 5, 0));
                    ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
                }*/
                /*double dist = Math.sqrt(Math.pow(loc.getX() - ent.getLocation().getX(), 2) + Math.pow(loc.getZ() - ent.getLocation().getZ(), 2));
                if (!(dist < 3 + i)) {
                    continue;
                }
                if (!(Math.abs(ent.getLocation().add(new Vector(0, ent.getHeight()/2.0, 0)).getY() - loc.getY()) < ent.getHeight()/1.3 + i + 0.2)) {
                    continue;
                }
                alreadyHit.add(ent);
                */
            }
        }

    }
}

