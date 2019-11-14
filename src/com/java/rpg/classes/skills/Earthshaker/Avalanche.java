package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.skills.Pyromancer.PyroclasmProjectile;
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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Avalanche extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int damage = 20;
    private int duration = 2 * 20;
    private int stun = 2;
    private int interval = 6;
    private int range = 6;
    private int rad = 2;
    
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
        makeCircle(p, t, t.getLocation());
    }
    
    public void makeCircle(Player pl, LivingEntity t, Location Loc) {
    	new BukkitRunnable() {
	    	int times = 0;
			public void run() {
				for (LivingEntity ent : Loc.getNearbyLivingEntities(rad)) {
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
		            spellDamage(pl, ent, damage);
		            //STUN 2 TICKS
		        }
				times++;
				if(times >= 5)
					cancel();
			}
    	}.runTaskTimer(Main.getInstance(), 0L, 8L);
    }
}