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

    public Pyroclasm() {
        super("Pyroclasm", 150, 15 * 20, 40, 6, "%player% has shot a fireball!", "CAST-TARGET", 4, 8, 12, 16);
        DecimalFormat df = new DecimalFormat("#");
        setTargetRange(range);
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fLaunch a flaming projectile at the target."));
        desc.add(Main.color("&fThe projectile leaves a trail of flame."));
        desc.add(Main.color("&fIt has a base lifetime of &e" + df.format((duration * 1.0)/20.0) + " &fseconds"));
        desc.add(Main.color("&fand bounces around targets within &e" + range + " &fblocks."));
        desc.add(Main.color("&fEach hit deals &b" + damage + " &fmagic damage."));
        desc.add(Main.color("&fIf the target is on fire it deals &e" + empowered + "x &fmore damage."));
        desc.add(Main.color("&fEach bounce increases the lifetime by &e1 &fsecond."));
        desc.add(Main.color("&fEach bounce deals &e" + df.format(ratio * 100.0) + "% &fdamage"));
        desc.add(Main.color("&fof the previous bounce."));
        desc.add(Main.color("&fThe projectile bounces a maximum of &e" + maxbounces + " &ftimes."));
        setDescription(desc);
    }


    public void target(Player p, LivingEntity t) {
        super.target(p, t);
        PyroclasmProjectile proj = new PyroclasmProjectile(p, t, damage, empowered, range, duration, maxbounces, ratio, travelspeed);
    }

    public void targetParticles(Player p, LivingEntity t) {
        t.getWorld().spawnParticle(Particle.LAVA, t.getEyeLocation().subtract(new Vector(0, t.getHeight() * 0.25, 0)), 10, 0, 0.01, 0.01, 0.01);
    }

    public void warmup(Player p) {
        super.warmup(p);
        p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().add(new Vector(0, 0.1, 0)), 10, 0, 0.04, 0.04, 0.04);
        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(new Vector(0, 0.5, 0)), 10, 0, 0.04, 0.04, 0.04);
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
