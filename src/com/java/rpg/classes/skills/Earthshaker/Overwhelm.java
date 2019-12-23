package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusObject;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_14_R1.DataWatcherObject;
import net.minecraft.server.v1_14_R1.DataWatcherRegistry;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Overwhelm extends Skill {
	
    private Main main = Main.getInstance();

    private int range = 8;
    private double damage = 200;
    private int duration = 40;

    public Overwhelm() {
        super("Overwhelm", 400, 2400, 0, 3, "%player% has shot a fireball!", "CAST", 0, 0, 0, 0);
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
                if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(p)) {
                    	continue;
                    }
                }
                if (pl.equals(p)) {
                    continue;
                }
                spellDamage(p, ent, damage);
            	main.getRP(pl).getStun().getStatuses().add(new StatusValue("Stun:" + p.getName(), 1, duration, System.currentTimeMillis(), false));
            	dust(p, ent);
                return true;
            } 
    		else {
            	spellDamage(p, ent, damage);
            	//stun the entity for 2 seconds
            	//how do you stun not players
            	dust(p, ent);
            	return true;
            }   
    	}
    	return false;
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