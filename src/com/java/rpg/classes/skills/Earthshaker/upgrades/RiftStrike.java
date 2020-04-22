package com.java.rpg.classes.skills.Earthshaker.upgrades;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.ElementalStack;

import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RiftStrike extends Skill implements Listener {

    private Main main = Main.getInstance();
    private double speed = .2;

    private double APscale = .5;
    private double ADscale = 1;

    double damage = 0;
    double detectRadius = 0;
    public double getDmg(Player p) {
        return (damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }

    public RiftStrike() {
    	super("RiftStrike", 100, 20, 0, 5, SkillType.CAST, null, null);
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }

	public List<String> getDescription(Player p) {
		return new ArrayList<>();
	}
	public List<String> getDescription() {
		return new ArrayList<>();
	}

    public void cast(Player p) {
    	super.cast(p);
    	Location init = p.getLocation().clone();
    	Location hitCenter = init.clone();
    	Vector facing = p.getEyeLocation().getDirection().clone();
    	ArrayList<Location> hitEntities = new ArrayList<Location>();
    	hitCenter.add(facing.multiply(0.5));
    	for (LivingEntity ent: hitCenter.getNearbyLivingEntities(0.5)) {
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
    		spellDamage(p, ent, new PhysicalStack(), new ElementalStack(), getDmg(p), 0);
    		hitEntities.add(ent.getLocation());
    	}
    	hitCenter.add(facing.multiply(1.5));
    	for (LivingEntity ent: hitCenter.getNearbyLivingEntities(1.5)) {
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
			spellDamage(p, ent, new PhysicalStack(), new ElementalStack(), getDmg(p), 0);
    		hitEntities.add(ent.getLocation());
    	}
    	hitCenter.add(facing.multiply(1.5));
    	for (LivingEntity ent: hitCenter.getNearbyLivingEntities(0.5)) {
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
			spellDamage(p, ent, new PhysicalStack(), new ElementalStack(), getDmg(p), 0);
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
} 