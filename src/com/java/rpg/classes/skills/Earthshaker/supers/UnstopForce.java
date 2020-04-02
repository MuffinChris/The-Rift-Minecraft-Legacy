package com.java.rpg.classes.skills.Earthshaker.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UnstopForce extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private double speed = .2;
    
    private double APscale = .5;
    private double ADscale = 1;

    double damage = 0;
    double detectRadius = 0;
    public double getDmg(Player p) {
        return (damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
    
    public UnstopForce() {
    	super("Unstoppable Force", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST");
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }
    
    public void cast(Player p) {
    	super.cast(p);
    	
    }
    
    public boolean detectCollision(Player p) {
    	for(LivingEntity ent: p.getLocation().getNearbyLivingEntities(detectRadius)) {
    		return true;
    	}
    	return false;
    }
    
    public void charge(Player p) {
    	Location start = p.getLocation().clone();
    	Vector face = p.getEyeLocation().getDirection().clone();
    	p.teleport(start.add(face).multiply(speed));
    }
    
    /*public void collide(Player p) {
    	for(LivingEntity ent: p.getNearbyLivingEntities(hitRad)) {
    		ent.setVelocity(new Vector(0,launchVelocity,0));
    		spellDamage(p,ent,getDmg(p));
    	}
    }*/
}