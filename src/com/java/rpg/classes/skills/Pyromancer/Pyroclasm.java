package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
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

    private double damage = 100;
    private double empowered = 1.75;
    private int range = 11;
    private int duration = 20 * 10;
    private int maxbounces = 18;
    private double ratio = 0.75;
    double travelspeed = 0.9;
    private double apscale = 0.3;

    public Pyroclasm() {
        super("Pyroclasm", 150, 15 * 20, 40, 25, "%player% has shot a fireball!", "CAST-TARGET", Material.BLAZE_ROD);
        setTargetRange(range);
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Launch a flaming projectile at the target."));
        desc.add(Main.color("&7The projectile leaves a trail of flame."));
        desc.add(Main.color("&7It has a base lifetime of &e" + df.format((duration * 1.0)/20.0) + " &7seconds"));
        desc.add(Main.color("&7and bounces around targets within &e" + range + " &7blocks."));
        desc.add(Main.color("&7Each hit deals &b" + getDmg(p) + " &7magic damage."));
        desc.add(Main.color("&7If the target is on fire it deals &e" + empowered + "x &7more damage."));
        desc.add(Main.color("&7Each bounce increases the lifetime by &e1 &7second."));
        desc.add(Main.color("&7Each bounce deals &e" + df.format(ratio * 100.0) + "% &7damage"));
        desc.add(Main.color("&7of the previous bounce."));
        desc.add(Main.color("&7The projectile bounces a maximum of &e" + maxbounces + " &7times."));
        return desc;
    }


    public void target(Player p, LivingEntity t) {
        super.target(p, t);
        PyroclasmProjectile proj = new PyroclasmProjectile(p, t, getDmg(p), empowered, range, duration, maxbounces, ratio, travelspeed);
    }

    public void targetParticles(Player p, LivingEntity t) {
        t.getWorld().spawnParticle(Particle.LAVA, t.getEyeLocation().subtract(new Vector(0, t.getHeight() * 0.25, 0)), 10, 0.01, 0.01, 0.01, 0.01,null, true);
    }

    public void warmup(Player p) {
        super.warmup(p);
        //p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().add(new Vector(0, 0.1, 0)), 10, 0.04, 0.04, 0.04, 0.04,null, true);
        Location finalLoc;
        Vector dir = p.getLocation().getDirection();
        finalLoc = p.getLocation().add(p.getLocation().getDirection().multiply(3.5).add(new Vector(-dir.multiply(3.5).getX(), 1.5, -dir.multiply(3.5).getZ()).normalize()));
        p.getWorld().spawnParticle(Particle.LAVA, finalLoc, 1, 0.01, 0.01, 0.01, 0.01,null, true);

        double radius = 0.75;
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/16) {
            Location loc = finalLoc;
            Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), 0.5, radius * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), 0.5, radius * Math.sin( alpha + Math.PI ) );
            //Location firstLocation = loc.clone().add( Math.cos( alpha ), Math.sin( alpha ) + 1, Math.sin( alpha ) );
            //Location secondLocation = loc.clone().add( Math.cos( alpha + Math.PI ), Math.sin( alpha ) + 1, Math.sin( alpha + Math.PI ) );
            p.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0.0, 0.0, 0.0, 0.0, null, true);
            p.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0.0, 0.0, 0.0, 0.0,null, true);
        }
    }

    @EventHandler
    public void noFireSpread(BlockSpreadEvent e) {
        Location fireLoc = e.getSource().getLocation();
        if (fireLoc.getBlock().hasMetadata("noFire")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noFireIgnite(BlockIgniteEvent e) {
        if (e.getIgnitingBlock() != null) {
            Location fireLoc = e.getIgnitingBlock().getLocation();
            if (fireLoc.getBlock().hasMetadata("noFire")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noFireBurn(BlockBurnEvent e) {
        if (e.getIgnitingBlock() != null) {
            Location fireLoc = e.getIgnitingBlock().getLocation();
            if (fireLoc.getBlock().hasMetadata("noFire")) {
                e.setCancelled(true);
            }
        }
    }

}
