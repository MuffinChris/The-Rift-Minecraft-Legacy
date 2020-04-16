package com.java.rpg.classes.skills.Earthshaker.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.*;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Petrify extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int duration = 20;
    
    public Petrify() {
    	super("Petrify", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST");
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }
    
    public void cast(Player p) {
    	super.cast(p);
    	Location init = p.getLocation().clone();
    	Location hitCenter = init.clone();
    	Vector facing = p.getEyeLocation().getDirection().clone();
    	ArrayList<Location> hitEntities = new ArrayList<Location>();
    	hitCenter.add(facing.multiply(1));
    	for (LivingEntity ent: hitCenter.getNearbyLivingEntities(1)) {
    		if (checkRep(hitEntities, ent.getLocation())) {
    			continue;
    		}
    		if (ent instanceof ArmorStand) {
    			continue;
    		}
    		if (ent instanceof Player) {
    			Player pl = (Player) ent;
    			if(pl.equals(p)) {
    				continue;
    			}
    			if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
    		}
    		stun(p, ent, duration);
    		hitEntities.add(ent.getLocation());
    	}
    	hitCenter.add(facing.multiply(2));
    	for (LivingEntity ent: hitCenter.getNearbyLivingEntities(2)) {
    		if (checkRep(hitEntities, ent.getLocation())) {
    			continue;
    		}
    		if (ent instanceof ArmorStand) {
    			continue;
    		}
    		if (ent instanceof Player) {
    			Player pl = (Player) ent;
    			if(pl.equals(p)) {
    				continue;
    			}
    			if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
    		}
    		stun(p, ent, duration);
    		hitEntities.add(ent.getLocation());
    	}
    	hitCenter.add(facing.multiply(1));
    	for (LivingEntity ent: hitCenter.getNearbyLivingEntities(1)) {
    		if (checkRep(hitEntities, ent.getLocation())) {
    			continue;
    		}
    		if (ent instanceof ArmorStand) {
    			continue;
    		}
    		if (ent instanceof Player) {
    			Player pl = (Player) ent;
    			if(pl.equals(p)) {
    				continue;
    			}
    			if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
    		}
    		stun(p, ent, duration);
    	}
    }
    
    public boolean checkRep(ArrayList<Location> locs, Location loc) {
    	for (Location l: locs) {
    		if (l.equals(loc)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void stun(Player p, LivingEntity e, int stunDur) {
    	if (e instanceof ArmorStand) {
        }
        else if (e instanceof Player) {
            Player pl = (Player) e;
        	main.getRP(pl).getStun().getStatuses().add(new StatusValue("Stun:" + p.getName(), 1, duration, System.currentTimeMillis(), false));
        } 
		else {
			e.setAI(false);
	    	new BukkitRunnable() {
	    		int times = 0;
	    		public void run() {
	    			if(times >= stunDur) {
	    				e.setAI(true);
	    				cancel();
	    			}
	    			times++;
	    		}
	    	}.runTaskTimer(Main.getInstance(), 0L, 1L);
        }
    }
    
}