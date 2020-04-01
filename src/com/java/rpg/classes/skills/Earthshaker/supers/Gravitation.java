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

public class Gravitation extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int damage = 20;
    private int duration = 2 * 20;
    private int radius = 6;
    
    private double pullStr = .25;
    
    private double APscale = 3;
    private double ADscale = 7;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
    
    public Gravitation() {
    	super("Gravitation", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST");
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }
    
    public void cast(Player p) {
    	super.cast(p);
    	new BukkitRunnable() {
    		int times = 0;
    		public void run() {
    			for (LivingEntity ent: p.getLocation().getNearbyLivingEntities(radius)) {
    			
    				if(ent.getLocation().distance(p.getLocation()) < 3) {
    					pull(ent, p.getLocation(), pullStr / (ent.getLocation().distance(p.getLocation())));
    				} else {
    					pull(ent, p.getLocation(), pullStr);
    				}
    				
    				if (ent instanceof ArmorStand) {
    	                continue;
    	            }
    	            if (ent instanceof Player) {
    	                Player pl = (Player) ent;
    	                if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
    	                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
    	                        continue;
    	                    }
    	                }
    	            }
    	            ent.setKiller(p);
    	            spellDamage(p, ent, getDmg(p));
   
    				times++;
    				if(times >= duration) {
    					cancel();
    				}
    			}
    		}
    	}.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
    
    public void pull(LivingEntity ent, Location t, double pf) {
    	Location entL = ent.getLocation().clone();
    	Location tar = t.clone();
    	
    	Vector vec = tar.subtract(entL).toVector();
    	ent.teleport(entL.add(vec).multiply(pf));
    }
}