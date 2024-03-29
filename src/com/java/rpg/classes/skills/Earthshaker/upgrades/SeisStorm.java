package com.java.rpg.classes.skills.Earthshaker.upgrades;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SeisStorm extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int damage = 200;
    private int duration = 8 * 20;
    private int radius = 8;
    
    private double APscale = .5;
    private double ADscale = 1;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
    
    public SeisStorm() {
    	super("Seismic Storm", 100, 20, 0, 5, SkillType.CAST, null, null);
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }

	public List<String> getDescription(Player p) {
		return new ArrayList<>();
	}
    
    public void cast(Player p) {
    	super.cast(p);
        //main.getRP(p).getDmgReduction().getStatuses().add(new StatusValue("Damage Reduction" + p.getName(), 50, duration, System.currentTimeMillis(), false));
    	new BukkitRunnable() {
    		int times = 0;
    		double random;
    		public void run() {
    			for (LivingEntity ent: p.getLocation().getNearbyLivingEntities(radius)) {
    				random = 10 * Math.random();
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
    	            if (random < 1) {
	    	            ent.setKiller(p);
	    	            spellDamage(p, ent, new PhysicalStack(), new ElementalStack(), getDmg(p), 0);
    	            }
    			}
    			
    			times++;
				if(times >= duration) {
					cancel();
				}
    		}
    	}.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
}