package com.java.rpg.classes.skills.Earthshaker.upgrades;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.damage.utility.PhysicalStack;
import com.java.rpg.party.Party;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UForce extends Skill implements Listener {

    private Main main = Main.getInstance();
    private double launchVelocity = 10;
    private double hitRad = 2;

    private double APscale = .5;
    private double ADscale = 1;

    double damage = 600;
    double detectRadius = 2;
    public double getDmg(Player p) {
        return (damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }

    public UForce() {
    	super("Unstoppable Force", 100, 20, 0, 5, SkillType.CAST, null, null);
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }

	public List<String> getDescription(Player p) {
		return new ArrayList<>();
	}

    public void cast(Player p) {
    	super.cast(p);
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 2.0F, 0.5F);
    	new BukkitRunnable() {
    		int times = 0;
			public void run() {
				p.setVelocity(p.getEyeLocation().getDirection().multiply(3.0));
				if(detectCollision(p)) {
					collide(p);
					cancel();
				}
				if(checkStep(p)) {
					p.teleport(p.getLocation().add(new Vector(0,1,0)));
				}
				times++;
				if (times >= 20) {
					cancel();
				}
			}
    		
    	}.runTaskTimer(main, 0L, 1L);
    }

    public boolean detectCollision(Player p) {
    	Location loc = p.getEyeLocation();
    	loc.add(p.getEyeLocation().getDirection().setY(0).normalize());
    	if(loc.getBlock().getType() != Material.AIR)
    		return true;
    	for(LivingEntity ent: p.getLocation().getNearbyLivingEntities(detectRadius)) {
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
                if(pl == p) {
                	continue;
                }
            }
    		return true;
    	}
    	return false;
    }
    
    public boolean checkStep(Player pl) {
    	Location loc = pl.getEyeLocation();
    	loc.add(pl.getEyeLocation().getDirection().setY(0).normalize().add(new Vector(0, -1, 0)));
    	if(loc.getBlock().getType() != Material.AIR)
    		return true;
    	return false;
    }

    //doesn't work pepega
    /*public void charge(Player p) {
    	Location start = p.getLocation().clone();
    	Vector face = p.getEyeLocation().getDirection().clone();
    	p.teleport(start.add(face).multiply(speed));
    }*/

    public void collide(Player p) {
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 0.5F);
    	for(LivingEntity ent: p.getLocation().getNearbyLivingEntities(hitRad)) {
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
                if(pl.equals(p)) {
                	continue;
                }
            }
    		ent.setVelocity(new Vector(0,launchVelocity,0));
    		spellDamage(p,ent,new PhysicalStack(),new ElementalStack(0,200,0,0,0), getDmg(p), 0);
    	}
    }
} 