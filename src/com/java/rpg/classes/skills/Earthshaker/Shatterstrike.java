package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.modifiers.utility.ElementalStack;
import com.java.rpg.classes.Skill;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class Shatterstrike extends Skill implements Listener {
	
	private Main main = Main.getInstance();
	
	private double damage = 200;
	private int range = 3;
	
	private double APscale = 1;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
	
    public Shatterstrike() {
		super("Shatterstrike", 100, 2, 10, 1, "fireball", "CAST");
	}
    
    public void warmup(Player p) {
    	super.warmup(p);
    }
    
    public void cast(Player p){
    	super.cast(p);
    	iterate(p, range);
    }
    
    public void iterate(Player p, int times) {
    	Location loc = p.getLocation();
    	Vector sline = p.getEyeLocation().getDirection();
    	for(int i =0; i < times; i++) {
        	loc.add(sline.multiply(.5));
        	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
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
                spellDamage(p, ent, getDmg(p), new ElementalStack(0, 0, 0, 50, 0));
                ent.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 5, .1, .1, .1);
        	}
    	}
    }
}	