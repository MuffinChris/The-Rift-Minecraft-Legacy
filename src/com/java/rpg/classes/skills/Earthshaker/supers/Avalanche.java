package com.java.rpg.classes.skills.Earthshaker.supers;

import com.java.Main;
import com.java.rpg.modifiers.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Avalanche extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int damage = 20;
    private int duration = 2 * 20;
    private int stunD = 2;
    private int range = 6;
    private int rad = 2;
    
    private double APscale = 3;
    private double ADscale = 7;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale ) / 5;
    }
    
    public Avalanche() {
    	super("Avalanche", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST-TARGET");
    	DecimalFormat df = new DecimalFormat("#");
        setTargetRange(range);
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fUnleash an avalanche of rocks at your target."));
        desc.add(Main.color("&fOn cast, creates a zone around your target."));
        desc.add(Main.color("&fEnemies that stand in the zone"));
        desc.add(Main.color("&fwill take damage and be stunned periodically."));
        setDescription(desc);
    }
    
    public void target(Player p, LivingEntity t) {
        super.target(p, t);
        makeCircle(p, t);
    }
    
    public void makeCircle(Player pl, LivingEntity t) {
    	new BukkitRunnable() {
	    	int times = 0;
			public void run() {
				for (LivingEntity ent : t.getLocation().getNearbyLivingEntities(rad)) {
		            if (ent instanceof ArmorStand) {
		                continue;
		            }
		            if (ent instanceof Player) {
		                Player p = (Player) ent;
		                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(p).getPvp()) {
		                    if (main.getPM().getParty(p).getPlayers().contains(p)) {
		                        continue;
		                    }
		                }
		                if (p.equals(pl)) {
		                    continue;
		                }
		            }
		            spellDamage(pl, ent, getDmg(pl), new ElementalStack(0, 0, 0, 50, 0));
		            stun(pl, ent, stunD);
		        }
				times++;
				if(times >= 5)
					cancel();
			}
    	}.runTaskTimer(Main.getInstance(), 0L, 8L);
    }
    
    public void stun(Player player, LivingEntity e, int stunDur) {
    	if (e instanceof ArmorStand) {
        }
        else if (e instanceof Player) {
            Player pl = (Player) e;
        	main.getRP(pl).getStun().getStatuses().add(new StatusValue("Stun:" + player.getName(), 1, duration, System.currentTimeMillis(), false));
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