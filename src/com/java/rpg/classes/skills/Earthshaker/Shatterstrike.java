package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;

public class Shatterstrike extends Skill implements Listener {
	
	private Main main = Main.getInstance();
	
	private double damage = 200;
	private double rad = 1.5;
    BlockData dust = Material.STONE.createBlockData();
	
	private double APscale = 1;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
	
    public Shatterstrike() {
		super("Shatterstrike", 100, 20, 0, 1, "fireball niqqa", "CAST");
	}
    
    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Tear a rift in the ground in front of you,"));
        desc.add(Main.color("&7dealing " + getDmg(p) + " &7damage to enemies."));
        return desc;
    }
    
    public void cast(Player p){
    	super.cast(p);
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 2.0F, .5F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 2.0F, .5F);
    	Location loc = p.getLocation().clone();
    	Vector sline = p.getEyeLocation().getDirection().clone();
    	for(int i =0; i < 4; i++) {
        	loc.add(sline.multiply(.5));
        	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(rad)) {
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
                loc.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 50, 1, 1, 1, dust);
        	}
    	}
    }
    
    /*public void iterate(Player p, int times) {
    	Location loc = p.getLocation().clone();
    	Vector sline = p.getEyeLocation().getDirection().clone();
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
                loc.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 50, 1, 1, 1, dust);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 2.0F, .5F);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 2.0F, .5F);
        	}
    	}
    }*/
}	