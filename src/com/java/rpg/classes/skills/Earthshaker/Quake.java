package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class Quake extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 8;

    public Quake(String name, int manaCost, double cooldown, int warmup, int level, String flavor, String type) {
        super(name, manaCost, cooldown, warmup, level, flavor, type);
    }

    public void cast(Player p) {
    	super.cast(p);
    	makeCircle(p);
    	for(int i = 0; i < 20; i++)
    		//caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
    	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
                if (p.equals(pl)) {
                    continue;
                    //p.getWorld().spawnParticle(Particle.BLOCKDUST, ent.getLocation(), 20, 0.04, 0.04, 0.04, 0.04);
                }
            }
            ent.setKiller(p);
            /*} else {
                spellDamage(p, ent, damage);
                p.getWorld().spawnParticle(Particle.BLOCKDUST, ent.getLocation(), 20, 0.04, 0.04, 0.04, 0.04);*/
    	}
    }
    
    public void makeCircle(Player p) {
    	Location center = p.getLocation();
    	double mag;
    	double angle;
    	for(int i = 0; i < 40; i++) {
    		mag = Math.random() * range;
    		angle = Math.random() * Math.PI;
    		//p.getWorld.spawnParticle(Particle.BLOCKDUST, center.add(mag * Math.cos(angle), .1, mag * Math.sin(angle), 1, .04, .04, .04, .04 );
    	}
    }
    
}