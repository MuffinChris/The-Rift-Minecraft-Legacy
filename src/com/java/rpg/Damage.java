package com.java.rpg;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class Damage {

    private LivingEntity target;
    private Double damage;
    private DamageType dt;
    private Player caster;
    private int lifetime;
    public enum DamageType
    {
        ATTACK, ATTACK_MAGIC, ATTACK_TRUE, SPELL_MAGIC, SPELL_PHYSICAL, SPELL_TRUE
    }
    private int task;

    public Damage(Player caster, LivingEntity p, DamageType dt, Double damage, int lifetime) {
        target = p;
        this.dt = dt;
        this.damage = damage;
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

    public double getDamage() {
        return damage;
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
        damage = null;
    }

    public String toString() {
        return "Caster: "  + caster.getName() + ", Target: " + target.getType() + ", DT: " + dt.toString() + ", Damage: " + damage;
    }

}
