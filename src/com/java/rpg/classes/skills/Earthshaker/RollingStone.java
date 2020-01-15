package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.skills.Pyromancer.PyroclasmProjectile;
import com.java.rpg.party.Party;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RollingStone extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int duration = 2 * 20;
    private int stun = 2;
    private int interval = 6;
    private int range = 6;
    private int rad = 2;
    
    public RollingStone() {
    	super("RollingStone", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST-TARGET");
    	DecimalFormat df = new DecimalFormat("#");
        setTargetRange(range);
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fCharge forwards with a shell of rocks around you"));
        desc.add(Main.color("&fEnemies around you are caught in the rockslide"));
        desc.add(Main.color("&fand will travel with you until you stop."));
        setDescription(desc);
    }
    
    public void cast(Player p) {
    	new BukkitRunnable() {
            int times = 16;
            public void run() {
                if(checkCollision(p))
                    cancel();
                increment(p);
                if(checkStep(p)) {
                    for (LivingEntity ent: p.getLocation().getNearbyLivingEntities(rad)) {
                    	if (ent instanceof Player) {
                            Player player = (Player) ent;
                            if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                                if (main.getPM().getParty(p).getPlayers().contains(player)) {
                                    continue;
                                }
                            }
                            if (player.equals(p)) {
                                continue;
                            }
                    	}
                    	ent.teleport(ent.getLocation().add(new Vector(0, 1, 0)));
                    }
                }
                if(times < 1) {
                    launch(p);
                    cancel();
                }
            	times--;
            }    
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
    
    public void warmup() {
    	
    }
    
    // moves the player and the mobs forwards
    public void increment(Player pl) {
    	Vector FDir = pl.getEyeLocation().getDirection();
    	Vector rand;
    	for(LivingEntity ent: pl.getLocation().getNearbyLivingEntities(rad)) {
    		rand = new Vector( .1f * Math.random(), .1f * Math.random(), .1f * Math.random());
    		if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player player = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(player)) {
                        continue;
                    }
                }
                if (player.equals(pl)) {
                    continue;
                }
            }
    		ent.setVelocity(FDir.multiply(0.5f).add(rand));
    	}
    	pl.setVelocity(FDir.multiply(0.5f));
    }
    
    // launch enemies forwards or something idk
    public void launch(Player pl) {
    	for (LivingEntity ent: pl.getLocation().getNearbyLivingEntities(rad)) {
    		if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player player = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    /*if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }*/
                }
                if (player.equals(pl)) {
                    continue;
                }
            }
    		ent.setVelocity(ent.getVelocity().add(new Vector(0, 0.5, 0)));
    	}
    }
    
    //check if the player hits a wall or something
    public boolean checkCollision(Player pl) {
    	Location loc = pl.getEyeLocation();
    	loc.add(pl.getEyeLocation().getDirection());
    	if(loc.getBlock().getType() != Material.AIR)
    		return true;
    	return false;
    }
    
    //check if the player hits a 1 block high wall
    public boolean checkStep(Player pl) {
    	Location loc = pl.getLocation();
    	loc.add(pl.getEyeLocation().getDirection().add(new Vector(0, .5 , 0)));
    	if(loc.getBlock().getType() != Material.AIR)
    		return true;
    	return false;
    }
    
}