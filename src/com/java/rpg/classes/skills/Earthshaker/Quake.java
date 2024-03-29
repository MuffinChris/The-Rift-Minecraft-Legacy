package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.skills.Earthshaker.upgrades.ObSkin;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import java.util.ArrayList;
import java.util.List;

public class Quake extends Skill {
	
    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 8;
    BlockData dust = Material.STONE.createBlockData();
    
    private double APscale = .7;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
	
    public Quake() {
    	super("Quake", 200, 200, 0, 3, SkillType.CAST, new ObSkin(),  Material.COARSE_DIRT);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fShake the Earth beneath you,"));
        desc.add(Main.color("&fDealing " + getDmg(p) + " to all enemies around you"));
        return desc;
    }
    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fShake the Earth beneath you,"));
        desc.add(Main.color("&fDealing " + damage + " to all enemies around you"));
        return desc;
    }
    
    public void cast(Player p) {
    	super.cast(p);
    	makeCircle(p,range,80,.1);
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 2.0F, 0.5F);
    	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(pl)) {
                        continue;
                    }
                }
            }
            ent.setKiller(p);
            spellDamage(p, ent, new PhysicalStack(), new ElementalStack(), getDmg(p), 0);
    	}
    }
    
    public void makeCircle(Player caster, double radius, double cnt, double height) {
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/cnt) {
            Location loc = caster.getLocation();
            Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), height, radius * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), height, radius * Math.sin( alpha + Math.PI ) );
            caster.getWorld().spawnParticle( Particle.BLOCK_DUST, firstLocation, 1, 0.01, 0.01, 0.01, 0.01, dust);
            caster.getWorld().spawnParticle( Particle.BLOCK_DUST, secondLocation, 1, 0.01, 0.01, 0.01, 0.01, dust);
        }
    }

}