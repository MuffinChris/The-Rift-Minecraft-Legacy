package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
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

public class Quake extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 8;

    public Quake() {
        super("Quake", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST-TARGET");
        DecimalFormat df = new DecimalFormat("#");
        setTargetRange(range);
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fCharge forwards with a shell of rocks around you"));
        desc.add(Main.color("&fEnemies around you are caught in the rockslide"));
        desc.add(Main.color("&fand will travel with you until you stop."));
        setDescription(desc);
    }
	
    public void cast(Player p) {
    	super.cast(p);
    	makeCircle(p);
    	for(int i = 0; i < 20; i++)
    		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
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
                /*
                if (p.equals(pl)) {
                    continue;
                    p.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 20, 0.04, 0.04, 0.04, 0.04);
                }*/
            }
            ent.setKiller(p);
            //} else{
            spellDamage(p, ent, damage);
            p.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 20, 0.04, 0.04, 0.04, 0.04);
    	}
    }
    
    public void makeCircle(Player p) {
    	Location center = p.getLocation();
    	double mag;
    	double angle;
    	for(int i = 0; i < 40; i++) {
    		mag = Math.random() * range;
    		angle = Math.random() * Math.PI;
    		//p.getWorld().spawnParticle(Particle.BLOCK_DUST, center.add(new mag * Math.cos(angle), .1, mag * Math.sin(angle), 1, .04, .04, .04, .04 );
    	}
    }
    
}