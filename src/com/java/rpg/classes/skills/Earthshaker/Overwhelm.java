package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.skills.Earthshaker.upgrades.Avalanche;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
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
    BlockData dust = Material.STONE.createBlockData();
    
    private double APscale = 1;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }

    public Overwhelm() {
        super("Overwhelm", 400, 2400, 0, 3, SkillType.TARGET, new Avalanche(), Material.IRON_BARS);
        setTargetRange(range);
    }
    
    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fUse the power of Earth to suppress an enemy."));
        desc.add(Main.color("&fThe target will take damage"));
        desc.add(Main.color("&f/equal to 10% of their maximum HP + " + getDmg(p)));
        desc.add(Main.color("&fand be stunned for 2 seconds."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fUse the power of Earth to suppress an enemy."));
        desc.add(Main.color("&fThe target will take damage"));
        desc.add(Main.color("&f/equal to 10% of their maximum HP + " + damage));
        desc.add(Main.color("&fand be stunned for 2 seconds."));
        return desc;
    }
    
    public void target(Player p, LivingEntity ent) {
    	super.target(p, ent);
        spellDamage(p, ent, new PhysicalStack(), new ElementalStack(),  ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.1, 0);
        stun(p, ent, duration);
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
            spellDamage(p, ent, new PhysicalStack(), new ElementalStack(), getDmg(p) + ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.1, 0);
            stun(p, ent, duration);
            dust(p, ent);
            return true;
    	}
    	return false;
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
    
    public void dust(Player p, Entity ent) {
    	new BukkitRunnable() {
	    	int times = 0;
			public void run() {
                p.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 20, dust);
				times++;
				if(times >= duration)
					cancel();
			}
    	}.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
}