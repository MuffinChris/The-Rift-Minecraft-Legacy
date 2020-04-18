package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
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
    	super("Quake", 200, 200, 0, 3, "player has shot a fireball", "CAST");
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fShake the Earth beneath you,"));
        desc.add(Main.color("&fDealing " + getDmg(p) + " to all enemies around you"));
        return desc;
    }
    
    public void cast(Player p) {
    	super.cast(p);
    	p.getWorld().spawnParticle(Particle.BLOCK_DUST, p.getLocation(), 40, 4, 1, 4, dust);
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
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
            spellDamage(p, ent, getDmg(p), new ElementalStack(0, 0, 0, 50, 0));
    	}
    }

}