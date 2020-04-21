package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.party.Party;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.VortexEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class Pyroclasm extends Skill implements Listener {

    private Main main = Main.getInstance();

    private ElementalStack eDmg = new ElementalStack(0, 0, 25, 100, 0);
    private double magicDamage = 100;
    private double empowered = 1.75;
    private int range = 11;
    private int duration = 20 * 10;
    private int maxbounces = 18;
    private double ratio = 0.75;
    double travelspeed = 0.9;
    private double apscale = 0.3;

    public Pyroclasm() {
        super("Pyroclasm", 150, 15 * 20, 40, 25, SkillType.TARGET, null, Material.BLAZE_ROD);
        setTargetRange(range);
    }

    public double getMagicDmg(Player p) {
        return magicDamage + main.getRP(p).getAP() * apscale;
    }
    public ElementalStack getEDmg(Player p) {
        return eDmg;
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Launch a flaming projectile at the target."));
        desc.add(Main.color("&7The projectile leaves a trail of flame."));
        desc.add(Main.color("&7It has a base lifetime of &e" + df.format((duration * 1.0)/20.0) + " &7seconds"));
        desc.add(Main.color("&7and bounces around targets within &e" + range + " &7blocks."));
        desc.add(Main.color("&7Each hit deals &b" + getMagicDmg(p) + " &7+ " + getEDmg(p).getFancyNumberFire() + " &7magic damage."));
        desc.add(Main.color("&7If the target is on fire it deals &e" + empowered + "x &7more damage."));
        desc.add(Main.color("&7Each bounce increases the lifetime by &e1 &7second."));
        desc.add(Main.color("&7Each bounce deals &e" + df.format(ratio * 100.0) + "% &7damage"));
        desc.add(Main.color("&7of the previous bounce."));
        desc.add(Main.color("&7The projectile bounces a maximum of &e" + maxbounces + " &7times."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Launch a flaming projectile at the target."));
        desc.add(Main.color("&7The projectile leaves a trail of flame."));
        desc.add(Main.color("&7It has a base lifetime of &e" + df.format((duration * 1.0)/20.0) + " &7seconds"));
        desc.add(Main.color("&7and bounces around targets within &e" + range + " &7blocks."));
        desc.add(Main.color("&7Each hit deals &b" + magicDamage + " &7+ " + eDmg.getFancyNumberFire() + " &7magic damage."));
        desc.add(Main.color("&7If the target is on fire it deals &e" + empowered + "x &7more damage."));
        desc.add(Main.color("&7Each bounce increases the lifetime by &e1 &7second."));
        desc.add(Main.color("&7Each bounce deals &e" + df.format(ratio * 100.0) + "% &7damage"));
        desc.add(Main.color("&7of the previous bounce."));
        desc.add(Main.color("&7The projectile bounces a maximum of &e" + maxbounces + " &7times."));
        return desc;
    }


    public void target(Player p, LivingEntity t) {
        super.target(p, t);
        PyroclasmProjectile proj = new PyroclasmProjectile(p, t, getMagicDmg(p), getEDmg(p), empowered, range, duration, maxbounces, ratio, travelspeed);
    }

    public void targetParticles(Player p, LivingEntity t) {
        t.getWorld().spawnParticle(Particle.LAVA, t.getEyeLocation().subtract(new Vector(0, t.getHeight() * 0.25, 0)), 10, 0.01, 0.01, 0.01, 0.01,null, true);
    }

    public void warmup(Player p) {
        super.warmup(p);
        Location finalLoc;
        Vector dir = p.getLocation().getDirection();
        finalLoc = p.getLocation().add(p.getLocation().getDirection().multiply(3.5).add(new Vector(-dir.multiply(3.5).getX(), 1.5, -dir.multiply(3.5).getZ()).normalize()));
        p.getWorld().spawnParticle(Particle.LAVA, finalLoc, 1, 0.01, 0.01, 0.01, 0.01,null, true);
    }

}
