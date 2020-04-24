package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.skills.Earthshaker.upgrades.RiftStrike;
import com.java.rpg.party.Party;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RollingStone extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    private int rad = 2;
    
    public RollingStone() {
    	super("RollingStone", 100, 400, 0, 5, SkillType.CAST, new RiftStrike(), Material.STONE_SLAB);
    	DecimalFormat df = new DecimalFormat("#");
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fCharge forwards with a shell of rocks around you"));
        desc.add(Main.color("&fEnemies around you are caught in the rockslide"));
        desc.add(Main.color("&fand will travel with you until you stop."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fCharge forwards with a shell of rocks around you"));
        desc.add(Main.color("&fEnemies around you are caught in the rockslide"));
        desc.add(Main.color("&fand will travel with you until you stop."));
        return desc;
    }
    
    public void cast(Player p) {
        super.cast(p);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MINECART_RIDING, 2F, .5F);
        ArrayList<LivingEntity> hit = new ArrayList<LivingEntity> ();
        Vector face = p.getEyeLocation().getDirection().clone();
        new BukkitRunnable() {
            int times = 16;
            
            public void run() {
                if (checkCollision(p))
                    cancel();
                if (checkStep(p)) {
                    p.teleport(p.getLocation().add(new Vector(0,1,0)));
                }
                face = p.getEyeLocation().getDirection().clone();
                p.setVelocity(face.multiply(1.5));
                for (LivingEntity ent: p.getLocation().getNearbyLivingEntities(2)) {
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
                    hit.add(ent);
                }
                for(LivingEntity ent: hit) {
                	ent.teleport(p);
                }
                if (times < 1) {
                    launch(p);
                    cancel();
                }
                times--;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
    
    // moves the player and the mobs forwards
    /*public void increment(Player pl) {
    	Vector FDir = pl.getEyeLocation().getDirection();
    	Vector rand;
    	for(LivingEntity ent: pl.getLocation().getNearbyLivingEntities(rad)) {
    		rand = new Vector( .1f * Math.random(), .1f * Math.random(), .1f * Math.random());
    		if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player player = (Player) ent;
                if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(player)) {
                        continue;
                    }
                }
                if (player.equals(pl)) {
                    continue;
                }
            }
    	}
    	pl.setVelocity(FDir.multiply(2.0));
    }*/
    
    // launch enemies forwards or something idk
    public void launch(Player pl) {
    	for (LivingEntity ent: pl.getLocation().getNearbyLivingEntities(rad)) {
    		if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player player = (Player) ent;
                if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(player)) {
                        continue;
                    }
                }
                if (player.equals(pl)) {
                    continue;
                }
            }
    		ent.setVelocity(pl.getEyeLocation().getDirection().multiply(3.0));
    	}
    }
    
    //check if the player hits a wall or something
    public boolean checkCollision(Player pl) {
    	Location loc = pl.getEyeLocation();
    	loc.add(pl.getEyeLocation().getDirection().setY(0).normalize());
    	if(loc.getBlock().getType() != Material.AIR)
    		return true;
    	return false;
    }
    
    //check if the player hits a 1 block high wall
    public boolean checkStep(Player pl) {
    	Location loc = pl.getLocation();
    	Vector facing = pl.getEyeLocation().getDirection().clone();
    	loc.add(pl.getLocation().add(facing.setY(0).normalize()));
    	if(loc.getBlock().getType() != Material.AIR)
    		return true;
    	return false;
    }
    
}