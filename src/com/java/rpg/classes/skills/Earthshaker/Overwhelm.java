package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Overwhelm extends Skill {

    private Main main = Main.getInstance();

    private int range = 8;
    private double damage = 200;
    private int duration = 40;
    
    private double APscale = 1;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }

    public Overwhelm() {
        super("Overwhelm", 400, 2400, 0, 3, "%player% has shot a fireball!", "CAST");
        DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fUse the power of Earth to supress an enemy."));
        desc.add(Main.color("&fThe target will take 200 damage,"));
        desc.add(Main.color("&fand be stunned for 2 seconds."));
        setDescription(desc);
    }

    public void cast(Player p) {
    	Location bean  = p.getLocation();
    	Vector sightLine = p.getEyeLocation().getDirection();
    	for(int i = 0; i < range / .25; i++) {
    		bean.add(sightLine.multiply(.25));
    		if(increment(p, bean))
    			break;
    	}
    }

    public boolean increment(Player p, Location loc) {
    	for (LivingEntity ent : loc.getNearbyLivingEntities(0.5)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            else if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(p)) {
                    	continue;
                    }
                }
                if (pl.equals(p)) {
                    continue;
                }
            } 
            spellDamage(p, ent, damage);
            stun(p, ent, duration);
            dust(p, ent);
            return true;
    	}
    	return false;
    }
    
    public void stun(Player player, LivingEntity e, int stunDur) {
    	if (e instanceof ArmorStand) {
        }
        else if (e instanceof Player) {
            Player pl = (Player) e;
        	main.getRP(pl).getStun().getStatuses().add(new StatusValue("Stun:" + pl.getName(), 1, duration, System.currentTimeMillis(), false));
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
    
    public void dust(Player p, Entity ent) {
    	new BukkitRunnable() {
	    	int times = 0;
			public void run() {
                	p.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 20, 0.1, 0.1, 0.1, 0.1);
				times++;
				if(times >= duration)
					cancel();
			}
    	}.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
}