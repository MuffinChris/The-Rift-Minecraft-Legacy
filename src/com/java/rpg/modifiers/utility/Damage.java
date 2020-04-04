package com.java.rpg.modifiers.utility;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Damage {

    private LivingEntity target;
    private PhysicalStack physicalDamage;
    private double magicDamage;
    private double trueDamage;
    private ElementalStack elementalDamage;

    private DamageType dt;
    private Player caster;
    private int lifetime;
    public enum DamageType
    {
        PHYSICAL, PUNCTURE, IMPACT, SLASH, MAGIC, TRUE, ICE, FIRE, EARTH, ELECTRIC, AIR
    }
    private int task;

    public Damage(Player caster, LivingEntity p, PhysicalStack ps, double magicDamage, double trueDamage, ElementalStack ed, int lifetime) {
        target = p;
        this.physicalDamage = ps;
        this.magicDamage = magicDamage;
        this.trueDamage = trueDamage;
        elementalDamage = ed;
        this.caster = caster;
        this.lifetime = lifetime;
        /*
        BukkitScheduler sched = Bukkit.getScheduler();
        task = sched.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                if (!dt.toString().contains("ATTACK") && !target.isDead()) {
                    target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
                }
                Bukkit.broadcastMessage("Doing another attack round.");
                target.damage(damage, caster);
                if (!dt.toString().contains("ATTACK") && !target.isDead()) {
                    target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
                }
                sched.cancelTask(task);
            }
        }, 1, 1);*/
    }

    public int getLifetime() {
        return lifetime;
    }

    public void decLifetime() {
        lifetime--;
    }

    public int getTask() {
        return task;
    }

    public DamageType getDamageType() {
        return dt;
    }

    public PhysicalStack getPhysicalDamage() {
        return physicalDamage;
    }
    public double getMagicDamage() {
        return magicDamage;
    }
    public double getTrueDamage() {
        return trueDamage;
    }
    public ElementalStack getElementalDamage() {
        return elementalDamage;
    }

    public Player getCaster() {
        return caster;
    }

    public LivingEntity getPlayer() {
        return target;
    }

    public void scrub() {
        target = null;
        dt = null;
        caster = null;
    }

    public String toString() {
        return "Caster: "  + caster.getName() + ", Target: " + target.getType() + ", DT: " + dt.toString();
    }

}
