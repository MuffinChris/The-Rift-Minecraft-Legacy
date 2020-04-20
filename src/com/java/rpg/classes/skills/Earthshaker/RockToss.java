package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RockToss extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int damage = 20;
    private int stunD = 20;
    private int range = 8;
    private int rad = 2;
    
    private double APscale = 3;
    private double ADscale = 7;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale ) / 5;
    }
    
    public RockToss() {
    	super("RockToss", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST-TARGET");
        setTargetRange(range);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSummon a large rock above an emnemy"));
        desc.add(Main.color("&fStunning all enemies for" + stunD/20 + "seconds and dealing " + getDmg(p) + "damage" ));
        desc.add(Main.color("&fAround the target. "));
        return desc;
    }
    
    public void target(Player p, LivingEntity t) {
        super.target(p, t);
        makeCircle(p,t);
    }
    
    public void makeCircle(Player p, LivingEntity t) {
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
                if(pl == p) {
                	continue;
                }
            }
            ent.setKiller(p);
            spellDamage(p, ent, getDmg(p), new ElementalStack());
            stun(p, ent, stunD);
    	}
    }
    
    public void stun(Player player, LivingEntity e, int stunDur) {
    	if (e instanceof Player) {
            Player pl = (Player) e;
        	main.getRP(pl).getStun().getStatuses().add(new StatusValue("Stun:" + player.getName(), 1, stunDur, System.currentTimeMillis(), false));
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
    
    /*public void bigExplosion(Location loc) {
    	loc.getWorld().spawnParticle(Particle.BLOCK_DUST, loc, 40, 2, 2, 2);
    }*/
    
}