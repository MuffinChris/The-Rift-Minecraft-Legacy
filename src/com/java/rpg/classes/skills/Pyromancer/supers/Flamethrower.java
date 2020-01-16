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

    private double damage = 40;

    private double apscale = 0.25;

    private int range = 4;

    public Flamethrower() {
        super("Flamethrower", 0, 1 * 20, 0, 0, "%player% has shot a fireball!", "TOGGLE-CAST");
        setToggleMana(25);
        setToggleTicks(1);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSpew flame from your hand traveling &e" + range + "&f blocks."));
        desc.add(Main.color("&fIt deals &b" + getDmg(p) + " &fdamage per second"));
        desc.add(Main.color("&fand ignites them for 5 seconds."));
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



        return false;
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);

    }

    public int toggleInit(Player p) {

        return super.toggleInit(p);
    }

    public void spewFlame(Player caster, double damage) {
        Location loc = caster.getLocation();
        for (double d = 0; d<=range; d-=0.1) {
            for (double i = -2.5; i <= 2.5; i+=0.1) {
                for (double z = -0.5; z <= 0.5; z+=0.1) {
                    Location far = loc.clone().getDirection().multiply(d).toLocation(loc.getWorld());
                    far.clone().multiply(new Vector(0, 0, 0));
                    loc.clone().add(new Vector()).getWorld().spawnParticle(Particle.FLAME, loc, 1, 0.01, 0.01, 0.01, 0.01, null, true);
                }
            }
        }

    }

    public void lightEntities(Entity e, Player caster, Location loc, double damage) {
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 25, 0.12, 0.12, 0.12, 0.12,null, true);
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
            if (ent.getHealth() < damage && !(ent instanceof Player)) {
                ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
            }
            spellDamage(caster, ent, damage);
            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
            ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);

        }
    }
}

